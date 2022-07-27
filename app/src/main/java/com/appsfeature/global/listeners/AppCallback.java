package com.appsfeature.global.listeners;

import com.appsfeature.global.model.PresenterModel;
import com.helper.callback.Response;

public class AppCallback {
    public interface View extends Response.Progress {
        void onUpdateUI(PresenterModel response);
        void onErrorOccurred(Exception e);
    }
}
