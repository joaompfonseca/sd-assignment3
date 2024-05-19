package server.main;

import configuration.Config;
import interfaces.Register;
import interfaces.generalrepository.IGeneralRepository;
import server.objects.GeneralRepository;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;

/**
 * Server side of the General Repository of Information.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public class ServerGeneralRepository {

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
        int portNumb = -1;                                             // port number for listening to service requests
        String rmiRegHostName;                                         // name of the platform where is located the RMI registering service
        int rmiRegPortNumb = -1;                                       // port number where the registering service is listening to service requests

        if (args.length != 3) {
            System.out.println("Wrong number of parameters!");
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
            System.exit(1);
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
            System.exit(1);
        }

        /* create and install the security manager */

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        System.out.println("Security manager was installed!");

        /* instantiate a general repository object */
        GeneralRepository repos = new GeneralRepository(Config.N_CONTESTANTS_PER_TEAM, Config.LOGS_FOLDER);
        IGeneralRepository reposStub = null;

        try {
            reposStub = (IGeneralRepository) UnicastRemoteObject.exportObject (repos, portNumb);
        } catch (Exception e) {
            System.out.println("General Repository stub exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("General Repository stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = "RegisterHandler";
        String nameEntryObject = "GeneralRepository";
        Registry registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("RMI registry was created!");

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
            reg.bind(nameEntryObject, reposStub);
        } catch (RemoteException e) {
            System.out.println("General Repository registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("General Repository already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        /* wait for the end of operations */
        System.out.println("General Repository is in operation!");

        try {
            while (!waitConnection) {
                synchronized (Class.forName("server.main.ServerGeneralRepository")) {
                    try {
                        Class.forName("server.main.ServerGeneralRepository").wait();
                    } catch (InterruptedException e) {
                        System.out.println("ServerGeneralRepository main thread was interrupted!");
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ServerGeneralRepository main thread was interrupted!");
            e.printStackTrace();
            System.exit(1);
        }

        /* server shutdown */

        boolean shutdownDone = false;

        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("General Repository registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("General Repository not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("General Repository was deregistered!");

        try {
            shutdownDone = UnicastRemoteObject.unexportObject(repos, true);
        } catch (NoSuchObjectException e) {
            System.out.println("General Repository unexport exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        if (shutdownDone) {
            System.out.println("General Repository was shutdown!");
        }
    }

    /**
     *  Close of operations.
     */
    public static void shutdown() {
        waitConnection = true;
        try {
            synchronized (Class.forName("server.main.ServerGeneralRepository")) {
                Class.forName("server.main.ServerGeneralRepository").notify();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ServerGeneralRepository main thread was interrupted!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Private constructor to hide the implicit public one.
     */
    private ServerGeneralRepository() {
    }
}
