package org.galaxy.microserver.service.manager;

import android.content.Context;
import android.content.Intent;

import org.galaxy.microserver.service.ServerService;
import org.galaxy.microserver.service.ServerServiceConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by galaxy on 2017/1/24.
 */
public class SocketManager {

    private final Map<Context, ServerServiceConnection> map = new HashMap<>();

    private void bindServer(Context context, ServerServiceConnection connection) {
        Intent intent = new Intent(context, ServerService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void unBindServer(Context context, ServerServiceConnection connection) {
        context.unbindService(connection);
    }

    public void close() {

        Set<Context> set = map.keySet();

        for (Context context : set) {
            unBindServer(context, map.get(context));
        }

    }

}
