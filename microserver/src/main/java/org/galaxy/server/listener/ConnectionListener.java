package org.galaxy.server.listener;

import org.galaxy.server.MicroConnection;

/**
 * Created by galaxy on 2017/1/24.
 *
 * 已建立连接的回调
 */
public abstract class ConnectionListener {

    /**
     * 连接建立成功
     */
    public abstract void onAccept(final MicroConnection connection);

    /**
     * 连接断开成功
     */
    public abstract void onClose(final MicroConnection connection);

    /**
     * 连接收到数据
     */
    public abstract void onReceive(final MicroConnection connection, byte[] buffer, int length);

    /**
     * 连接发送数据
     */
    public abstract void onSend(final MicroConnection connection, byte[] buffer);

}