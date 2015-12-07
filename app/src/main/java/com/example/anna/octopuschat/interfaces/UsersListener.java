package com.example.anna.octopuschat.interfaces;

/**
 * Created by anna on 07/12/15.
 */
public interface UsersListener {
    void onGetUsersSuccess();
    void onGetUsersFailure(int code, String errorString);
}
