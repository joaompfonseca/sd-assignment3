package server.main;

import configuration.Config;
import interfaces.Register;
import interfaces.generalrepository.IGeneralRepository;
import interfaces.playground.IPlayground;
import server.objects.Playground;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Server that encapsulates the playground.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public class ServerPlayground {

    /**
     * Flag signaling the service is active.
     */
    public static boolean waitConnection = false;

    /**
     *  Main method.
     *
     *        args[0] - port number for listening to service requests
     *        args[1] - name of the platform where is located the RMI registering service
     *        args[2] - port number where the registering service is listening to service requests
     */
    public static void main(String[] args) {
        int portNumb = -1;              // port number for listening to service requests
        String rmiRegHostName;          // name of the platform where is located the RMI registering service
        int rmiRegPortNumb = -1;        // port number where the registering service is listening to service requests

        if (args.length != 3) {
            System.out.println("Usage: java -jar server_playground.jar <portNumb> <rmiRegHostName> <rmiRegPortNumb>");
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

        /* instantiate a playground object */
        Playground playground = new Playground(Config.N_CONTESTANTS_PER_TRIAL, reposStub);
        IPlayground pStub = null;

        try {
            pStub = (IPlayground) java.rmi.server.UnicastRemoteObject.exportObject(playground, portNumb);
        } catch (RemoteException e) {
            System.out.println("Playground stub create exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Playground stub was created!");

        /* register it with the general registry service */
        String nameEntryBase = "RegisterHandler";
        String nameEntryObject = "Playground";
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
            reg.bind(nameEntryObject, pStub);
        } catch (RemoteException e) {
            System.out.println("Playground registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("Playground already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Playground object was registered!");

        /* wait for the end of operations */
        System.out.println("Playground is in operation!");
        try {
            while(!waitConnection){
                synchronized (Class.forName("server.main.ServerPlayground")) {
                    try {
                        Class.forName("server.main.ServerPlayground").wait();
                    } catch (InterruptedException e) {
                        System.out.println("ServerPlayground main thread was interrupted!");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("The class ServerPlayground was not found!");
            e.printStackTrace();
            System.exit(1);
        }

        /* server shutdown */

        boolean shutdownDone = false;

        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Playground registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Playground not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Playground was deregistered!");

        try {
            shutdownDone = UnicastRemoteObject.unexportObject(playground, true);
        } catch (NoSuchObjectException e) {
            System.out.println("Playground unexport exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        if (shutdownDone) {
            System.out.println("Playground was shutdown!");
        }

    }

    /**
     *  Close of operations.
     */
    public static void shutdown() {
        waitConnection = true;
        try {
            synchronized (Class.forName("server.main.ServerPlayground")) {
                (Class.forName("server.main.ServerPlayground")).notify();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("The class ServerPlayground was not found!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
