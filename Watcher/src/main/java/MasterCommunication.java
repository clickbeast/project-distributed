import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ReadOnlyBufferException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MasterCommunication extends UnicastRemoteObject implements Runnable,Ping{

    public MasterCommunication() throws RemoteException{}

    public void setup(){
        boolean connectionSetUp = false;

        while(!connectionSetUp){
            try{
                sleep(500);
                Registry registry = LocateRegistry.getRegistry(Main.IP_OF_MASTER, Main.PORT_FOR_PINGING_TO_MASTER);
                Main.PING_TO_MASTER = (Ping) registry.lookup("Ping");
                connectionSetUp = true;
            }
            catch(Exception e){
                Main.printError("[WATCHER] error setting up connection to master, trying again...");
e.printStackTrace();            }
        }
        Main.print("[WATCHER] ping to master succeeded.");
    }

    public void run() {
        while(true){
            try {
                sleep(1000);
                Main.PING_TO_MASTER.ping();
                Main.entries = Main.PING_TO_MASTER.getEntries();
                Main.print("[WATCHER] ping to master succesfull");
            } catch (Exception e) {
                Main.printError(
                        "[WATCHER] connection lost to master, attempting to restart it...");

                Main.startMaster(false);

                try {
                    sleep(1000);
                    Registry registry = LocateRegistry.getRegistry(Main.IP_OF_MASTER,Main.PORT_FOR_PINGING_TO_MASTER);
                    Main.PING_TO_MASTER = (Ping) registry.lookup("Ping");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean ping() throws RemoteException {
        return true;
    }

    public void kill() throws RemoteException {
        System.exit(0);
    }

    public LinkedList<ServerEntry> getEntries() throws RemoteException {
        return Main.entries;
    }
}
