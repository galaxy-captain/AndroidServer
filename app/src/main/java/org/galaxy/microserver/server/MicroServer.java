package org.galaxy.microserver.server;

import org.galaxy.microserver.utils.L;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by OoO on 2017/1/21.
 */
public class MicroServer {

    private ServerSocket mServer;

    private ServerConfig mConfig;

    private ServerState mState;

    public MicroServer() {
        this(new ServerConfig());
    }

    public MicroServer(ServerConfig config) {
        this.mConfig = config;
    }

    /**
     *
     */
    public void initServer() {

        try {

            L.error("Server initialize starting...");

            mServer = new ServerSocket(mConfig.getPort());

            mState = new ServerState(this);

            mState.setRunning(true);

            L.error("Server initialize success...");

        } catch (IOException e) {
            e.printStackTrace();

            L.error("Server initialize failed...");
        }

    }

    public void startServer(){
        waitForConnectAtThread();
    }

    /**
     *
     */
    public void waitForConnectAtThread() {

        new Thread() {

            @Override
            public void run() {

                while (mState.isRunning()) {

                    if (mState.getConnectionTotal() < mConfig.getConnectionMaximum()) {
                        serverAccept();
                    }

                }

                if (mServer == null) {
                    L.error("cannot wait for connect because server has been closed...");
                }

            }

        }.start();

    }

    /**
     *
     */
    private void serverAccept() {

        try {

            L.error("wait for connect...");

            Socket socket = mServer.accept();

            if (socket != null && socket.isConnected()) {

                String name = socket.getInetAddress().getHostName();
                String ip = socket.getInetAddress().getHostAddress();
                int port = socket.getPort();

                // 新的连接
                new MicroConnection(this, socket);

                L.error(name + "(" + ip + ":" + port + ")" + " connect success...");

            } else {
                L.error("connect failed...");
            }

        } catch (IOException e) {
            e.printStackTrace();

            L.error("connect error because server has been closed...");
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
            e.printStackTrace();
        }

        mState.setRunning(false);

        mServer = null;

        L.error("Server has closed...");

    }

    /**
     *
     */
    public void addConnection(MicroConnection connection) {
        mState.addConnection(connection);
    }

    /**
     *
     */
    public void removeConnection(MicroConnection connection) {
        mState.cutConnection(connection);
    }

    /**
     *
     */
    public MicroConnection getConnection(String name) {
        return mState.getConnection(name);
    }

    public ServerState getState() {
        return mState;
    }

}
