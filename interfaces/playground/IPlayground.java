package interfaces.playground;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General interface for the playground, that extends from the playground interfaces for the playground.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IPlayground extends IPlayground_Coach, IPlayground_Contestant, IPlayground_Referee, Remote {
    /**
     * Shutdown the server.
     * @throws RemoteException a remote exception
     */
    public void shutdown() throws RemoteException;
}