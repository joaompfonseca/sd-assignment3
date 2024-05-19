package interfaces.generalrepository;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for the bench in the general repository.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IGeneralRepository_Bench extends Remote {
    /**
     * Set the new state of the contestant when he seats down.
     *
     * @param team             the team
     * @param id               the contestant id
     * @param increaseStrength if the strength of the contestant should be increased
     */
    public void seatDown(int team, int id, boolean increaseStrength) throws RemoteException;

    /**
     * Set the new state of the coach when he calls the contestants.
     *
     * @param team the team
     */
    public void callContestants(int team) throws RemoteException;

    /**
     * Set the new state of the contestant when he is called by the coach.
     *
     * @param team the team
     * @param id   the contestant id
     */
    public void followCoachAdvice(int team, int id) throws RemoteException;
}
