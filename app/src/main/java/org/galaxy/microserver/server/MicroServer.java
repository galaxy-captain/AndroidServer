package org.galaxy.microserver.server;

import org.galaxy.microserver.server.callback.ConnectionListener;
import org.galaxy.microserver.utils.L;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Created by OoO on 2017/1/21.
 * <p>
 * <p>
 * 微服务器基础类
 */
public class MicroServer {

    private ServerSocket mServer;

    private ServerConfig mConfig;

    private ServerState mState;

    /**
     * -----------------------------------------------------------------------------------------------
     **/

    private ConnectionListener mListener;

    public void setConnectionListener(ConnectionListener listener) {
        this.mListener = listener;
    }

    public void removeConnectionListener(ConnectionListener listener) {
        mListener = null;
    }

    /**
     * 回调 --- 新的连接
     */
    void onConnectionAccept(MicroConnection connection) {

        addConnection(connection);

        if (mListener != null) mListener.onAccept(connection);
    }

    /**
     * 回调 --- 连接异常
     */
    void onConnectionClose(MicroConnection connection) {

        removeConnection(connection);

        if (mListener != null) mListener.onClose(connection);
    }

    /**
     * 回调 --- 连接发送消息
     */
    void onConnectionSend(MicroConnection connection, byte[] buffer) {
        if (mListener != null) mListener.onSend(connection, buffer);
    }

    /**
     * 回调 --- 连接接收消息
     */
    void onConnectionReceive(MicroConnection connection, byte[] buffer, int length) {
        if (mListener != null) mListener.onReceive(connection, buffer, length);
    }

    /** ----------------------------------------------------------------------------------------------- **/

    /**
     * 等待客户端连接的线程
     */
    private final Thread waitForConnectTread = new Thread() {

        @Override
        public void run() {

            // 服务器运行时，监听并建立连接
            while (mState != null && mState.isRunning() && mServer != null) {

                // 连接数不能大于设定的最大值
                if (mState.getConnectionTotal() < mConfig.getConnectionMaximum()) {
                    // 等待连接
                    serverAccept();
                }

            }

            // 服务器已经关闭
            // L.error("WaitThread stopped that server has been closed...");

        }

    };


    public MicroServer() {
        this(new ServerConfig());
    }

    public MicroServer(ServerConfig config) {
        this.mConfig = config;
    }

    /**
     * 初始化服务器
     */
    public boolean initServer() {

        try {

            L.error("Server initialize starting...");

            // 初始化ServerSocket服务
            mServer = new ServerSocket(mConfig.getPort());

            // 初始化服务器信息
            mState = new ServerState(MicroServer.this);

            // 设置服务器状态
            mState.setRunning(true);

            L.error("Server initialize success...");

        } catch (IOException e) {

            onError(e);

            L.error("Server initialize failed...");

            return false;
        }

        return true;

    }

    /**
     * 启动服务器
     */
    public void startServer() {
        waitForConnectAtThread();
    }

    /**
     * 打开等待客户端连接的线程
     */
    public void waitForConnectAtThread() {
        waitForConnectTread.start();
    }

    /**
     * 等待客户端连接
     */
    private void serverAccept() {

        try {

            L.error("Waiting for client connect...");

            // 等待客户端连接
            Socket socket = mServer.accept();

            // 判断连接是否正常
            if (socket != null && socket.isConnected()) {

                socket.setKeepAlive(true);
                socket.setOOBInline(false);

                // 客户端 - 名字
                String name = socket.getInetAddress().getHostName();
                // 客户端 - IP地址
                String ip = socket.getInetAddress().getHostAddress();
                // 客户端 - 端口号
                int port = socket.getPort();

                // 保存客户端连接
                new MicroConnection(MicroServer.this, socket).run();

                L.error("new client[" + name + "(" + ip + ":" + port + ")" + "] connect success...");

            } else {
                L.error("new client error...");
            }

        } catch (IOException e) {

            onError(e);

            L.error("Wait for connect stopped that Server has been closed...");
        }

    }

    /**
     * 关闭服务器
     */
    public void closeServer() {

        L.error("Server is closing...");

        try {

            if (mServer != null) {
                mServer.close();
            }

        } catch (IOException e) {
            onError(e);
        }

        mState.setRunning(false);
        mState.closeAllConnection();

        mServer = null;
        mState = null;
        mConfig = null;

        L.error("Server has closed...");

    }

    private void onError(IOException e) {

    }

    /**
     * 增加一个客户端连接
     */
    public void addConnection(MicroConnection connection) {
        if (mState != null) mState.addConnection(connection);
    }

    /**
     * 关闭一个客户端连接
     */
    public void removeConnection(MicroConnection connection) {
        if (mState != null) mState.cutConnection(connection);
    }

    /**
     * 获取一个客户端连接
     */
    public MicroConnection getConnection(String name) {
        return mState == null ? null : mState.getConnection(name);
    }

    /**
     * 获取全部客户端连接
     */
    public Map<String, MicroConnection> getAllConnection() {
        return mState == null ? null : mState.getAllConnection();
    }

    /**
     * 获取用户列表
     */
    public List<String> getAllConnectionName() {
        return mState == null ? null : mState.getAllConnectionName();
    }

    /**
     * 获取服务器状态
     */
    public ServerState getState() {
        return mState == null ? null : mState;
    }

    /**
     * 获取服务器配置
     */
    public ServerConfig getConfig() {
        return mConfig == null ? null : mConfig;
    }

}
