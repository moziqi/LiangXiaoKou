package org.liangxiaokou.module.person;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.liangxiaokou.app.GeneralActivity;
import org.liangxiaokou.app.MApplication;
import org.liangxiaokou.app.SwipeBackActivity;
import org.liangxiaokou.app.ToolBarActivity;
import org.liangxiaokou.bean.User;
import org.liangxiaokou.config.Constants;
import org.liangxiaokou.module.R;
import org.liangxiaokou.module.login.LoginActivity;
import org.liangxiaokou.widget.view.CircleImageView;

import java.lang.ref.WeakReference;

import cn.bmob.v3.BmobUser;

public class PersonActivity extends SwipeBackActivity {

    private RelativeLayout mRlHeader;
    private CircleImageView mIvHeader;
    private RelativeLayout mRlNick;
    private TextView mTvNickRight;
    private RelativeLayout mRlSex;
    private TextView mTvSexRight;
    private Button mBtnLogout;
    private TextView mTvUpdatePassword;

    private static class StaticHandler extends Handler {
        private final WeakReference<Activity> reference;

        public StaticHandler(Activity activity) {
            this.reference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private StaticHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        showActionBarBack(true);
    }

    @Override
    public boolean isOverridePendingTransition() {
        return false;
    }

    @Override
    protected PendingTransitionMode getPendingTransitionMode() {
        return PendingTransitionMode.RIGHT;
    }

    @Override
    public void initView() {
        mRlHeader = (RelativeLayout) findViewById(R.id.rl_header);
        mIvHeader = (CircleImageView) findViewById(R.id.iv_header);
        mRlNick = (RelativeLayout) findViewById(R.id.rl_nick);
        mTvNickRight = (TextView) findViewById(R.id.tv_nick_right);
        mRlSex = (RelativeLayout) findViewById(R.id.rl_sex);
        mTvSexRight = (TextView) findViewById(R.id.tv_sex_right);
        mBtnLogout = (Button) findViewById(R.id.btn_logout);
        mTvUpdatePassword = (TextView) findViewById(R.id.tv_update_password);
    }

    @Override
    public void initData() {
        mHandler = new StaticHandler(this);
        mBtnLogout.setOnClickListener(this);
        User currentUser = BmobUser.getCurrentUser(getApplicationContext(), User.class);
        if (currentUser != null) {
            mTvNickRight.setText(TextUtils.isEmpty(currentUser.getNick()) ? "请设置" : currentUser.getNick() + "");
            mTvSexRight.setText(currentUser.getSex() == 0 ? "男" : "女");
        }
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

    }

    @Override
    public void PreOnDestroy() {
    }

    @Override
    public boolean PreOnKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout: {
                alertDialog.show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BmobUser.logOut(getApplicationContext());   //清除缓存用户对象
                        MApplication.getInstance().finishAllActivity();
                        startActivity(LoginActivity.class);
                    }
                }, Constants._TIME);
            }
            break;
        }
    }
}
