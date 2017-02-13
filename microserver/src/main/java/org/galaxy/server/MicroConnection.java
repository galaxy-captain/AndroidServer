package org.galaxy.server;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by OoO on 2017/1/21.
 * <p>
 * 客户端连接类
 */
public final class MicroConnection {

    private final MicroServer mServer;

    private final Socket mSocket;

    private final String name;

    private final String ip;

    private final int port;

    private boolean isConnect = false;

    private boolean isSending = false;

    /**
     * 监听在其他线程进行
     */
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

        this.name = socket.getInetAddress().getHostName() + ":" + this.port;

        this.mServer.onServerAccept(this);
        this.mServer.onConnectionAccept(this);

    }

    /**
     * 启动输入流监听、心跳守护线程
     */
    void run() {

        isConnect = true;

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
     * 客户端地址
     */
    public String getIp() {
        return ip;
    }

    /**
     * 客户端端口号
     */
    public int getPort() {
        return port;
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

            while (isConnected()) {

                int length = stream.read(buffer);

                if (length > 0) {
                    onReceive(buffer, length);
                }

            }

            SLog.error("client[" + getName() + "] end...");

        } catch (IOException e) {
            onConnectError(e);
        }

    }

    /**
     * 输入流监听回调
     */
    private void onReceive(byte[] buffer, int length) {

        String data = new String(buffer, 0, length);

        SLog.error("client[" + getName() + "] receive message on Port(" + mServer.getState().getPort() + ")-> " + data);

        if (mServer != null) mServer.onConnectionReceive(this, buffer, length);
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

            onSend(buffer);

        } catch (IOException e) {

            isSending = false;

            onConnectError(e);

            return false;
        }

        return true;
    }

    /**
     * 输出流回调
     */
    private void onSend(byte[] buffer) {
        if (mServer != null) mServer.onConnectionSend(this, buffer);
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
                            mSocket.sendUrgentData(0xFF);
                        }

                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            SLog.error("thread sleep error...");
                        }

                    }

                } catch (IOException e) {
                    onConnectError(e);
                }

            }

        }.start();

    }

    public boolean testConnection() {

        if (mSocket == null) return false;

        try {

            if (!isSending) {
                isSending = true;
                mSocket.sendUrgentData(0xFF);
                isSending = false;
            }

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * 连接异常时处理
     */
    private void onConnectError(IOException e) {
        close();
    }

    /**
     * 关闭连接
     */
    public boolean close() {

        if (!isConnect) return false;

        isConnect = false;

        try {

            mSocket.close();

        } catch (IOException ignored) {
            // ignored exception
            return false;
        }

        mServer.onConnectionClose(this);

        SLog.error("client[" + getName() + "] connection closed...");

        return true;
    }

    /**
     * 是否连接正常
     */
    private boolean isConnected() {
        return isConnect
                && mServer.getState() != null && mServer.getState().isRunning()
                && mSocket != null && mSocket.isConnected();
    }

}