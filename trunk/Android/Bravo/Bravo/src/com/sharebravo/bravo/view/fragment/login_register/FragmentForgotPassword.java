package com.sharebravo.bravo.view.fragment.login_register;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObPostForgot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPost;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.EmailValidator;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentForgotPassword extends FragmentBasic {
    // ====================Constant Define=================
    // ====================Class Define====================
    private EmailValidator mEmailValidator;
    // ====================Variable Define=================
    private EditText       mEditTextEmailForgot;
    private Button         mBtnResetPassword;
    private boolean        isEmailNotVaid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_forgot_password, container);
        mEmailValidator = new EmailValidator();
        initializeView(root);
        return root;
    }

    private void initializeView(View root) {
        mEditTextEmailForgot = (EditText) root.findViewById(R.id.edittext_input_email_reset_pw);
        mEditTextEmailForgot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isEmailNotVaid) {
                    isEmailNotVaid = false;
                    mEditTextEmailForgot.setText("");
                    mEditTextEmailForgot.setHint(getString(R.string.email_address));
                    mEditTextEmailForgot.setHintTextColor(getActivity().getResources().getColor(R.color.black));
                }
            }
        });
        mBtnResetPassword = (Button) root.findViewById(R.id.btn_reset_password);
        mBtnResetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickResetPassword();
            }
        });
    }

    private void onClickResetPassword() {
        String email = mEditTextEmailForgot.getText().toString();
        if (StringUtility.isEmpty(email)) {
            isEmailNotVaid = true;
            mEditTextEmailForgot.setText("");
            mEditTextEmailForgot.setHint(getString(R.string.email_is_empty));
            mEditTextEmailForgot.setHintTextColor(getActivity().getResources().getColor(R.color.red));
        } else if (checkValidateEmail(email)) {
            requestToCheckForgotPassword(email);
        } else {
            isEmailNotVaid = true;
            mEditTextEmailForgot.setText("");
            mEditTextEmailForgot.setHint(getString(R.string.email_not_valid));
            mEditTextEmailForgot.setHintTextColor(getActivity().getResources().getColor(R.color.red));
        }

    }

    private void requestToCheckForgotPassword(String email) {
        AIOLog.d("email:=" + email);
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Email", email);
        JSONObject jsonObject = new JSONObject(subParams);
        String subParamsStr = jsonObject.toString();
        List<NameValuePair> params = ParameterFactory.createSubParams(subParamsStr);
        AsyncHttpPost postForgotPassword = new AsyncHttpPost(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response " + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObPostForgot obPostForgot = gson.fromJson(response.toString(), ObPostForgot.class);
                AIOLog.d("obPostForgot.status:" + obPostForgot.status);
                if (obPostForgot.status == BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS) {
                    showToast(getActivity().getResources().getString(R.string.check_in_your_email_and_change_pass_word));
                } else {
                    showToast(obPostForgot.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        postForgotPassword.execute(BravoWebServiceConfig.URL_POST_FORGOT);
    }

    private boolean checkValidateEmail(String email) {
        if (mEmailValidator.validate(email))
            return true;
        else {
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
}
