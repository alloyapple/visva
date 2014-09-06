package com.sharebravo.bravo.view.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentSetting extends FragmentBasic {
    // =======================Constant Define==============
    // =======================Class Define=================
    private IShowPageTermOfUse iShowPageTermOfUse;
    // =======================Variable Define==============
    private TextView           mTextTermOfUse;
    private TextView           mTextUpdateUserInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_settings, container);

        initializeView(root);
        return root;
    }

    private void initializeView(View root) {
        mTextTermOfUse = (TextView) root.findViewById(R.id.text_term_of_use);
        mTextUpdateUserInfo = (TextView) root.findViewById(R.id.text_edit_profile);

        mTextUpdateUserInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showToast("This feature is comming soon");
            }
        });
        mTextTermOfUse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iShowPageTermOfUse.showPageTermOfUse();
            }
        });
    }

    public interface IShowPageTermOfUse {
        public void showPageTermOfUse();
    }

    public void setListener(IShowPageTermOfUse iShowPageTermOfUse) {
        this.iShowPageTermOfUse = iShowPageTermOfUse;
    }

}
