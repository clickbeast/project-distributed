import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.lang.Thread.sleep;

public class WatcherCommunication implements Runnable{
    @Override
    public void run() {
        //Iedere seconde pingen naar de watcher.
        //Indien offline -> herstarten
        while(true){
            try {
                sleep(500);
                Main.CONNECTION_TO_WATCHER.ping();
            }
            catch (Exception e){
                Main.printError("[MASTER] connection to watcher lost, attempting to restart it...");
                restartWatcher();
            }
        }
    }

    private void restartWatcher(){
        ProcessBuilder pb;

        pb = new ProcessBuilder(
                "/bin/bash",
                "Watcher.sh",
                "restart");

        pb.directory(new File(Main.PATH_TO_WATCHER_JAR));
        try {
            Process p = pb.start();
            MasterServer.watch(p);
        } catch (IOException e) {
            Main.printError(e.toString());
        }

        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean reconnected = false;

        while (!reconnected){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try{
                Registry pingToWatcher = LocateRegistry.getRegistry(Main.IP_OF_WATCHER, Main.PORT_OF_WATCHER);
                Main.CONNECTION_TO_WATCHER = (Ping) pingToWatcher.lookup("Ping");
                reconnected = true;
            } catch (RemoteException | NotBoundException e){
                Main.printError("[MASTER] reconnecting to watcher failed, trying again...");
            }
        }
        Main.print("[MASTER] succesfully restarted watcher");
    }
}
