package com.pony.ninjarpc.aweme.client;

import android.os.Handler;
import android.os.Message;

import com.pony.ninjarpc.aweme.common.Constant;
import com.pony.ninjarpc.aweme.utils.DeviceUtil;
import com.pony.ninjarpc.aweme.utils.VLog;

import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketController {

    private static WebSocketController webSocketController;
    private JWebSocketClient client;

    public static WebSocketController getInstance() {
        synchronized (WebSocketController.class) {
            if (webSocketController == null) {
                webSocketController = new WebSocketController();
            }
            return webSocketController;
        }
    }

    /**
     * 初始化websocket
     */
    public JWebSocketClient initWebSocket(Handler handler) {
        URI uri = URI.create("ws://" + Constant.URL + "/websocket/" + "aweme-"+DeviceUtil.serialNumber());
        //TODO 创建websocket
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                super.onMessage(message);
                Message msg = new Message();
                msg.what=2;
                msg.obj = message;
                handler.sendMessage(msg);
                VLog.e("websocket收到消息：" + message);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                VLog.e("websocket连接成功" +DeviceUtil.serialNumber());

            }

            @Override
            public void onError(Exception ex) {
                super.onError(ex);
                VLog.e("websocket连接错误：" + ex);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                super.onClose(code, reason, remote);

                if (code != 1000) {
                    reconnectWs();//意外断开马上重连
                }
                VLog.e("websocket断开连接：·code:" + code + "·reason:" + reason + "·remote:" + remote);
            }
        };
        //TODO 设置超时时间
        client.setConnectionLostTimeout(110 * 1000);
        //TODO 连接websocket
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        }.start();
        return client;
    }


    /**
     * 开启重连
     */
    public synchronized void reconnectWs() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (client.getReadyState() == ReadyState.NOT_YET_CONNECTED) {
                        if (client.isClosed()) {
                            client.reconnectBlocking();
                        } else {
                            client.connectBlocking();
                        }
                    } else if (client.getReadyState() == ReadyState.CLOSED) {
                        client.reconnectBlocking();
                    }
                } catch (Throwable e) {
                    //e.printStackTrace();
                }
            }
        }.start();
    }

    public void sendMsg(String msg) {
        if (null != client) {
            VLog.e("^_^Websocket发送的消息：-----------------------------------^_^" + msg);
            if (client.isOpen()) {
                client.send(msg);
            }

        }
    }

}
