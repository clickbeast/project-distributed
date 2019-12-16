import java.rmi.RemoteException;

import static java.lang.Thread.sleep;

public class SlaveWatcher implements Runnable{
    @Override
    public void run() {
        while (true) {
            try {
                sleep(1000);

                synchronized (MasterServer.slaves){
                    for(SlaveServer slaveServer : MasterServer.slaves){
                        try {
                            slaveServer.communication().ping();
                        } catch (RemoteException re) {
                            Main.printError("[MASTER] lost connection to slave on port " + slaveServer.getPortNumber() + " attempting to reconnect");
                            slaveServer.reconnect();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
