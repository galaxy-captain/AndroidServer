package org.galaxy.microserver.server;

import org.galaxy.microserver.utils.L;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by OoO on 2017/1/21.
 */

public class MicroConnection {

    private final MicroServer mServer;

    private final Socket mSocket;

    private final String name;

    private final String ip;

    private final int port;

    private boolean isConnect = false;

    public MicroConnection(MicroServer server, Socket socket) {
        this.mServer = server;
        this.mSocket = socket;

        this.ip = socket.getInetAddress().getHostAddress();
        this.port = socket.getPort();

        this.name = ip + ":" + port;

        this.mServer.addConnection(this);

        listenStreamAtThread();//
        loopLiveTestAtThread();//
    }

    /**
     *
     */
    public String getName() {
        return name;
    }

    /**
     *
     */
    public void listenStreamAtThread() {

        new Thread() {

            @Override
            public void run() {
                listenStream();
            }

        }.start();

    }

    /**
     *
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

            L.error(name + " connect closed...");

        } catch (IOException e) {
            onConnectError(e);
        }

    }

    /**
     *
     */
    private void onReceive(byte[] buffer, int length) {

        String data = new String(buffer, 0, length);

        L.error(data);
    }

    /**
     *
     */
    public boolean sendMessageAtMain(String data) {

        try {

            if (!isConnected()) return false;

            OutputStream stream = mSocket.getOutputStream();

            byte[] buffer = data.getBytes();

            stream.write(buffer);

            stream.flush();

        } catch (IOException e) {

            onConnectError(e);

            return false;
        }

        return true;
    }

    /**
     *
     */
    public void loopLiveTestAtThread() {

        new Thread() {

            @Override
            public void run() {

                try {

                    while (isConnected()) {

                        mSocket.sendUrgentData(0xFF);

                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            L.error("thread sleep error...");
                        }

                    }

                } catch (IOException e) {
                    onConnectError(e);
                }

            }

        }.start();

    }

    /**
     *
     */
    private void onConnectError(IOException e) {

        e.printStackTrace();

        L.error(name + " connect error...");

        isConnect = false;

        mServer.removeConnection(this);
    }

    /**
     *
     */
    private boolean isConnected() {
        return mServer.getState().isRunning() && mSocket.isConnected() && isConnect;
    }

}
