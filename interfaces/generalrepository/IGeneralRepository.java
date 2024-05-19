package interfaces.generalrepository;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General interface for the general repository, that extends from the general repository interfaces for the referee site,
 * bench and the playground.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IGeneralRepository extends IGeneralRepository_Site, IGeneralRepository_Bench, IGeneralRepository_Playground, Remote {
    /**
     * Shutdown the server.
     */
    public void shutdown() throws RemoteException;
}
