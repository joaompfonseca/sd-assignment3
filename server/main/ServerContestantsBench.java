package server.main;

import configuration.Config;
import interfaces.Register;
import interfaces.contestantsbench.IContestantsBench;
import interfaces.generalrepository.IGeneralRepository;
import server.objects.ContestantsBench;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Server that encapsulates the contestants bench.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public class ServerContestantsBench {

    /**
     * Flag signaling the service is active.
     */
    public static boolean waitConnection = false;

    /**
     *  Main method.
     *
     * @param args runtime arguments
     *             <ul>
     *                 <li>args[0] - port number for listening to service requests</li>
     *                 <li>args[1] - RMI registry server hostname</li>
     *                 <li>args[2] - RMI registry server port number</li>
     *             </ul>
     */
    public static void main(String[] args) {
        int portNumb = -1;              // port number for listening to service requests
        String rmiRegHostName;          // name of the platform where is located the RMI registering service
        int rmiRegPortNumb = -1;        // port number where the registering service is listening to service requests

        if (args.length != 3) {
            System.out.println("Usage: java -jar server_contestants_bench.jar <portNumb> <rmiRegHostName> <rmiRegPortNumb>");
            System.exit(1);
        }

        try {
            portNumb = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("args[0] is not a number!");
            System.exit(1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536)) {
            System.out.println("args[0] is not a valid port number!");
            System.exit (1);
        }

        rmiRegHostName = args[1];

        try {
            rmiRegPortNumb = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("args[2] is not a number!");
            System.exit(1);
        }
        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536)) {
            System.out.println("args[2] is not a valid port number!");
            System.exit (1);
        }

        /* create and install the security manager */

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        System.out.println("Security manager was installed!");

        /* get a remote reference to the general repository object */
        String nameEntryGeneralRepos = "GeneralRepository";           // public name of the general repository object
        IGeneralRepository reposStub = null;                 // remote reference to the general repository object
        Registry registry = null;                                   // remote reference for registration in the RMI registry service

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("RMI registry was created!");

        try {
            reposStub = (IGeneralRepository) registry.lookup(nameEntryGeneralRepos);
        } catch (RemoteException e) {
            System.out.println("GeneralRepository lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("GeneralRepository not bound to registry: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        /* instantiate a contestant bench object */
        ContestantsBench contestantsBench = new ContestantsBench(Config.N_CONTESTANTS_PER_TEAM, Config.MAX_STRENGTH, reposStub);
        IContestantsBench cBenchStub = null;

        try {
            cBenchStub = (IContestantsBench) UnicastRemoteObject.exportObject(contestantsBench, portNumb);
        } catch (RemoteException e) {
            System.out.println("ContestantsBench stub create exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("ContestantsBench stub was created!");

        /* register it with the general registry service */

        String nameEntryBase = "RegisterHandler";
        String nameEntryObject = "ContestantsBench";
        Register reg = null;

        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, cBenchStub);
        } catch (RemoteException e) {
            System.out.println("ContestantsBench registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("ContestantsBench already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("ContestantsBench object was registered!");

        /* wait for the end of operations */
        System.out.println("ContestantsBench is in operation!");
        try {
            while (!waitConnection) {
                synchronized (Class.forName("server.main.ServerContestantsBench")) {
                    try {
                        (Class.forName("server.main.ServerContestantsBench")).wait();
                    } catch (InterruptedException e) {
                        System.out.println("ServerContestantsBench main thread was interrupted!");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("The data type server.main.ServerContestantsBench was not found (blocking)!");
            e.printStackTrace();
            System.exit(1);
        }

        /* server shutdown */

        boolean shutdownDone = false;

        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("ContestantsBench registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ContestantsBench not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("ContestantsBench was deregistered!");

        try {
            shutdownDone = UnicastRemoteObject.unexportObject(contestantsBench, true);
        } catch (NoSuchObjectException e) {
            System.out.println("ContestantsBench unexport exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        if (shutdownDone) {
            System.out.println("ContestantsBench was shutdown!");
        }
    }

    /**
     *  Close of operations.
     */
    public static void shutdown() {
        waitConnection = true;
        try {
            synchronized (Class.forName("server.main.ServerContestantsBench")) {
                (Class.forName("server.main.ServerContestantsBench")).notify();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("The data type server.main.ServerContestantsBench was not found (waking up)!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Private constructor to hide the implicit public one.
     */
    private ServerContestantsBench() {
    }
}
