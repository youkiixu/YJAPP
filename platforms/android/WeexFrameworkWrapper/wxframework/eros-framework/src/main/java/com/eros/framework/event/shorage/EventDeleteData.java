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

public class EventDeleteData extends EventGate{

    @Override
    public void perform(Context context, WeexEventBean weexEventBean) {
        deleteData(context, weexEventBean.getParamsList(),
                weexEventBean.getJscallback());
    }

    public void deleteData(Context context, ArrayList<String> paramsList, JSCallback jscallback) {
        String key = paramsList.get(0);
        StorageManager storageManager = ManagerFactory.getManagerService(StorageManager.class);
        boolean result = storageManager.deleteData(context, key);
        if (result) {
            JsPoster.postSuccess(null, jscallback);
        } else {
            JsPoster.postFailed("删除" + key + "失败", jscallback);
        }
    }

    public Object deleteDataSync(Context context, ArrayList<String> paramsList) {
        String key = paramsList.get(0);
        StorageManager storageManager = ManagerFactory.getManagerService(StorageManager.class);
        boolean result = storageManager.deleteData(context, key);
        return result ? JsPoster.getSuccess() : JsPoster.getFailed("删除" + key + "失败");
    }
}
