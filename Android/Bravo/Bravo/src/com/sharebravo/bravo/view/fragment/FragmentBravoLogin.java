package com.sharebravo.bravo.view.fragment;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.model.response.ObPostUserSuccess;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.EmailValidator;
import com.sharebravo.bravo.utils.StringUtility;

public class FragmentBravoLogin extends FragmentBasic {
    // ===================Constant Define ==========================
    // ===================Class Define =============================
    private EmailValidator          mEmailValidator;
    private IShowPageForgotPassword mListener;
    // ===================Variable Define ==========================
    private Button                  mBtnBravoLogin;
    private EditText                mEditTextUserEmail;
    private EditText                mEditTextPassWord;
    private TextView                mTextForgotPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_bravo_login, container);

        mEmailValidator = new EmailValidator();
        initializeView(root);
        return root;
    }

    private void initializeView(View root) {
        mBtnBravoLogin = (Button) root.findViewById(R.id.btn_bravo_login);
        mBtnBravoLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = mEditTextUserEmail.getText().toString();
                String passWord = mEditTextPassWord.getText().toString();
                if (isValidEmail_PassWord(email, passWord)) {
                    requestToLoginByBravoAccount(email, passWord);
                }
            }
        });
        mTextForgotPassword = (TextView) root.findViewById(R.id.text_forgot_password);
        mTextForgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.showPageForgotPassword();
            }
        });

        mEditTextUserEmail = (EditText) root.findViewById(R.id.edittext_input_email);
        mEditTextPassWord = (EditText) root.findViewById(R.id.edittext_input_pass);
    }

    private void requestToLoginByBravoAccount(String email, String passWord) {
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Email", email);
        subParams.put("Password", passWord);
        // JSONObject jsonObject = new JSONObject(subParams);
        // String subParamsStr = jsonObject.toString();

        String userId = getUserIdFromUserBravoInfo();
        String url = BravoWebServiceConfig.URL_GET_USER_INFO_WITH_BRAVO_ACCOUNT + "/" + userId;
        List<NameValuePair> params = ParameterFactory.createSubParamsLoginBravoAccount(email, passWord);
        AsyncHttpGet getLoginRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestToLoginByBravoAccount:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetUserInfo obGetUserInfo = gson.fromJson(response.toString(), ObGetUserInfo.class);
                if (obGetUserInfo == null) {
                    showToast(getActivity().getResources().getString(R.string.username_password_not_valid));
                } else if (StringUtility.isEmpty(obGetUserInfo.data.New_Access_Token)) {
                    showToast(getActivity().getResources().getString(R.string.username_password_not_valid));
                } else {
                    showToast("Hello " + obGetUserInfo.data.Full_Name);
                    Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    getActivity().finish();
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getLoginRequest.execute(url);
    }

    private String getUserIdFromUserBravoInfo() {
        String userBravoInfo = BravoSharePrefs.getInstance(getActivity()).getStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_BRAVO);
        Gson gson = new GsonBuilder().serializeNulls().create();
        ObPostUserSuccess obPostUserSuccess = gson.fromJson(userBravoInfo.toString(), ObPostUserSuccess.class);
        if (obPostUserSuccess == null)
            return "";
        return obPostUserSuccess.data.User_ID;
    }

    private boolean isValidEmail_PassWord(String email, String passWord) {
        if (mEmailValidator.validate(email))
            if (passWord.length() >= 8)
                return true;
            else {
                mEditTextPassWord.setError(getActivity().getResources().getString(R.string.password_not_valid));
                return false;
            }
        else {
            mEditTextUserEmail.setError(getActivity().getResources().getString(R.string.email_not_valid));
            return false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface IShowPageForgotPassword {
        public void showPageForgotPassword();
    }

    public void setListener(IShowPageForgotPassword iShowPageForgotPassword) {
        this.mListener = iShowPageForgotPassword;
    }
}
