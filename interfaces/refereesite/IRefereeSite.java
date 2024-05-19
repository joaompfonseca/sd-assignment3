package interfaces.refereesite;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General interface for the referee site, that extends from the referee and coach interfaces for the referee site.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IRefereeSite extends IRefereeSite_Referee, IRefereeSite_Coach, Remote {
    /**
     * Shutdown the server.
     * @throws RemoteException a remote exception
     */
    public void shutdown() throws RemoteException;
}