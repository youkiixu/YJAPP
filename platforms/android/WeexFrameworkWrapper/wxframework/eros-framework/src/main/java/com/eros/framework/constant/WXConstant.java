package com.eros.framework.constant;

/**
 * Created by Carry on 2017/8/7.
 */

public class WXConstant {

    /**
     * -------------调试参数-----------------------
     **/
    public static final int HOT_REFRESH_CONNECT = 0x111;
    public static final int HOT_REFRESH_DISCONNECT = HOT_REFRESH_CONNECT + 1;
    public static final int HOT_REFRESH_REFRESH = HOT_REFRESH_DISCONNECT + 1;
    public static final int HOT_REFRESH_CONNECT_ERROR = HOT_REFRESH_REFRESH + 1;

    /**
     * ----------router moudle回调js参数名--------
     **/
    public static final String OPEN_PAGE_SUCCESS = "打开页面成功";
    public static final String OPNE_PAGE_FAILED = "打开页面失败";

    public static final String BACK_PAGE_SUCCESS = "关闭页面成功";
    public static final String BACK_PAGE_FAILED = "关闭页面失败";

    public static final String ACTION_WEEX_REFRESH = "ACTION_WEEX_REFRESH";
    //拦截器状态发生变化
    public static final String ACTION_INTERCEPTOR_SWTICH = "ACTION_INTERCEPTOR_SWTICH";
    //初始化激活中介者
    public static final String MEDIATOR_INIT = "MEDIATOR_INIT";
    //销毁中介者
    public static final String MEDIATOR_DESTROY = "MEDIATOR_DESTROY";
    //网络状态变化
    public static final String ACTION_NETWORK_CHANGED = "ACTION_NETWORK_CHANGED";
    //更新包下载完成
    public static final String ACTION_BUNDLE_DOWNLOADED = "ACTION_BUNDLE_DOWNLOADED";
    //activity入栈广播
    public static final String ACTION_ACTIVITY_ATTACH = "ACTION_ACTIVITY_PUSH";

}
