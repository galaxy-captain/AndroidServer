package org.galaxy.microserver.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import org.galaxy.microserver.R;
import org.galaxy.microserver.server.MicroConnection;
import org.galaxy.microserver.server.ServerState;
import org.galaxy.microserver.server.callback.ConnectionListener;
import org.galaxy.microserver.service.ServerService;
import org.galaxy.microserver.service.ServerServiceConnection;
import org.galaxy.microserver.utils.NetworkUtils;

/**
 * Created by OoO on 2017/1/21.
 */
public class MainActivity extends AppCompatActivity {

    private TextView vLocalAddress, vPort, vServerState;
    private GridView vConnection;

    private ServerServiceConnection mConnection = new ServerServiceConnection();

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
//            mAdapter.update(connection);
        }

        @Override
        public void onSend(final MicroConnection connection, byte[] buffer) {
//            mAdapter.update(connection);
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bind();

        vLocalAddress = (TextView) findViewById(R.id.serverInfo_localAddress_tv);
        vPort = (TextView) findViewById(R.id.serverInfo_port_tv);
        vServerState = (TextView) findViewById(R.id.serverInfo_serverState_tv);
        vConnection = (GridView) findViewById(R.id.activity_main_connection_gv);

        mAdapter = new ConnectionGridAdapter(this);
        vConnection.setAdapter(mAdapter);

        vLocalAddress.setText(NetworkUtils.localAddress(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        addListener();

    }

    @Override
    protected void onStop() {
        super.onStop();

        mConnection.removeConnectionListener(mListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbind();
    }

    public void addListener() {

        try {
            mConnection.setConnectionListener(mListener);
        } catch (NullPointerException e) {

            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    addListener();
                }
            }.start();

        }
    }

    private void bind() {
        Intent intent = new Intent(MainActivity.this, ServerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbind() {
        unbindService(mConnection);
    }

    private void refreshState(ServerState state) {
        vLocalAddress.setText(state.getLocalAddress());
        vPort.setText(state.getPort() + "");
    }

}
