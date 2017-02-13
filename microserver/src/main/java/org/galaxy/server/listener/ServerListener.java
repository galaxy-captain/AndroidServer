package org.galaxy.server.listener;

import org.galaxy.server.MicroConnection;
import org.galaxy.server.ServerConfig;
import org.galaxy.server.ServerState;

public abstract class ServerListener {

    public abstract void onCreate(ServerConfig config, ServerState state);

    public abstract void onStart(ServerConfig config, ServerState state);

    public abstract void onAccept(final MicroConnection connection);

    public abstract void onClose(ServerConfig config, ServerState state);

}

