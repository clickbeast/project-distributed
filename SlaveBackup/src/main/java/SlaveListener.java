import java.rmi.RemoteException;

import static java.lang.Thread.sleep;

public class SlaveListener implements Runnable{
    public void run() {
        while (true){
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                BackupServer.PING_TO_SLAVE.ping();
            } catch (RemoteException e) {
                Main.printError("[SLAVE " + (Main.PORT_NUMBER-2000) + " BACKUP] lost connection to slave, restarting it...");
                Main.restartSlave();
            }
        }
    }
}
