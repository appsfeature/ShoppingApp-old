package com.appsfeature.global.login;

public interface LoginListener<T> {
    void onPreExecute();

    void onSuccess(T var1);

    void onError(Exception var1);
}

