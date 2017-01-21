package org.galaxy.microserver.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.galaxy.microserver.R;
import org.galaxy.microserver.service.ServerService;
import org.galaxy.microserver.service.ServerServiceConnection;

/**
 * Created by OoO on 2017/1/21.
 */
public class MainActivity extends AppCompatActivity {

    private Button startBtn,closeBtn;

    private ServerServiceConnection mConnection = new ServerServiceConnection();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = (Button) findViewById(R.id.start_btn);
        closeBtn = (Button) findViewById(R.id.close_btn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnection.startServer();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnection.closeServer();
            }
        });

        bind();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbind();
    }

    private void bind(){
        Intent intent = new Intent(MainActivity.this, ServerService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbind(){
        unbindService(mConnection);
    }

}
