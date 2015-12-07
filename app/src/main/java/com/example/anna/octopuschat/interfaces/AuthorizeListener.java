package com.example.anna.octopuschat.interfaces;

/**
 * Created by anna on 07/12/15.
 */
public interface AuthorizeListener {
    void onAuthorizeSuccess();
    void onAuthorizeFailure(int code, String errorMessage);
}
