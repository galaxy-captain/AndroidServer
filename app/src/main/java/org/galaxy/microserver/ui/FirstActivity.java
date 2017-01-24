package org.galaxy.microserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.galaxy.microserver.R;
import org.galaxy.microserver.service.ServerService;

/**
 * Created by galaxy on 2017/1/24.
 */
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Intent intent = new Intent(FirstActivity.this, ServerService.class);

        startService(intent);

        new Thread(){

            @Override
            public void run() {

                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(FirstActivity.this, MainActivity.class);

                        startActivity(intent);

                        FirstActivity.this.finish();
                    }
                });

            }

        }.start();

    }

}
