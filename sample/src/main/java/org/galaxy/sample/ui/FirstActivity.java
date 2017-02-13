package org.galaxy.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.galaxy.androidserver.newApi.AndroidServer;
import org.galaxy.sample.R;

/**
 * Created by galaxy on 2017/1/24.
 */
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        AndroidServer.getInstance().bind(this, new AndroidServer.Callback() {

            @Override
            public void onServiceConnected() {

                AndroidServer.getInstance().open(9999);

                startActivity();
            }

        });

    }

    private void startActivity() {
        Intent intent = new Intent(FirstActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AndroidServer.getInstance().unbind(this);
    }

}
