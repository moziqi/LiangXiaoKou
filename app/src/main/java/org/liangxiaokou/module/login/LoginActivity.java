package org.liangxiaokou.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.liangxiaokou.app.GeneralActivity;
import org.liangxiaokou.app.MApplication;
import org.liangxiaokou.app.ToolBarActivity;
import org.liangxiaokou.bean.User;
import org.liangxiaokou.module.R;
import org.liangxiaokou.module.home.HomeActivity;
import org.liangxiaokou.module.invite.InviteActivity;
import org.liangxiaokou.module.register.RegisterActivity;
import org.liangxiaokou.module.welcome.WelcomeActivity;
import org.liangxiaokou.util.KeyBoardUtils;
import org.mo.netstatus.NetUtils;

public class LoginActivity extends ToolBarActivity implements ILoginView {

    private TextInputLayout mTextInputUsername;
    private TextInputLayout mTextInputPassword;
    private TextView mTvForgetPassword;
    private TextView mTvRegister;
    private Button mBtnLogin;

    private LoginPresenter loginPresenter = new LoginPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean isOverridePendingTransition() {
        return true;
    }

    @Override
    protected PendingTransitionMode getPendingTransitionMode() {
        return PendingTransitionMode.TOP;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void initView() {
        getWindow().setBackgroundDrawable(null);
        mTextInputUsername = (TextInputLayout) findViewById(R.id.textInput_username);
        mTextInputPassword = (TextInputLayout) findViewById(R.id.textInput_password);
        mTvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        mTvRegister = (TextView) findViewById(R.id.tv_register);
        mBtnLogin = (Button) findViewById(R.id.btn_login);

    }

    @Override
    public void initData() {
        User currentUser = User.getCurrentUser(getApplicationContext(), User.class);
        mTextInputUsername.getEditText().setText(currentUser != null ? currentUser.getUsername() : "");
        mTvRegister.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void PreOnStart() {

    }

    @Override
    public void PreOnResume() {
    }

    @Override
    public void PreOnRestart() {

    }

    @Override
    public void PreOnPause() {
    }

    @Override
    public void PreOnStop() {
        //KeyBoardUtils.closeKeybord(mTextInputUsername.getEditText(), getApplicationContext());
    }

    @Override
    public void PreOnDestroy() {
        //KeyBoardUtils.closeKeybord(mTextInputUsername.getEditText(), getApplicationContext());
    }

    @Override
    public boolean PreOnKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register: {
                startActivity(RegisterActivity.class);
            }
            break;
            case R.id.btn_login: {
                KeyBoardUtils.closeKeybord(mTextInputPassword.getEditText(), getApplicationContext());
                loginPresenter.toLoginByAccount(this);
            }
            break;
        }
    }

    @Override
    public String getUsername() {
        return mTextInputUsername.getEditText().getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return mTextInputPassword.getEditText().getText().toString().trim();
    }

    @Override
    public void onSuccess(User user) {
        //判断是否完善个人信息(性别，昵称)
        if (!user.getIsOk()) {
            finish();
            //需要完善个人资料
            startActivity(WelcomeActivity.class);
        } else if (!user.getHaveLove()) {
            finish();
            //需要添加好友
            startActivity(InviteActivity.class);
        } else {
            //清除所有activity
            MApplication.getInstance().AppExit();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("LoginActivity_code", 0);
            startActivity(intent);
        }
    }

    @Override
    public void showLoading() {
        alertDialog.show();
    }

    @Override
    public void hideLoading() {
        alertDialog.dismiss();
    }

    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure(int code, String msg) {
        showToast(LoginActivity.class.getName() + " code is " + code + " and msg is " + msg);
    }

}
