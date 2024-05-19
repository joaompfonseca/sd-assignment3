package interfaces.playground;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface of the referee in the playground.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IPlayground_Referee extends Remote {
    /**
     * The referee waits for the coaches to be ready to announce the start of the trial.
     */
    public void startTrial() throws RemoteException;

    /**
     * The referee waits for the contestants to be done to decide the result of the trial.
     *
     * @return the rope position
     */
    public int assertTrialDecision() throws RemoteException;

    /**
     * The referee sets the rope position.
     *
     * @param ropePosition the rope position
     */
    public void setRopePosition(int ropePosition) throws RemoteException;
}
