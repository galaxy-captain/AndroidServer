package org.galaxy.microserver.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.galaxy.microserver.R;
import org.galaxy.microserver.server.ServerState;
import org.galaxy.microserver.service.ServerService;
import org.galaxy.microserver.service.ServerServiceConnection;

/**
 * Created by OoO on 2017/1/21.
 */
public class MainActivity extends AppCompatActivity {

    private TextView vLocalAddress, vPort, vServerState, vConnectionState, vHeartState;

    private ServerServiceConnection mConnection = new ServerServiceConnection();

    private static final String CLOSE = "关闭";

    private static final String SUCCESS = "正常";

    private boolean isListen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vLocalAddress = (TextView) findViewById(R.id.serverInfo_localAddress_tv);
        vPort = (TextView) findViewById(R.id.serverInfo_port_tv);
        vServerState = (TextView) findViewById(R.id.serverInfo_serverState_tv);
        vConnectionState = (TextView) findViewById(R.id.serverInfo_connectionState_tv);
        vHeartState = (TextView) findViewById(R.id.serverInfo_heartState_tv);

        bind();

    }

    @Override
    protected void onStart() {
        super.onStart();

        listenServerState();

        isListen = true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        isListen = false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbind();
    }

    private void bind() {
        Intent intent = new Intent(MainActivity.this, ServerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbind() {
        unbindService(mConnection);
    }

    private void listenServerState() {

        new Thread() {

            @Override
            public void run() {

                while (isListen) {

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isListen)
                                refreshState(mConnection.getServerState());
                        }
                    });
                }

            }

        }.start();

    }

    private void refreshState(ServerState state) {
        vLocalAddress.setText(state.getLocalAddress());
        vPort.setText(state.getPort() + "");
        vServerState.setText(state.isRunning() ? SUCCESS : CLOSE);
    }

}
