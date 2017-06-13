package com.wangzy.joker.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.common.util.StringUtils;
import com.common.util.Tool;
import com.common.util.ValidateTool;
import com.wangzy.joker.R;

import java.security.acl.Acl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseJokeActivity {


    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.editTextUserName)
    EditText editTextUserName;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.editTextPasswordConfirm)
    EditText editTextPasswordConfirm;
    @BindView(R.id.viewLoginCenter)
    LinearLayout viewLoginCenter;
    @BindView(R.id.buttonRegister)
    Button buttonRegister;
    @BindView(R.id.viewLoginContent)
    RelativeLayout viewLoginContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.buttonRegister)
    public void register() {

        String email = getInput(editTextEmail);
        if (!ValidateTool.checkEmail(email)) {
            editTextEmail.startAnimation(animationShake);
            return;
        }

        String userName = getInput(editTextUserName);
        if (StringUtils.isEmpty(userName)) {
            editTextUserName.startAnimation(animationShake);
            return;
        }

        String password = getInput(editTextPassword);
        if (StringUtils.isEmpty(password)) {
            editTextPassword.startAnimation(animationShake);
            return;
        }

        String rpasword = getInput(editTextPasswordConfirm);
        if (!rpasword.equals(password)) {
            Tool.ToastShow(this, "两次密码不一致");
            return;
        }


        AVUser avUser = new AVUser();
        avUser.setUsername(userName);
        avUser.setPassword(password);
        avUser.setEmail(email);
        avUser.put("level", "author_register");


        showProgressDialog(false);
        avUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                hideProgressDialog();

                if (null == e) {
                    Tool.ToastShow(RegisterActivity.this, "注册成功！");
                    setResult(RESULT_OK);
                    finish();
                } else {

                    showAVException(RegisterActivity.this,e);
                }

            }
        });


    }


}
