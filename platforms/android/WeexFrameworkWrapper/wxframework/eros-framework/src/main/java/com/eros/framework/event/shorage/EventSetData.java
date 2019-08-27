package com.eros.framework.event.shorage;

import android.content.Context;

import com.eros.framework.manager.ManagerFactory;
import com.eros.framework.manager.StorageManager;
import com.eros.framework.model.WeexEventBean;
import com.eros.framework.utils.JsPoster;
import com.eros.wxbase.EventGate;
import com.taobao.weex.bridge.JSCallback;

import java.util.ArrayList;

/**
 * Created by Carry on 2017/5/21.
 */

public class EventSetData extends EventGate {

    @Override
    public void perform(Context context, WeexEventBean weexEventBean) {
        setData(context, weexEventBean.getParamsList(), weexEventBean
                .getJscallback());
    }

    public void setData(Context context, ArrayList<String> paramsList, JSCallback jscallback) {
        StorageManager storageManager = ManagerFactory.getManagerService(StorageManager.class);
        String key = paramsList.get(0);
        String value = paramsList.get(1);
        boolean result = storageManager.setData(context, key, value);
        if (result) {
            JsPoster.postSuccess(null, jscallback);
        } else {
            JsPoster.postFailed("存储失败", jscallback);
        }
    }


    public Object setDataSync(Context context, ArrayList<String> paramsList) {
        StorageManager storageManager = ManagerFactory.getManagerService(StorageManager.class);
        String key = paramsList.get(0);
        String value = paramsList.get(1);
        boolean result = storageManager.setData(context, key, value);
        return result ? JsPoster.getSuccess() : JsPoster.getFailed("存储失败");
    }
}
