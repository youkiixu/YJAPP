package com.eros.framework.extend.module;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.weex.plugin.annotation.WeexModule;
import com.eros.framework.BMWXEnvironment;
import com.eros.framework.activity.MainActivity;
import com.eros.framework.constant.WXEventCenter;
import com.eros.framework.manager.ManagerFactory;
import com.eros.framework.manager.impl.dispatcher.DispatchEventManager;
import com.eros.framework.model.WeexEventBean;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

/**
 * Created by liuyuanxiao on 2018/6/21.
 */
@WeexModule(name = "bmTabbar", lazyLoad = true)
public class TabBarModule extends WXModule {

    @JSMethod(uiThread = true)
    public void showBadge(String params) {
        WeexEventBean weexEventBean = new WeexEventBean();
        weexEventBean.setKey(WXEventCenter.EVENT_TABBAR_SHOWBADGE);
        weexEventBean.setJsParams(params);
        weexEventBean.setContext(mWXSDKInstance.getContext());
        ManagerFactory.getManagerService(DispatchEventManager.class).getBus().post(weexEventBean);
    }

    @JSMethod(uiThread = true)
    public void hideBadge(String params) {
        WeexEventBean weexEventBean = new WeexEventBean();
        weexEventBean.setKey(WXEventCenter.EVENT_TABBAR_HIDBADGE);
        weexEventBean.setJsParams(params);
        weexEventBean.setContext(mWXSDKInstance.getContext());
        ManagerFactory.getManagerService(DispatchEventManager.class).getBus().post(weexEventBean);
    }

    @JSMethod(uiThread = true)
    public void openPage(String params) {
        WeexEventBean weexEventBean = new WeexEventBean();
        weexEventBean.setJsParams(params);
        weexEventBean.setKey(WXEventCenter.EVENT_TABBAR_OPENPAGE);
        weexEventBean.setContext(mWXSDKInstance.getContext());
        ManagerFactory.getManagerService(DispatchEventManager.class).getBus().post(weexEventBean);
    }

    @JSMethod(uiThread = false)
    public Object getIndex() {
        if (mWXSDKInstance.getContext() instanceof MainActivity) {
            return ((MainActivity) mWXSDKInstance.getContext()).getPageIndex();
        }
        return -1;
    }

    @JSMethod(uiThread = false)
    public Object getInfo() {
        return JSON.toJSON(BMWXEnvironment.mPlatformConfig.getTabBar());
    }


    @JSMethod(uiThread = true)
    public void setInfo(String params) {
        WeexEventBean weexEventBean = new WeexEventBean();
        weexEventBean.setJsParams(params);
        weexEventBean.setKey(WXEventCenter.EVENT_TABBAR_SETTABBAR);
        weexEventBean.setContext(mWXSDKInstance.getContext());
        ManagerFactory.getManagerService(DispatchEventManager.class).getBus().post(weexEventBean);
    }

    @JSMethod(uiThread = false)
    public void watchIndex(JSCallback callback) {
        WeexEventBean weexEventBean = new WeexEventBean();
        weexEventBean.setKey(WXEventCenter.EVENT_TABBAR_WATCHINDEX);
        weexEventBean.setContext(mWXSDKInstance.getContext());
        weexEventBean.setExpand(mWXSDKInstance.hashCode());
        weexEventBean.setJscallback(callback);
        ManagerFactory.getManagerService(DispatchEventManager.class).getBus().post(weexEventBean);
    }

    @JSMethod(uiThread = true)
    public void clearWatch(JSCallback callback) {
        WeexEventBean weexEventBean = new WeexEventBean();
        weexEventBean.setKey(WXEventCenter.EVENT_TABBAR_CLEARWATCH);
        weexEventBean.setContext(mWXSDKInstance.getContext());
        weexEventBean.setExpand(mWXSDKInstance.hashCode());
        weexEventBean.setJscallback(callback);
        ManagerFactory.getManagerService(DispatchEventManager.class).getBus().post(weexEventBean);
    }

    @JSMethod(uiThread = true)
    public void clearInfo() {
        WeexEventBean weexEventBean = new WeexEventBean();
        weexEventBean.setKey(WXEventCenter.EVENT_TABBAR_CLEARTABBARINFO);
        weexEventBean.setContext(mWXSDKInstance.getContext());
        ManagerFactory.getManagerService(DispatchEventManager.class).getBus().post(weexEventBean);
    }
}
