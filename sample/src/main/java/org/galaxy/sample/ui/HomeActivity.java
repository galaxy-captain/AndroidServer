package org.galaxy.sample.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;

import org.galaxy.androidserver.ServerServiceConnection;
import org.galaxy.androidserver.newApi.AndroidServer;
import org.galaxy.androidserver.utils.NetworkUtils;
import org.galaxy.sample.R;
import org.galaxy.server.MicroConnection;
import org.galaxy.server.MicroServer;
import org.galaxy.server.ServerState;
import org.galaxy.server.listener.ConnectionListener;

import java.util.Collection;

/**
 * Created by OoO on 2017/1/21.
 */
public class HomeActivity extends AppCompatActivity {

    private TextView vLocalAddress, vPort, vServerState;
    private GridView vConnection;

    private ServerServiceConnection mService = new ServerServiceConnection();

    private static final String CLOSE = "关闭";

    private static final String SUCCESS = "正常";

    private ConnectionGridAdapter mAdapter;

    private ConnectionListener mListener = new ConnectionListener() {

        @Override
        public void onAccept(final MicroConnection connection) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.add(connection);
                    AndroidServer.getInstance().send(9999, "connect success");

                }
            });

        }

        @Override
        public void onClose(final MicroConnection connection) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.remove(connection);
                }
            });

        }

        @Override
        public void onReceive(final MicroConnection connection, byte[] buffer, int length) {

        }

        @Override
        public void onSend(final MicroConnection connection, byte[] buffer) {

        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidServer.getInstance().bind(this, new AndroidServer.Callback() {
            @Override
            public void onServiceConnected() {
                AndroidServer.getInstance().addPortListen(9999, mListener);

                MicroServer server = AndroidServer.getInstance().getServer(9999);

                vLocalAddress.setText(server.getConfig().getLocalAddress() + "");
                vPort.setText(server.getConfig().getPort() + "");

                Collection collection = server.getState().getAllConnection().values();
            }
        });


        vLocalAddress = (TextView) findViewById(R.id.serverInfo_localAddress_tv);
        vPort = (TextView) findViewById(R.id.serverInfo_port_tv);
        vServerState = (TextView) findViewById(R.id.serverInfo_serverState_tv);
        vConnection = (GridView) findViewById(R.id.activity_main_connection_gv);

        mAdapter = new ConnectionGridAdapter(this);
        vConnection.setAdapter(mAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        AndroidServer.getInstance().removePortListen(9999);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AndroidServer.getInstance().unbind(this);
    }

    private void refreshState(ServerState state) {
        vLocalAddress.setText(state.getLocalAddress());
        vPort.setText(state.getPort() + "");
    }

}