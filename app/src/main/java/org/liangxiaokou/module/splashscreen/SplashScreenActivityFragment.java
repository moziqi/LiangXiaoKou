package org.liangxiaokou.module.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yalantis.starwars.interfaces.TilesFrameLayoutListener;

import org.liangxiaokou.module.home.HomeActivity;
import org.liangxiaokou.module.R;
import org.liangxiaokou.app.GeneralFragment;
import org.liangxiaokou.module.login.LoginActivity;

import java.lang.ref.WeakReference;

import cn.bmob.v3.BmobUser;

/**
 * A placeholder fragment containing a simple view.
 */
public class SplashScreenActivityFragment extends GeneralFragment implements TilesFrameLayoutListener {

    private StaticHandler handler;

    private static class StaticHandler extends Handler {
        private final WeakReference<Activity> weakReference;

        public StaticHandler(Activity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private static class SplashHandler implements Runnable {
        private final WeakReference<Activity> weakReference;

        public SplashHandler(Activity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        public void run() {
            Activity activity = weakReference.get();
            if (activity != null) {
                activity.startActivity(new Intent(activity, LoginActivity.class));
                activity.finish();
            }
        }
    }

    public SplashScreenActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        handler = new StaticHandler(getActivity());
        handler.postDelayed(new SplashHandler(getActivity()), 3000);
    }

    @Override
    protected void PreOnStart() {

    }

    @Override
    protected void PreOnResume() {
    }

    @Override
    protected void PreOnPause() {
    }

    @Override
    protected void PreOnStop() {

    }

    @Override
    protected void PreOnDestroy() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onAnimationFinished() {
    }

}
