package org.galaxy.microserver.server;

import org.galaxy.microserver.utils.L;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by OoO on 2017/1/21.
 * <p>
 * 客户端连接类
 */
final class MicroConnection {

    private final MicroServer mServer;

    private final Socket mSocket;

    private final String name;

    private final String ip;

    private final int port;

    private boolean isConnect = false;

    private boolean isSending = false;

    private final Thread listenStreamThread = new Thread() {

        @Override
        public void run() {
            listenStream();
        }

    };

    MicroConnection(MicroServer server, Socket socket) {

        this.mServer = server;
        this.mSocket = socket;

        this.ip = socket.getInetAddress().getHostAddress();
        this.port = socket.getPort();

        this.name = ip + ":" + port;

        this.mServer.addConnection(this);

    }

    /**
     * 启动输入流监听、心跳守护线程
     */
    void run() {
        listenStreamAtThread();
        loopLiveTestAtThread();
    }

    /**
     * 客户端连接名称
     */
    public String getName() {
        return name;
    }

    /**
     * 启动输入流监听线程
     */
    private void listenStreamAtThread() {
        listenStreamThread.start();
    }

    /**
     * 输入流监听
     */
    private void listenStream() {

        try {

            InputStream stream = mSocket.getInputStream();

            byte[] buffer = new byte[256];

            isConnect = true;

            while (isConnected()) {

                int length = stream.read(buffer);

                if (length > 0) {
                    onReceive(buffer, length);
                }

            }

            L.error("client[" + name + "] connection closed...");

        } catch (IOException e) {
            onConnectError(e);
        }

    }

    /**
     * 输入流监听回调
     */
    private void onReceive(byte[] buffer, int length) {

        if (mServer != null)
            mServer.onConnectionReceive(name, buffer, length);
    }

    /**
     * 向客户端发送数据
     */
    public boolean sendMessageAtMain(String data) {

        try {

            if (!isConnected()) return false;

            isSending = true;

            OutputStream stream = mSocket.getOutputStream();

            byte[] buffer = data.getBytes();

            stream.write(buffer);

            stream.flush();

            isSending = false;

        } catch (IOException e) {

            isSending = false;

            onConnectError(e);

            return false;
        }

        return true;
    }

    /**
     * 启动心跳守护线程
     */
    private void loopLiveTestAtThread() {

        new Thread() {

            @Override
            public void run() {

                try {

                    while (isConnected()) {


                        if (!isSending) {
                            isSending = true;
                            mSocket.sendUrgentData(0xFF);
                            isSending = false;
                        }

                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            L.error("thread sleep error...");
                        }

                    }

                } catch (IOException e) {

                    isSending = false;

                    onConnectError(e);
                }

            }

        }.start();

    }

    /**
     * 连接异常时处理
     */
    private void onConnectError(IOException e) {

        e.printStackTrace();

        L.error("client[" + name + "] connection error...");

        isConnect = false;

        mServer.removeConnection(this);
    }

    /**
     * 是否连接正常
     */
    private boolean isConnected() {
        return mServer.getState() != null && mServer.getState().isRunning()
                && mSocket != null && mSocket.isConnected()
                && isConnect;
    }

}
