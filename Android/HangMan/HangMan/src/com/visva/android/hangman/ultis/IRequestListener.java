package com.visva.android.hangman.ultis;

public interface IRequestListener {
    public void onResponse(String response);

    public void onErrorResponse(String errorMessage);
}
