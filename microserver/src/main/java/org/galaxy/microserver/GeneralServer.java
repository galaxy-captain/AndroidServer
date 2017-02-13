package org.galaxy.microserver;

import org.galaxy.microserver.impl.TcpServerImpl;

/**
 * Created by galaxy on 2017/2/10.
 */
public abstract class GeneralServer implements TcpServerImpl {

    private GeneralServerInfo mInfo;

    public GeneralServer() {

    }

}
