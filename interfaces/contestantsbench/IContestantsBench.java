package interfaces.contestantsbench;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General interface for the contestants bench, that extends from the contestants bench interfaces for the contestant
 * and the coach.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IContestantsBench extends IContestantsBench_Contestant, IContestantsBench_Coach, Remote {
    /**
     * Shutdown the server.
     */
    public void shutdown() throws RemoteException;
}