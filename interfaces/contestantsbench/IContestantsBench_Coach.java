package interfaces.contestantsbench;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for the coach in the contestants bench.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IContestantsBench_Coach extends Remote {
    /**
     * The coach calls the contestants selected for the trial. The coach waits for all his contestants to seat down.
     * The coach alerts the contestants that the team has been assembled.
     *
     * @param team     the team
     * @param selected the selected contestants
     */
    public void callContestants(int team, boolean[] selected) throws RemoteException;

    /**
     * The coach gets the strengths of the contestants of his team. The coach waits until all his contestants are seated
     * down.
     *
     * @param team the team
     * @return the strengths of the team
     */
    public int[] getTeamStrengths(int team) throws RemoteException;

    /**
     * The coach sets the match end flag to alert the contestants form his team that the match has ended.
     *
     * @param team       the team
     * @param isMatchEnd the match end flag
     */
    public void setTeamIsMatchEnd(int team, boolean isMatchEnd) throws RemoteException;
}
