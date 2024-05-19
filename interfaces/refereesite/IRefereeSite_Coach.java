package interfaces.refereesite;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for the coach in the referee site.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IRefereeSite_Coach extends Remote {
    /**
     * The coach waits for the referee command.
     *
     * @param team the team of the coach
     *
     * @return true if the match has not ended, false otherwise
     */
    public boolean reviewNotes(int team) throws RemoteException;
}
