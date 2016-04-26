package org.liangxiaokou.module.QRcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.android.volley.VolleyLog;
import com.google.zxing.Result;

import org.liangxiaokou.app.ToolBarActivity;
import org.liangxiaokou.bean.Friend;
import org.liangxiaokou.bmob.BmobNetUtils;
import org.liangxiaokou.config.Constants;
import org.liangxiaokou.module.R;
import org.liangxiaokou.module.home.HomeActivity;
import org.liangxiaokou.widget.activity.BaseWebActivity;
import org.liangxiaokou.widget.dialog.listener.OnBtnClickL;

import java.util.List;

import cn.bmob.v3.listener.FindListener;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends ToolBarActivity implements ZXingScannerView.ResultHandler, IScannerView {

    private ZXingScannerView mZXingScannerView;

    private ScannerPresenter scannerPresenter = new ScannerPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        showBack(true);
    }

    @Override
    public void initView() {
        mZXingScannerView = (ZXingScannerView) findViewById(R.id.zXingScannerView);

    }

    @Override
    public void initData() {

    }

    @Override
    public void PreOnStart() {

    }

    @Override
    public void PreOnResume() {
        mZXingScannerView.setResultHandler(this);
        mZXingScannerView.startCamera();
    }

    @Override
    public void PreOnRestart() {

    }

    @Override
    public void PreOnPause() {
        mZXingScannerView.stopCamera();
    }

    @Override
    public void PreOnStop() {

    }

    @Override
    public void PreOnDestroy() {
        mZXingScannerView.stopCamera();
    }

    @Override
    public boolean PreOnKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void handleResult(Result result) {
        //检查result.getText是否app所设定的数据
        String key = Constants.author + "&" + Constants.APP_NAME + "&";
        if (result.getText().contains(key)) {
            //判断是否含有 小俩口签名
            String[] id = result.getText().split("&");
            //实现添加好友
            scannerPresenter.toSaveFriend(ScannerActivity.this, id[2]);
            //mZXingScannerView.resumeCameraPreview(ScannerActivity.this);
        } else if (result.getText().startsWith("http://") || result.getText().startsWith("https://")) {
            //如果是网站，就跳转到对应的网页中去
            mZXingScannerView.resumeCameraPreview(ScannerActivity.this);
            Intent intent = new Intent(this, BaseWebActivity.class);
            intent.putExtra("BaseWebActivity_url", result.getText());
            startActivity(intent);
        } else {
            materialDialog.content(result.getText());
            materialDialog.show();
            materialDialog.setOnBtnClickL(new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    mZXingScannerView.resumeCameraPreview(ScannerActivity.this);
                    materialDialog.hide();
                }
            }, new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    mZXingScannerView.resumeCameraPreview(ScannerActivity.this);
                    materialDialog.hide();
                }
            });
        }
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
        startActivity(HomeActivity.class);
    }

    @Override
    public void onFailure(int code, String msg) {
        if (code == -1) {
            //-1表示 已经存在好友
            startActivity(HomeActivity.class);
        }
    }
}