package com.example.anna.octopuschat.interfaces;

/**
 * Created by anna on 07/12/15.
 */
public interface RegisterListener {
    void onRegisterSuccess();
    void onRegisterFailure(int code, String errorMessage);
}
