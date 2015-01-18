package com.gurusolution.android.hangman2.utils;

public interface IRequestListener {
    public void onResponse(String response);

    public void onErrorResponse(String errorMessage);
}
