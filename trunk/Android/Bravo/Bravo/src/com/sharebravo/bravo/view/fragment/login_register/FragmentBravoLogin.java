package com.sharebravo.bravo.view.fragment.login_register;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;

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
import com.sharebravo.bravo.model.response.ObGetUserInfoWithBravoAccount;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.EmailValidator;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

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
    private boolean                 isUsernamenotValid;
    private boolean                 isPasswordnotValid;

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
                if(StringUtility.isEmpty(email)){
                    isUsernamenotValid = true;
                    mEditTextUserEmail.setText("");
                    mEditTextUserEmail.setHint(getString(R.string.email_is_empty));
                    mEditTextUserEmail.setHintTextColor(getActivity().getResources().getColor(R.color.red));
                    return;
                }
                if(StringUtility.isEmpty(passWord)){
                    isPasswordnotValid = true;
                    mEditTextPassWord.setText("");
                    mEditTextPassWord.setHint(getString(R.string.password_is_empty));
                    mEditTextPassWord.setHintTextColor(getActivity().getResources().getColor(R.color.red));
                    return;
                }
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
        mEditTextPassWord.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isPasswordnotValid) {
                    isPasswordnotValid = false;
                    mEditTextPassWord.setText("");
                    mEditTextPassWord.setHint(getString(R.string.pass_word));
                    mEditTextPassWord.setHintTextColor(getActivity().getResources().getColor(R.color.black));
                }
            }
        });
        mEditTextUserEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isUsernamenotValid) {
                    isUsernamenotValid = false;
                    mEditTextUserEmail.setText("");
                    mEditTextUserEmail.setHint(getString(R.string.forgot_mailaddress));
                    mEditTextUserEmail.setHintTextColor(getActivity().getResources().getColor(R.color.black));
                }
            }
        });
    }

    private void requestToLoginByBravoAccount(String email, String passWord) {
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Email", email);
        subParams.put("Password", passWord);

        String url = BravoWebServiceConfig.URL_GET_USER_INFO_WITH_BRAVO_ACCOUNT;
        List<NameValuePair> params = ParameterFactory.createSubParamsLoginBravoAccount(email, passWord);
        AsyncHttpGet getLoginRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(),this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestToLoginByBravoAccount:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetUserInfoWithBravoAccount obGetUserInfoWithBravoAccount = gson.fromJson(response.toString(), ObGetUserInfoWithBravoAccount.class);
                if (obGetUserInfoWithBravoAccount == null) {
                    showToast(getActivity().getResources().getString(R.string.username_password_not_valid));
                } else if (obGetUserInfoWithBravoAccount.status == BravoConstant.STATUS_FAILED) {
                    showToast(getActivity().getResources().getString(R.string.username_password_not_valid));
                } else {
                    /* save data to share preferences */
                    BravoUtils.saveResponseToSharePreferences(getActivity(), BravoConstant.LOGIN_BY_BRAVO_ACC, response);

                    /* go to home screen */
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

    private boolean isValidEmail_PassWord(String email, String passWord) {
        if (mEmailValidator.validate(email))
            if (passWord.length() >= 8)
                return true;
            else {
                isPasswordnotValid = true;
                mEditTextPassWord.setText("");
                mEditTextPassWord.setHint(getString(R.string.signup_bravo_password_error));
                mEditTextPassWord.setHintTextColor(getActivity().getResources().getColor(R.color.red));
                return false;
            }
        else {
            isUsernamenotValid = true;
            mEditTextUserEmail.setText("");
            mEditTextUserEmail.setHint(getString(R.string.signup_bravo_regex_error));
            mEditTextUserEmail.setHintTextColor(getActivity().getResources().getColor(R.color.red));
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
