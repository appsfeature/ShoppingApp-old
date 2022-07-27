package com.appsfeature.global.login;


import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.appsfeature.global.R;
import com.appsfeature.global.listeners.LoginType;
import com.appsfeature.global.model.SessionModel;
import com.appsfeature.global.network.NetworkManager;
import com.appsfeature.global.util.AppConstant;
import com.appsfeature.global.util.AppData;
import com.appsfeature.global.util.AppPreference;
import com.appsfeature.global.util.ClassUtil;
import com.appsfeature.global.util.SupportUtil;
import com.appsfeature.login.fragment.BaseFragment;
import com.appsfeature.login.util.FieldValidation;
import com.progressbutton.ProgressButton;

/*
 * Created by Admin on 5/22/2017.
 */

public class LoginFragment extends BaseFragment {

    private EditText etUsername, etPassword;
    private LinearLayout llSignup, llForgot;
    private ProgressButton btnAction;
    private Activity activity;
    private int loginType = LoginType.TYPE_COURSE;
    private CheckBox cbRememberMe;

    @NonNull
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        activity = getActivity();
        initArguments(getArguments());
        initToolBarTheme(getActivity(), v, getTitle());
        InitUI(v);
        return v;
    }

    private String getTitle() {
        return AppData.loginTitle[loginType];
    }

    private void initArguments(Bundle arguments) {
        if (arguments != null) {
            loginType = arguments.getInt(AppConstant.EXTRA_PROPERTY);

        }
    }

    private void InitUI(@NonNull View v) {

        etUsername = v.findViewById(R.id.et_employee_username);
        etPassword = v.findViewById(R.id.et_employee_password);
        llSignup = v.findViewById(R.id.ll_signup);
        llForgot = v.findViewById(R.id.ll_forgot);
        cbRememberMe = v.findViewById(R.id.cb_remember_me);

        String username = AppPreference.getUsername(loginType);
        etUsername.setText(username);
        etPassword.setText(AppPreference.getPassword(loginType));

        cbRememberMe.setChecked(!TextUtils.isEmpty(username));

//        if(BuildConfig.DEBUG){
//            if(loginType == LoginType.TYPE_COURSE) {
//                etUsername.setText("caamitjain1982@gmail.com");
//                etPassword.setText("jain1982");
//            }else if(loginType == LoginType.TYPE_TOOLS) {
//                etUsername.setText("dummy@dummy.com");
//                etPassword.setText("dummy123");
//            }
//        }

        if (TextUtils.isEmpty(AppData.forgetPasswordUrls[loginType])) {
            llForgot.setVisibility(View.GONE);
        }

        btnAction = ProgressButton.newInstance(getContext(), v)
                .setText(getString(R.string.login))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!FieldValidation.isEmpty(getContext(), etUsername)) {
                            return;
                        } else if (!FieldValidation.isEmpty(getContext(), etPassword)) {
                            return;
                        }
                        SupportUtil.hideKeybord(getActivity());
                        executeTask();
                    }
                });

        if (TextUtils.isEmpty(AppData.signUpUrl[loginType])) {
            llSignup.setVisibility(View.GONE);
        }
        llSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String signUpUrl = AppData.signUpUrl[loginType];
                ClassUtil.openLink(activity, AppData.signUpTitle[loginType], signUpUrl, true);
            }
        });

        llForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassUtil.openLink(activity, "Forgot Password", AppData.forgetPasswordUrls[loginType], true);
            }
        });

        btnAction.setOnEditorActionListener(etPassword, "Submit");

    }

    private void saveFields(boolean checked, String userName, String password) {
        if (checked) {
            AppPreference.setUsername(loginType, userName);
            AppPreference.setPassword(loginType, password);
        } else {
            AppPreference.setUsername(loginType, "");
            AppPreference.setPassword(loginType, "");
        }
    }


    private void executeTask() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        NetworkManager.getInstance(getContext())
                .login(username, password, loginType, new LoginListener<SessionModel>() {
                    @Override
                    public void onPreExecute() {
                        btnAction.startProgress();
                    }

                    @Override
                    public void onSuccess(SessionModel response) {
                        AppPreference.setSessionLoginUrl(response.getUserLoginUrl());
                        AppPreference.setUserLoggedIn(loginType);
                        saveFields(cbRememberMe.isChecked(), username, password);
                        btnAction.revertSuccessProgress(new ProgressButton.Listener() {
                            @Override
                            public void onAnimationCompleted() {
                                if (activity != null) {
                                    ClassUtil.openLink(activity, "", response.getUserLoginUrl(), false);
                                    activity.finish();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        btnAction.revertProgress();
                        SupportUtil.showToast(getActivity(), e.getMessage());
                    }
                });

    }
}