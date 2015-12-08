package com.example.anna.octopuschat.API;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.anna.octopuschat.Constants;
import com.example.anna.octopuschat.dbTables.Message;
import com.example.anna.octopuschat.dbTables.Profile;
import com.example.anna.octopuschat.dbTables.User;
import com.example.anna.octopuschat.interfaces.AuthorizeListener;
import com.example.anna.octopuschat.interfaces.MessagesListener;
import com.example.anna.octopuschat.interfaces.RegisterListener;
import com.example.anna.octopuschat.interfaces.UsersListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anna on 06/12/15.
 */

public class APIManager {

    private static APIManager instance;
    private AsyncHttpClient asyncHttpClient;
    private static Context mContext;

    public static void init(Context context) {
        if (instance == null) {
            instance = new APIManager(context);
        }
    }

    private APIManager(Context context) {
        mContext = context;
        asyncHttpClient = new AsyncHttpClient();
    }

    public static APIManager getInstance() {
        return instance;
    }

    public static boolean isConnectionAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else return false;
    }

    //Users

    public void register(final String username, final String password, final RegisterListener listener) {
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);
        asyncHttpClient.post(mContext, Constants.BASE_API_ADDRESS + "registration.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (listener != null) {
                    try {
                        String token = response.getString("token");
                        asyncHttpClient.addHeader("x-token", token);

                        Profile profile = new Profile();
                        profile.username = username;
                        profile.password = password;
                        profile.userId = response.getInt("id");
                        profile.token = token;
                        profile.save();

                        listener.onRegisterSuccess();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (listener != null) {
                    try {
                        listener.onRegisterFailure(statusCode, errorResponse.getString("error"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void authorize(final String username, final String password, final AuthorizeListener listener) {
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);
        asyncHttpClient.post(mContext, Constants.BASE_API_ADDRESS + "auth.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (listener != null) {
                    try {
                        String token = response.getString("token");
                        asyncHttpClient.addHeader("x-token", token);
                        Profile profile = Profile.getProfile();
                        if (profile == null)
                            profile = new Profile();
                        profile.username = username;
                        profile.password = password;
                        profile.userId = response.getInt("id");
                        profile.token = token;
                        profile.save();
                        listener.onAuthorizeSuccess();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (listener != null) {
                    try {
                        listener.onAuthorizeFailure(statusCode, errorResponse.getString("error"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void getUsers(final UsersListener listener) {
        asyncHttpClient.get(mContext, Constants.BASE_API_ADDRESS + "users.php", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject userJSON = response.getJSONObject(i);
                        User user = User.getUser(userJSON);
                        if (user != null)
                            user.createOrUpdate();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (listener != null) {
                    listener.onGetUsersSuccess();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (listener != null) {
                    try {
                        listener.onGetUsersFailure(statusCode, errorResponse.getString("error"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    //Messages

    public void getMessages(final int contactId, int lastMessageId, final MessagesListener listener) {
        RequestParams params = new RequestParams();
        params.put("recipient_id", contactId);
        params.put("last_message_id", lastMessageId);
        asyncHttpClient.get(mContext, Constants.BASE_API_ADDRESS + "messages.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Message> messages = new ArrayList<>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject messageJSON = response.getJSONObject(i);
                        int messageId = messageJSON.getInt("id");
                        Message message = Message.getMessage(messageId);
                        if (message == null) {
                            message = Message.getMessage(messageJSON);
                            if (message != null) {
                                message.save();
                                messages.add(message);
                            }
                        }
                    }
                    if (messages.size() > 0) {
                        User companion = User.getUser(contactId);
                        companion.lastMessageIndex = messages.get(messages.size() - 1).messageId;
                        companion.save();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (listener != null) {
                    listener.onGetMessagesSuccess(messages);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.w("GET_MESSAGES", errorResponse.getString("error"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void postMessage(final int recipient_id, int last_message_id, String message, final MessagesListener listener) {
        RequestParams params = new RequestParams();
        params.put("recipient_id", recipient_id);
        params.put("last_message_id", last_message_id);
        params.put("message_text", message);
        asyncHttpClient.post(mContext, Constants.BASE_API_ADDRESS + "messages.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Message> messages = new ArrayList<Message>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject messageJSON = response.getJSONObject(i);
                        int messageId = messageJSON.getInt("id");
                        Message message = Message.getMessage(messageId);
                        if (message == null) {
                            message = Message.getMessage(messageJSON);
                            if (message != null) {
                                message.save();
                                messages.add(message);
                            }
                        }
                    }
                    if (messages.size() > 0) {
                        User companion = User.getUser(recipient_id);
                        companion.lastMessageIndex = messages.get(messages.size() - 1).messageId;
                        companion.save();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (listener != null) {
                    listener.onGetMessagesSuccess(messages);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.w("POST_MESSAGE", errorResponse.getString("error"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}