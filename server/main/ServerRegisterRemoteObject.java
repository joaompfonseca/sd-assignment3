package server.main;

import interfaces.Register;
import server.objects.RegisterRemoteObject;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Instantiation and registering of a remote object that enables the registration of other remote objects
 * located in the same or other processing nodes of a parallel machine in the local registry service.
 * <p>
 * Communication is based in Java RMI.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public class ServerRegisterRemoteObject {

    /**
     * Main method.
     *
     * @param args runtime arguments
     *             <ul>
     *                 <li>args[0] - Registry server listening port</li>
     *                 <li>args[1] - RMI server hostname</li>
     *                 <li>args[2] - RMI server port number</li>
     *             </ul>
     */
    public static void main(String[] args) {
        // Runtime arguments
        int regPort = -1;
        String rmiHost;
        int rmiPort = -1;

        // Validate and parse runtime arguments
        if (args.length != 3) {
            System.err.println("Wrong number of parameters!");
            System.exit(1);
        }
        try {
            regPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("args[0] is not a number!");
            System.exit(1);
        }
        if ((regPort < 4000) || (regPort >= 65536)) {
            System.out.println("args[0] is not a valid port number!");
            System.exit(1);
        }
        rmiHost = args[1];
        try {
            rmiPort = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("args[2] is not a number!");
            System.exit(1);
        }
        if ((rmiPort < 4000) || (rmiPort >= 65536)) {
            System.out.println("args[2] is not a valid port number!");
            System.exit(1);
        }

        // create and install the security manager

        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        System.out.println("Security manager was installed!");

        // instantiate a registration remote object and generate a stub for it

        RegisterRemoteObject regEngine = new RegisterRemoteObject(rmiHost, rmiPort);
        Register regEngineStub = null;

        try {
            regEngineStub = (Register) UnicastRemoteObject.exportObject(regEngine, regPort);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject stub generation exception: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("Stub was generated!");

        // register it with the local registry service

        String nameEntry = "RegisterHandler";
        Registry registry = null;

        try {
            registry = LocateRegistry.getRegistry(rmiHost, rmiPort);
        } catch (RemoteException e) {
            System.out.println("RMI registry creation exception: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("RMI registry was created!");

        try {
            registry.rebind(nameEntry, regEngineStub);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject remote exception on registration: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("RegisterRemoteObject object was registered!");
    }
}
