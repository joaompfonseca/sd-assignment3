package interfaces.contestantsbench;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for the contestant in the contestants bench.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IContestantsBench_Contestant extends Remote {
    /**
     * The contestant seats down and waits for the coach to call him. The last contestant to seat down alerts the coach.
     * The contestant waits for the coach to select him. If the coach does not select him, the contestant increases his
     * strength and waits again.
     *
     * @param team       the team
     * @param contestant the contestant
     * @param strength   the strength of the contestant
     * @return the strength of the contestant
     * @throws RemoteException a remote exception
     */
    public int seatDown(int team, int contestant, int strength) throws RemoteException;

    /**
     * The contestant follows the coach advice.
     *
     * @param team       the team
     * @param contestant the contestant
     * @return true if the match has not ended, false otherwise
     * @throws RemoteException a remote exception
     */
    public boolean followCoachAdvice(int team, int contestant) throws RemoteException;
}
