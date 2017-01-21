package org.galaxy.microserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by OoO on 2017/1/21.
 */

public class ServerService extends Service {

    public class ServerBinder extends Binder{
        ServerService getService() {
            return ServerService.this;
        }
    }

    private ServerBinder mBinder = new ServerBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



}
