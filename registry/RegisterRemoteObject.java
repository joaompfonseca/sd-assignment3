package registry;

import interfaces.Register;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Generic functionality to register remote objects in the local registry service.
 * <p>
 * Communication is based in Java RMI.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public class RegisterRemoteObject implements Register {

    /**
     * Name of the local host (where the registering service is supposed to be located).
     */
    private String rmiHost;

    /**
     * Port number where the local registering service is listening to.
     */
    private int rmiPort = 1099;

    /**
     * Instantiation of a registering object (where the registering service is supposed to be located).
     *
     * @param rmiHost name of local host
     * @param rmiPort port number where the local registering service is listening to
     */
    public RegisterRemoteObject(String rmiHost, int rmiPort) {
        if ((rmiHost == null) || ("".equals(rmiHost)))
            throw new NullPointerException("RegisterRemoteObject: null pointer parameter on instantiation!");
        this.rmiHost = rmiHost;
        if ((rmiPort >= 4000) && (rmiPort <= 65535))
            this.rmiPort = rmiPort;
    }

    /**
     * Binds a remote reference to the specified name in this registry.
     *
     * @param name the name to associate with the reference to the remote object
     * @param ref  reference to the remote object
     * @throws RemoteException       if either the invocation of the remote method, or the communication with the registry
     *                               service fails
     * @throws AlreadyBoundException if the name is already registered
     */
    @Override
    public void bind(String name, Remote ref) throws RemoteException, AlreadyBoundException {
        Registry registry;

        if ((name == null) || (ref == null))
            throw new NullPointerException("RegisterRemoteObject: null pointer parameter(s) on on bind!");
        registry = LocateRegistry.getRegistry(rmiHost, rmiPort);
        registry.bind(name, ref);
    }

    /**
     * Removes the binding for the specified name in this registry.
     *
     * @param name the name associated with the reference to the remote object
     * @throws RemoteException   if either the invocation of the remote method, or the communication with the registry
     *                           service fails
     * @throws NotBoundException if the name is not in registered
     */
    @Override
    public void unbind(String name) throws RemoteException, NotBoundException {
        Registry registry;

        if ((name == null))
            throw new NullPointerException("RegisterRemoteObject: null pointer parameter(s) on unbind!");
        registry = LocateRegistry.getRegistry(rmiHost, rmiPort);
        registry.unbind(name);
    }

    /**
     * Replaces the binding for the specified name in this registry with the supplied remote reference.
     * <p>
     * If a previous binding for the specified name exists, it is discarded.
     *
     * @param name the name to associate with the reference to the remote object
     * @param ref  reference to the remote object
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                         service fails
     */
    @Override
    public void rebind(String name, Remote ref) throws RemoteException {
        Registry registry;

        if ((name == null) || (ref == null))
            throw new NullPointerException("RegisterRemoteObject: null pointer parameter(s) on rebind!");
        registry = LocateRegistry.getRegistry(rmiHost, rmiPort);
        registry.rebind(name, ref);
    }
}
