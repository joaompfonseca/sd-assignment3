package interfaces.generalrepository;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for the referee site in the general repository.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IGeneralRepository_Site extends Remote {
    /**
     * Set the new state of the coach when he waits for the referee.
     *
     * @param team the team
     * @throws RemoteException a remote exception
     */
    public void reviewNotes(int team) throws RemoteException;

    /**
     * Set the new state of the referee when he announces a new game.
     *
     * @throws RemoteException a remote exception
     */
    public void announceNewGame() throws RemoteException;

    /**
     * Set the new state of the referee when he calls a trial.
     *
     * @throws RemoteException a remote exception
     */
    public void callTrial() throws RemoteException;

    /**
     * Set the new state of the referee when he declares the game winner.
     *
     * @param team     the team
     * @param knockout the knockout flag
     * @throws RemoteException a remote exception
     */
    public void declareGameWinner(int team, boolean knockout) throws RemoteException;

    /**
     * Set the new state of the referee when he declares the match winner.
     *
     * @param team the team
     * @throws RemoteException a remote exception
     */
    public void declareMatchWinner(int team) throws RemoteException;
}
