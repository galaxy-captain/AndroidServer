package org.galaxy.server;


import org.galaxy.server.listener.ConnectionListener;
import org.galaxy.server.listener.ServerListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Created by OoO on 2017/1/21.
 * <p>
 * 微服务器基础类
 */
public final class MicroServer {

    /**
     * 默认服务器配置
     */
    private static final ServerConfig DEFAULT_CONFIG = new ServerConfig();

    /**
     * 服务器实例
     */
    private ServerSocket mServer;

    /**
     * 服务器配置
     */
    private ServerConfig mConfig;

    /**
     * 服务器状态
     */
    private ServerState mState;

    /**
     * -------------------------------------------------------------------------------------
     * 服务器回调
     * -------------------------------------------------------------------------------------
     **/

    private ServerListener mServerListener;

    public void setServerListener(ServerListener serverListener) {
        this.mServerListener = serverListener;
    }

    public void removeServerListener(ServerListener serverListener) {
        this.mServerListener = null;
    }

    void onServerCreate() {
        if (mServerListener != null) mServerListener.onCreate(getConfig(), getState());
    }

    void onServerStart() {
        if (mServerListener != null) mServerListener.onStart(getConfig(), getState());
    }

    void onServerClose() {
        if (mServerListener != null) mServerListener.onClose(getConfig(), getState());
    }

    void onServerAccept(MicroConnection connection) {
        if (mServerListener != null) mServerListener.onAccept(connection);
    }

    // --------------------------------------------------------------------------------------------
    private ConnectionListener mConnectionListener;

    public void setConnectionListener(ConnectionListener listener) {
        this.mConnectionListener = listener;
    }

    public void removeConnectionListener() {
        mConnectionListener = null;
    }

    /**
     * 回调 --- 新的连接
     */
    void onConnectionAccept(MicroConnection connection) {

        addConnection(connection);

        if (mConnectionListener != null) mConnectionListener.onAccept(connection);
    }

    /**
     * 回调 --- 连接异常
     */
    void onConnectionClose(MicroConnection connection) {

        removeConnection(connection);

        if (mConnectionListener != null) mConnectionListener.onClose(connection);
    }

    /**
     * 回调 --- 连接发送消息
     */
    void onConnectionSend(MicroConnection connection, byte[] buffer) {
        if (mConnectionListener != null) mConnectionListener.onSend(connection, buffer);
    }

    /**
     * 回调 --- 连接接收消息
     */
    void onConnectionReceive(MicroConnection connection, byte[] buffer, int length) {
        if (mConnectionListener != null) mConnectionListener.onReceive(connection, buffer, length);
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
            // SLog.error("WaitThread stopped that server has been closed...");

        }

    };


    public MicroServer() {
        this(DEFAULT_CONFIG);
    }

    public MicroServer(ServerConfig config) {
        this.mConfig = config;
    }

    /**
     * 初始化服务器
     */
    public boolean initServer() {

        try {

            SLog.error("Server initialize starting...");

            // 初始化ServerSocket服务
            mServer = new ServerSocket(mConfig.getPort());

            // 初始化服务器信息
            mState = new ServerState(MicroServer.this);

            // 设置服务器状态
            mState.setRunning(true);

            SLog.error("Server initialize success...");

            // 服务器回调
            onServerCreate();

        } catch (IOException e) {

            onError(e);

            SLog.error("Server initialize failed...");

            return false;

        }

        return true;

    }

    /**
     * 启动服务器
     */
    public void startServer() {

        try {

            waitForConnectAtThread();
        } finally {
            // 服务器启动回调
            onServerStart();
        }

    }

    /**
     * 打开等待客户端连接的线程
     */
    private void waitForConnectAtThread() {
        waitForConnectTread.start();
    }

    /**
     * 等待客户端连接
     */
    private void serverAccept() {

        try {

            SLog.error("Waiting for client connect...");

            // 等待客户端连接
            Socket socket = mServer.accept();

            // 判断连接是否正常
            if (socket != null && socket.isConnected()) {

                // 客户端 - 名字
                String name = socket.getInetAddress().getHostName();
                // 客户端 - IP地址
                String ip = socket.getInetAddress().getHostAddress();
                // 客户端 - 端口号
                int port = socket.getPort();

                // 保存客户端连接
                new MicroConnection(MicroServer.this, socket).run();

                SLog.error("new client[" + name + "(" + ip + ":" + port + ")" + "] connect success...");

            } else {
                SLog.error("new client error...");
            }

        } catch (IOException e) {

            onError(e);

            SLog.error("Wait for connect stopped that Server has been closed...");
        }

    }

    /**
     * 关闭服务器
     */
    public void closeServer() {

        SLog.error("Server is closing...");

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

        SLog.error("Server has closed...");

        // 服务器关闭回调
        onServerClose();

    }

    /**
     * 错误处理
     */
    private void onError(IOException e) {

    }

    /**
     * 增加一个客户端连接
     */
    private void addConnection(MicroConnection connection) {
        if (mState != null) mState.addConnection(connection);
    }

    /**
     * 关闭一个客户端连接
     */
    private void removeConnection(MicroConnection connection) {
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
