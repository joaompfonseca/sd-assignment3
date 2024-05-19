package interfaces.generalrepository;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for the playground in the general repository.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IGeneralRepository_Playground extends Remote {
    /**
     * Set the new state of the contestant when he is getting ready.
     *
     * @param team       the team
     * @param contestant the contestant
     */
    public void getReady(int team, int contestant) throws RemoteException;

    /**
     * Set the new state of the coach when he informs the referee.
     *
     * @param team the team
     */
    public void informReferee(int team) throws RemoteException;

    /**
     * Set the new state of the referee when he starts the trial.
     */
    public void startTrial() throws RemoteException;

    /**
     * Set the new state of the contestant when he pulls the rope.
     *
     * @param team       the team
     * @param contestant the contestant
     * @param reduce     if the strength of the contestant should be reduced
     */
    public void pullTheRope(int team, int contestant, boolean reduce) throws RemoteException;

    /**
     * Set the new state of the contestant when he's done.
     */
    public void amDone() throws RemoteException;

    /**
     * Set the new state of the referee when he asserts the trial decision.
     *
     * @param ropePosition the rope position
     */
    public void assertTrialDecision(int ropePosition) throws RemoteException;
}
