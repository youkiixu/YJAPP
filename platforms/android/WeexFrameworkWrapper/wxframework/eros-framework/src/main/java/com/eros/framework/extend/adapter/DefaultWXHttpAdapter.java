package com.eros.framework.extend.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.eros.framework.BMWXEnvironment;
import com.eros.framework.activity.AbstractWeexActivity;
import com.eros.framework.adapter.router.RouterTracker;
import com.eros.framework.constant.Constant;
import com.eros.framework.extend.hook.TypeFaceHandler;
import com.eros.framework.manager.ManagerFactory;
import com.eros.framework.manager.impl.AxiosManager;
import com.eros.framework.manager.impl.FileManager;
import com.eros.framework.manager.impl.ModalManager;
import com.eros.framework.manager.impl.PersistentManager;
import com.eros.framework.manager.impl.VersionManager;
import com.eros.framework.model.Md5MapperModel;
import com.eros.framework.utils.BaseJsInjector;
import com.eros.framework.utils.DebugableUtil;
import com.eros.framework.utils.L;
import com.eros.framework.utils.Md5Util;
import com.eros.framework.utils.SharePreferenceUtil;
import com.eros.framework.utils.TextUtil;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.HttpMethod;

/**
 * Created by Carry on 2017/8/7. interceptor for js resource
 */

public class DefaultWXHttpAdapter implements IWXHttpAdapter {
    private ExecutorService mExecutorService;
    private Context mContext;
    private String[] mFileFilter = {".js", ".css", ".html"};
    private String[] mIconFontFilter = {".ttf", ".woff"};
    private BaseJsInjector mInjector;
    private OkHttpClient client;

    private void execute(Runnable runnable) {
        if (mExecutorService == null) {
            mExecutorService = Executors.newFixedThreadPool(3);
        }
        mExecutorService.execute(runnable);
    }


    public DefaultWXHttpAdapter(Context context) {
        this.mContext = context;
        mInjector = BaseJsInjector.getInstance();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (DebugableUtil.isDebug()) {
            builder.addNetworkInterceptor(new WeexOkhttp3Interceptor());
        }
        client = builder.build();
    }


    public OkHttpClient getHttpClient() {
        return client;
    }

    @Override
    public void sendRequest(final WXRequest request, final OnHttpListener listener) {
        if (listener != null) {
            listener.onHttpStart();
        }

        Log.e("DefaultWXHttpAdapter", "url>>>>>>" + request.url);

        if (isIconFontRes(request.url)) {
            TypeFaceHandler.load(request.url, listener);
            return;
        }

        if (!(Constant.INTERCEPTOR_ACTIVE.equals(SharePreferenceUtil.getInterceptorActive
                (mContext))) || !isInterceptor(request.url)) {
            fetchUrl(request, listener);
        } else {
            execute(new Runnable() {
                @Override
                public void run() {
                    doInterceptor(request, listener);
                }
            });
        }

    }


    private boolean isInterceptor(String url) {
        return url.endsWith(".js");
    }

    private boolean isIconFontRes(String url) {
        for (String iconFilter : mIconFontFilter) {
            if (url.endsWith(iconFilter)) return true;
        }
        return false;
    }

    private void doInterceptor(WXRequest request, OnHttpListener listener) {
        WXResponse response = new WXResponse();
        String url = request.url;
        if (TextUtils.isEmpty(url)) {
            if (listener != null) {
                response.statusCode = "-1";
                response.errorCode = "-1";
                response.errorMsg = "路径不能为空";
                listener.onHttpFinish(response);
            }
            return;
        }
        String subPath = url.substring(url.indexOf("/dist/js") + 9);
        File bundleDir = ManagerFactory.getManagerService(FileManager.class).getBundleDir(mContext);
        File path = new File(bundleDir, "bundle/" + subPath);
        Log.e("bus", "bus>>>>>>>" + path.getAbsolutePath());
        if (listener != null) {
            listener.onHttpStart();
        }
        if (path.exists()) {
            //比较md5
            String targetMd5 = findMd5(path.getAbsolutePath());
            String currentMd5 = Md5Util.getFileMD5(path);
            if (currentMd5 == null) {
                //纪录错误   md5映射中找不到该路径
                if (listener != null) {
                    response.statusCode = "-1";
                    response.errorCode = "-1";
                    response.errorMsg = "映射中找不到:" + path.getAbsolutePath();
                    listener.onHttpFinish(response);
                }
                showError("不存在md5映射");
                return;
            }
            if (!targetMd5.equals(currentMd5)) {
                //纪录错误  得到的md5与映射中md5不一致
                if (listener != null) {
                    response.statusCode = "-1";
                    response.errorCode = "-1";
                    response.errorMsg = "文件不匹配" + path.getAbsolutePath();
                    listener.onHttpFinish(response);
                }
                showError("当前md5和config中的md5不一致");
                return;
            }
            //文件正确  加载本地js
            byte[] bytes = ManagerFactory.getManagerService(FileManager.class).loadLocalFile(path
                    .getAbsolutePath());
            if (listener != null) {
                response.statusCode = 200 + "";
                if (isInterceptor(request.url)) {
                    response.originalData = bytes;
                    appendBaseJs(response, listener);
                }
            }
            hideError();
        } else {
            if (listener != null) {
                response.statusCode = "-1";
                response.errorCode = "-1";
                response.errorMsg = "文件不存在" + path.getAbsolutePath();
                listener.onHttpFinish(response);
            }
            showError("文件" + path.getAbsolutePath() + "不存在");
        }

    }

    private void hideError() {
        if (!DebugableUtil.isDebug()) return;
        Activity activity = RouterTracker.peekActivity();
        if (activity != null && activity instanceof AbstractWeexActivity) {
            AbstractWeexActivity abs = (AbstractWeexActivity) activity;
            abs.hideError();
        }
    }


    private void showError(final String message) {
        if (!DebugableUtil.isDebug()) return;
        final Activity activity = RouterTracker.peekActivity();
        if (activity != null && activity instanceof AbstractWeexActivity) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AbstractWeexActivity abs = (AbstractWeexActivity) activity;
                    abs.showError();
                    ModalManager.BmToast.toast(mContext, message,
                            Toast.LENGTH_SHORT);
                }
            });
        }

    }


    private String findMd5(String path) {
        List<Md5MapperModel.Item> lists = ManagerFactory.getManagerService(PersistentManager
                .class).getFileMapper();
        if (lists == null) {
            VersionManager versionManager = ManagerFactory.getManagerService(VersionManager.class);
            versionManager.initMapper(BMWXEnvironment.mApplicationContext);
            lists = ManagerFactory.getManagerService(PersistentManager
                    .class).getFileMapper();
        }
        for (Md5MapperModel.Item item : lists) {
            if (path.endsWith(item.getPage())) {
                return item.getMd5();
            }
        }
        return "";
    }

    private void appendBaseJs(final WXResponse response, final OnHttpListener
            listener) {
        mInjector.injectBaseJs(mContext, response, new BaseJsInjector.InjectJsListener() {
            @Override
            public void onInjectStart(String origin) {

            }

            @Override
            public void onInjectFinish(WXResponse response) {
                L.e("DefaultWXHttpAdapter", "注入成功");
                if (listener != null) {
                    listener.onHttpFinish(response);
                }
            }

            @Override
            public void onInjectError() {
                Log.e("DefaultWXHttpAdapter", "baseJs注入失败");
                if (listener != null) {
                    listener.onHttpFinish(response);
                }
            }
        });
    }


    private void fetchUrl(final WXRequest request, final OnHttpListener listener) {
        AxiosManager axiosManager = ManagerFactory.getManagerService(AxiosManager.class);
        axiosManager.loadJSBundle(request, new JSBundleCallback() {
            @Override
            public void onFailure(WXResponse wxResponse) {
                if (listener != null) {
                    listener.onHttpFinish(wxResponse);
                }
            }

            @Override
            public void onResponse(WXResponse wxResponse) {
                if (isInterceptor(request.url)) {
                    if (listener != null) {
                        listener.onHttpFinish(wxResponse);
                    }
                }
            }
        });
    }

    public static interface JSBundleCallback {
        void onFailure(WXResponse wxResponse);

        void onResponse(WXResponse wxResponse);
    }

}
