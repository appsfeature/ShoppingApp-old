package com.appsfeature.global.login;


import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.appsfeature.global.AppApplication;
import com.appsfeature.global.R;
import com.appsfeature.global.model.UserModel;
import com.appsfeature.global.network.NetworkManager;
import com.appsfeature.global.util.AppPreference;
import com.appsfeature.global.util.ClassUtil;
import com.appsfeature.global.util.SupportUtil;
import com.appsfeature.login.fragment.BaseFragment;
import com.appsfeature.login.util.FieldValidation;
import com.dynamic.network.NetworkModel;
import com.helper.callback.Response;
import com.progressbutton.ProgressButton;

/*
 * Created by Admin on 5/22/2017.
 */

public class LoginFragment extends BaseFragment {

    private EditText etMobileNo, etOTP;
    private ProgressButton btnAction;
    private Activity activity;
    private View ivEditNumber;
//    private LinearLayout llSignup, llForgot;
//    private CheckBox cbRememberMe;

    @NonNull
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        activity = getActivity();
        initArguments(getArguments());
        initToolBarTheme(getActivity(), v, getTitle());
        InitUI(v);
        return v;
    }

    private String getTitle() {
        return "Login";
    }

    private void initArguments(Bundle arguments) {
//        if (arguments != null) {
//            loginType = arguments.getInt(AppConstant.EXTRA_PROPERTY);
//        }
    }

    private void InitUI(@NonNull View v) {
        etMobileNo = v.findViewById(R.id.et_mobile_no);
        etOTP = v.findViewById(R.id.et_otp);
        ivEditNumber = v.findViewById(R.id.iv_edit_number);
        ivEditNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibleOtpView(false);
            }
        });
//        llSignup = v.findViewById(R.id.ll_signup);
//        llForgot = v.findViewById(R.id.ll_forgot);
//        cbRememberMe = v.findViewById(R.id.cb_remember_me);

//        String username = AppPreference.getUsername(loginType);
//        etMobileNo.setText(username);
//        etOTP.setText(AppPreference.getPassword(loginType));
//
//        cbRememberMe.setChecked(!TextUtils.isEmpty(username));

        if(AppApplication.getInstance().isDebugMode()){
            etMobileNo.setText("9452786259");
            etOTP.setText("123");
        }

//        if (TextUtils.isEmpty(AppData.forgetPasswordUrls[loginType])) {
//            llForgot.setVisibility(View.GONE);
//        }

        btnAction = ProgressButton.newInstance(getContext(), v)
                .setText(getString(R.string.login))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SupportUtil.hideKeybord(getActivity());
                        if(etOTP.getVisibility() == View.VISIBLE){
                            if (!FieldValidation.isEmpty(getContext(), etMobileNo)) {
                                return;
                            }
                            if (!FieldValidation.isEmpty(getContext(), etOTP)) {
                                return;
                            }
                            verifyOtp(etMobileNo.getText().toString(), etOTP.getText().toString());
                        }else {
                            if (!FieldValidation.isEmpty(getContext(), etMobileNo)) {
                                return;
                            }
                            sendOtp(etMobileNo.getText().toString());
                        }
                    }
                });

//        if (TextUtils.isEmpty(AppData.signUpUrl[loginType])) {
//            llSignup.setVisibility(View.GONE);
//        }
//        llSignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String signUpUrl = AppData.signUpUrl[loginType];
//                ClassUtil.openLink(activity, AppData.signUpTitle[loginType], signUpUrl, true);
//            }
//        });
//
//        llForgot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ClassUtil.openLink(activity, "Forgot Password", AppData.forgetPasswordUrls[loginType], true);
//            }
//        });
        btnAction.setText("Send OTP");
        btnAction.setOnEditorActionListener(etOTP, "Submit");

    }

//    private void saveFields(boolean checked, String userName, String password) {
//        if (checked) {
//            AppPreference.setUsername(loginType, userName);
//            AppPreference.setPassword(loginType, password);
//        } else {
//            AppPreference.setUsername(loginType, "");
//            AppPreference.setPassword(loginType, "");
//        }
//    }


    private void sendOtp(String username) {
        NetworkManager.getInstance(getContext())
                .login(username, new Response.Callback<NetworkModel>() {
                    @Override
                    public void onProgressUpdate(Boolean isShow) {
                        btnAction.startProgress();
                    }

                    @Override
                    public void onSuccess(NetworkModel response) {
//                        AppPreference.setSessionLoginUrl(response.getUserLoginUrl());
//                        AppPreference.setUserLoggedIn(loginType);
//                        saveFields(cbRememberMe.isChecked(), username);
                        btnAction.revertProgress();
                        visibleOtpView(true);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        btnAction.revertProgress();
                        SupportUtil.showToast(getActivity(), e.getMessage());
                    }
                });

    }

    private void visibleOtpView(boolean isVisible) {
        if(isVisible){
            etMobileNo.setEnabled(false);
            etMobileNo.setAlpha(0.3f);
            etOTP.setVisibility(View.VISIBLE);
            ivEditNumber.setVisibility(View.VISIBLE);
            btnAction.setText("Verify");
        }else {
            etOTP.setVisibility(View.GONE);
            etOTP.setText("");
            ivEditNumber.setVisibility(View.GONE);
            etMobileNo.setEnabled(true);
            etMobileNo.requestFocus();
            etMobileNo.setAlpha(1f);
            btnAction.setText("Send OTP");
        }
    }

    private void verifyOtp(String mobile, String otp) {
        NetworkManager.getInstance(getContext())
                .verifyOtp(mobile, otp, new Response.Callback<UserModel>() {
                    @Override
                    public void onProgressUpdate(Boolean isShow) {
                        btnAction.startProgress();
                    }

                    @Override
                    public void onSuccess(UserModel response) {
                        btnAction.revertSuccessProgress(new ProgressButton.Listener() {
                            @Override
                            public void onAnimationCompleted() {
                                if (activity != null) {
                                    activity.finish();
                                }
                                if(TextUtils.isEmpty(AppPreference.getCountry())){
                                    ClassUtil.openPreferenceActivity(activity);
                                }else {
                                    AppApplication.getInstance().updateLoginListener();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        btnAction.revertProgress();
                        SupportUtil.showToast(getActivity(), e.getMessage());
                    }
                });

    }
}