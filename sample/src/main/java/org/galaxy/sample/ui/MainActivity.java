package org.galaxy.sample.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;

import org.galaxy.androidserver.ServerService;
import org.galaxy.androidserver.ServerServiceConnection;
import org.galaxy.androidserver.utils.NetworkUtils;
import org.galaxy.sample.R;
import org.galaxy.server.MicroConnection;
import org.galaxy.server.ServerState;
import org.galaxy.server.listener.ConnectionListener;

/**
 * Created by OoO on 2017/1/21.
 */
public class MainActivity extends AppCompatActivity {

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

        vLocalAddress.setText(NetworkUtils.localAddress(getBaseContext()));

    }

    @Override
    protected void onStart() {
        super.onStart();

        mService.setConnectionListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mService.removeConnectionListener(mListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbind();
    }

    private void bind() {
        Intent intent = new Intent(MainActivity.this, ServerService.class);
        bindService(intent, mService, Context.BIND_AUTO_CREATE);
    }

    private void unbind() {
        unbindService(mService);
    }

    private void refreshState(ServerState state) {
        vLocalAddress.setText(state.getLocalAddress());
        vPort.setText(state.getPort() + "");
    }

}
