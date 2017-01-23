package org.galaxy.microserver.server;

/**
 * Created by galaxy on 2017/1/23.
 */
public interface OnServerReceiveListener {

    void onReceive(String name, byte[] buffer, int length);

}
