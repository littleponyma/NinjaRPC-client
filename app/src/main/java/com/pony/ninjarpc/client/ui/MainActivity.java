package com.pony.ninjarpc.client.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pony.ninjarpc.client.R;
import com.pony.ninjarpc.client.socket.JWebSocketClient;
import com.pony.ninjarpc.client.socket.WebSocketController;
import com.pony.ninjarpc.client.utils.VLog;

import org.java_websocket.enums.ReadyState;

import de.robv.android.xposed.XposedHelpers;

public class MainActivity extends AppCompatActivity {

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
            }else {
                onHandleMessage((String)msg.obj);
            }
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(0, 60 * 1000);
        }
    };


    public void onHandleMessage(String msg) {
        VLog.e("onHandleMessage "+msg);
        JSONObject jsonObject = JSON.parseObject(msg);
        JSONObject data = jsonObject.getJSONObject("data");
        String className = data.getString("className");
        String method = data.getString("method");
        JSONArray params = data.getJSONArray("params");
        StringBuilder paramBuilder=new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            paramBuilder.append(params.get(i));
            if (params.size()!=i){
                paramBuilder.append(",");
            }
        }
        try {
            Class<?> aClass = Class.forName(className);
            XposedHelpers.callStaticMethod(aClass,method,paramBuilder.toString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler.sendEmptyMessageDelayed(0,0);
    }
}