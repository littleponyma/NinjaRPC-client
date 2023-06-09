package com.pony.ninjarpc.aweme.controller;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pony.android.robust.Robust;
import com.pony.ninjarpc.aweme.client.JWebSocketClient;
import com.pony.ninjarpc.aweme.client.WebSocketController;
import com.pony.ninjarpc.aweme.hook.HookDY;
import com.pony.ninjarpc.aweme.utils.VLog;

import org.java_websocket.enums.ReadyState;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectorController {

    private static CollectorController collectorController;

    public static CollectorController getInstance() {
        synchronized (CollectorController.class) {
            if (collectorController == null) {
                collectorController = new CollectorController();
            }
            return collectorController;
        }
    }

    private JWebSocketClient socketClient;
    private WebSocketController webSocketController;
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (socketClient == null) {
                    webSocketController = WebSocketController.getInstance();
                    socketClient = webSocketController.initWebSocket(this);
                } else if (socketClient.getReadyState() == ReadyState.NOT_YET_CONNECTED ||
                        socketClient.getReadyState() == ReadyState.CLOSED) {
                    webSocketController.reconnectWs();//进入页面发现断开开启重连
                }
            } else {
                onHandleMessage((String) msg.obj);
            }
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(0, 60 * 1000);
        }
    };

    public void start() {
        if (webSocketController != null) {
            return;
        }
        mHandler.sendEmptyMessageDelayed(0, 5000);
    }

    private void onHandleMessage(String obj) {
        VLog.e(obj);
        JSONObject jsonObject = JSON.parseObject(obj);
        if (jsonObject.getString("method").equals("sign6")){
            sign6(jsonObject);
        }
    }


    private void sign6(JSONObject jsonObject){
        String method = jsonObject.getString("method");
        String uuid = jsonObject.getString("uuid");
        String did = jsonObject.getString("did");
        String iid = jsonObject.getString("iid");
        String url = jsonObject.getString("url");
        JSONObject header = jsonObject.getJSONObject("header");
        Robust.invokeMethod(HookDY.msManager, "setDeviceID", did);
        Robust.invokeMethod(HookDY.msManager, "setInstallID", iid);
        Class<?> NetworkParams = Robust.findClass("com.bytedance.frameworks.baselib.network.http.NetworkParams", HookDY.mLpparam.classLoader);
        HashMap<String, List<String>> map = new HashMap<>();
        for (String next : header.keySet()) {
            map.put(next, Collections.singletonList(header.getString(next)));
        }
        Map sign = (Map) Robust.invokeStaticMethod(NetworkParams,
                "tryAddSecurityFactor", url, map);
        sendData(JSON.toJSONString(sign), method, uuid);
    }

    public void sendData(String data, String tag, String uuid) {
        JSONObject jsonObject = JSON.parseObject("{}");
        jsonObject.put("method", tag);
        jsonObject.put("data", data);
        jsonObject.put("uuid", uuid);
        webSocketController.sendMsg(jsonObject.toString());
    }
}
