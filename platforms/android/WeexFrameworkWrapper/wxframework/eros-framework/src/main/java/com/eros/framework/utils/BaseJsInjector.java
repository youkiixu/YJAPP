package com.eros.framework.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.eros.framework.BMWXEnvironment;
import com.eros.framework.constant.Constant;
import com.eros.framework.constant.WXConstant;
import com.eros.framework.http.okhttp.OkHttpUtils;
import com.eros.framework.http.okhttp.callback.StringCallback;
import com.eros.framework.manager.ManagerFactory;
import com.eros.framework.manager.impl.FileManager;
import com.eros.framework.manager.impl.dispatcher.DispatchEventManager;
import com.squareup.otto.Subscribe;
import com.taobao.weex.common.WXResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import okhttp3.Call;

/**
 * Created by Carry on 2017/10/25.
 */

public class BaseJsInjector {
    private static BaseJsInjector mInstance = new BaseJsInjector();
    private String mBaseJs;
    private static final String REMOTE_URL = BMWXEnvironment.mPlatformConfig.getUrl().getJsServer
            () + "/dist/js" + BMWXEnvironment.mPlatformConfig.getAppBoard();

    public static BaseJsInjector getInstance() {
        return mInstance;
    }

    private BaseJsInjector() {
        DispatchEventManager managerService = ManagerFactory.getManagerService
                (DispatchEventManager.class);
        managerService.getBus().register(this);
    }

    public void injectBaseJs(Context context, final WXResponse origin, final InjectJsListener
            listener) {

        if (Constant.INTERCEPTOR_ACTIVE.equals(SharePreferenceUtil.getInterceptorActive(context))) {
            if (TextUtils.isEmpty(mBaseJs)) {
                loadFromAssets(context);
            }
            //注入basejs
            if (TextUtils.isEmpty(mBaseJs)) {
                if (listener != null) {
                    listener.onInjectError();
                }
                return;
            }
            insert(origin, listener);
        } else {
            //去本地服务下载后注入
            if (listener != null) {
                listener.onInjectFinish(origin);
            }
        }
    }


    private void insert(WXResponse origin, InjectJsListener listener) {
        origin.originalData = (mBaseJs + "\n" + new String(origin.originalData)).getBytes();
        //注入完成 回调页面
        if (listener != null) {

            listener.onInjectFinish(origin);
        }
    }

    private void loadFromAssets(Context context) {
        File baseJs = new File(FileManager.getBundleDir(context), "bundle" + BMWXEnvironment
                .mPlatformConfig.getAppBoard());
        if (baseJs.exists()) {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(baseJs);
                byte[] bytes = IOUtil.readInputStream(inputStream);
                if (bytes != null) {
                    mBaseJs = new String(bytes, "UTF-8");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void onRefresh(Intent intent) {
        if (intent != null && WXConstant.ACTION_WEEX_REFRESH.equals(intent.getAction())) {
            //页面刷新了 如果这时候拦截器关闭将mbaseJs=null
            if (!(Constant.INTERCEPTOR_ACTIVE.equals(SharePreferenceUtil.getInterceptorActive
                    (BMWXEnvironment.mApplicationContext)))) {
                this.mBaseJs = null;
            }
        } else if (intent != null && WXConstant.MEDIATOR_DESTROY.equals(intent.getAction())) {
            //更新完成 重制资源
            mBaseJs = null;
        }
    }


    public interface InjectJsListener {
        void onInjectStart(String origin);

        void onInjectFinish(WXResponse response);

        void onInjectError();
    }
}
