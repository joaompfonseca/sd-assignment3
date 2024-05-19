package client.main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import client.entities.TReferee;
import configuration.Config;
import interfaces.contestantsbench.IContestantsBench;
import interfaces.playground.IPlayground;
import interfaces.refereesite.IRefereeSite;

/**
 * Client that encapsulates the referee thread.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public class RefereeClient {
    /**
     * Main method.
     * <p>
     * Communication is based in Java RMI.
     * Connects to the provided registry server using the provided hostname and port number.
     * Looks up the remote objects for the Playground and Referee Site.
     * Initiates referee thread and awaits its completion.
     *
     * @param args runtime arguments
     *             <ul>
     *                 <li>args[0]: RMI registry server hostname</li>
     *                 <li>args[1]: RMI registry server port number</li>
     *             </ul>
     */
    public static void main(String[] args) {
        // Runtime arguments
        String regHost;
        int regPort = -1;

        // Shared regions
        String pgName = "Playground";
        IPlayground pgObj = null;
        String rsName = "RefereeSite";
        IRefereeSite rsObj = null;

        // Validate and parse runtime arguments
        if (args.length != 2) {
            System.err.println("Wrong number of parameters!");
            System.exit(1);
        }
        regHost = args[0];
        try {
            regPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("args[1] is not a number!");
            System.exit(1);
        }
        if ((regPort < 4000) || (regPort >= 65536)) {
            System.err.println("args[1] is not a valid port number!");
            System.exit(1);
        }

        // Look for the remote objects by name in the remote host registry
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(regHost, regPort);
        } catch (RemoteException e) {
            System.err.println("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            pgObj = (IPlayground) registry.lookup(pgName);
            rsObj = (IRefereeSite) registry.lookup(rsName);
        } catch (RemoteException e) {
            System.err.println("CoachClient look up exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.err.println("CoachClient not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Thread
        Thread tReferee;

        // Instantiate thread
        tReferee = new TReferee(pgObj, rsObj, Config.N_GAMES_PER_MATCH, Config.N_TRIALS_PER_GAME);

        // Start thread
        tReferee.start();

        // Wait for thread to finish
        try {
            tReferee.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Shutdown stubs
        try {
            pgObj.shutdown();
            rsObj.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Private constructor to hide the implicit public one.
     */
    private RefereeClient() {
    }
}
