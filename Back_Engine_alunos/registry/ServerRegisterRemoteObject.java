package registry;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.AccessException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.*;
import java.util.Scanner;
import interfaces.Register;

/**
 *   Instantiation and registering of a remote object that enables the registration of other remote objects
 *   located in the same or other processing nodes of a parallel machine in the local registry service.
 *
 *   Communication is based in Java RMI.
 */

public class ServerRegisterRemoteObject
{
  /**
   *  Main method.
   *
   *    @param args runtime arguments
   */

   public static void main(String[] args)
   {
    /* get location of the registering service */

     String rmiRegHostName;
     int rmiRegPortNumb;

     Scanner sc= new Scanner(System.in);

     System.out.println ("Name of the processing node where the registering service is located? ");
     rmiRegHostName = sc.nextLine ();
     System.out.println ("Port number where the registering service is listening to? ");
     rmiRegPortNumb = sc.nextInt ();

    /* create and install the security manager */

     if (System.getSecurityManager () == null)
        System.setSecurityManager (new SecurityManager ());
     System.out.println ("Security manager was installed!");

    /* instantiate a registration remote object and generate a stub for it */

     RegisterRemoteObject regEngine = new RegisterRemoteObject (rmiRegHostName, rmiRegPortNumb);
     Register regEngineStub = null;
     int listeningPort = 22001;                                      /* it should be set accordingly in each case */

     try
     { regEngineStub = (Register) UnicastRemoteObject.exportObject (regEngine, listeningPort);
     }
     catch (RemoteException e)
     { System.out.println ("RegisterRemoteObject stub generation exception: " + e.getMessage ());
       System.exit (1);
     }
     System.out.println ("Stub was generated!");

    /* register it with the local registry service */

     String nameEntry = "RegisterHandler";
     Registry registry = null;

     try
     { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
     }
     catch (RemoteException e)
     { System.out.println ("RMI registry creation exception: " + e.getMessage ());
       System.exit (1);
     }
     System.out.println ("RMI registry was created!");

     try
     { registry.rebind (nameEntry, regEngineStub);
     }
     catch (RemoteException e)
     { System.out.println ("RegisterRemoteObject remote exception on registration: " + e.getMessage ());
       System.exit (1);
     }
     System.out.println ("RegisterRemoteObject object was registered!");
   }
}
