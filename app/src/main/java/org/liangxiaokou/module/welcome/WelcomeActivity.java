package org.liangxiaokou.module.welcome;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import org.liangxiaokou.app.ToolBarActivity;
import org.liangxiaokou.module.R;
import org.liangxiaokou.module.home.HomeActivity;
import org.liangxiaokou.module.invite.InviteActivity;
import org.liangxiaokou.util.KeyBoardUtils;
import org.liangxiaokou.widget.view.KeyboardListenRelativeLayout;

import java.lang.ref.WeakReference;

public class WelcomeActivity extends ToolBarActivity implements IWelcomeView {

    private ScrollView mSv;
    private RadioGroup mRgSex;
    private RadioButton mRbMan;
    private RadioButton mRbWoman;
    private TextInputLayout mTextInputNick;
    private Button mBtnLogin;
    private KeyboardListenRelativeLayout mKlrl;

    private StaticHandler mHandler;

    private static class StaticHandler extends Handler {
        private WeakReference<Activity> reference;

        public StaticHandler(Activity activity) {
            this.reference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WelcomeActivity activity = (WelcomeActivity) reference.get();
            if (activity != null) {
                switch (msg.what) {
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE://软键盘隐藏
                        activity.mSv.fullScroll(View.FOCUS_UP);
                        break;
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW://软键盘显示
                        activity.mSv.fullScroll(View.FOCUS_DOWN);
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }

    private WelcomePresenter welcomePresenter = new WelcomePresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //showBack(true);
    }

    @Override
    public void initView() {
        mSv = (ScrollView) findViewById(R.id.sv);
        mRgSex = (RadioGroup) findViewById(R.id.rg_sex);
        mRbMan = (RadioButton) findViewById(R.id.rb_man);
        mRbWoman = (RadioButton) findViewById(R.id.rb_woman);
        mTextInputNick = (TextInputLayout) findViewById(R.id.textInput_nick);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mKlrl = (KeyboardListenRelativeLayout) findViewById(R.id.klrl);
    }

    @Override
    public void initData() {
        mHandler = new StaticHandler(this);
        mKlrl.setOnKeyboardStateChangedListener(new KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener() {
            @Override
            public void onKeyboardStateChanged(int state) {
                Message message = null;
                switch (state) {
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE://软键盘隐藏
                        message = new Message();
                        message.what = KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE;
                        mHandler.sendMessage(message);
                        break;
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW://软键盘显示
                        message = new Message();
                        message.what = KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW;
                        mHandler.sendMessage(message);
                        break;
                    default:
                        break;
                }
            }
        });
        mBtnLogin.setOnClickListener(this);
        //滚动监听
        mSv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //隐藏键盘
                    KeyBoardUtils.closeKeybord(mTextInputNick.getEditText(), getApplicationContext());
                }
                return false;
            }
        });
        //性别
        mRgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_man) {
                    welcomePresenter.setSex(0);
                } else if (checkedId == R.id.rb_woman) {
                    welcomePresenter.setSex(1);
                }
            }
        });
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
            case R.id.btn_login:
                welcomePresenter.toMain(this);
                break;
        }
    }

    @Override
    public String getNick() {
        return mTextInputNick.getEditText().getText().toString().trim();
    }

    @Override
    public void showLoading() {
        alertDialog.show();
    }

    @Override
    public void hideLoading() {
        alertDialog.hide();
    }

    @Override
    public void onSuccess() {
        startActivity(InviteActivity.class);
    }

    @Override
    public void onFailure(int code, String msg) {
        showToast("current code is " + code + " and msg is " + msg);
    }
}
