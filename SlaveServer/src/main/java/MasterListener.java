import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.lang.Thread.sleep;

public class MasterListener implements Runnable{
    @Override
    public void run() {
        while(true){
            try {
                sleep(500);
                synchronized (Main.entries){
                    Main.entries = SlaveServer.toMaster.getSlaveList();
                    Main.printMailboxes();
                }
                SlaveServer.toMaster.ping();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RemoteException e){
                Main.printError(
                        "[SLAVE: " + SlaveServer.portNumber + "] " +
                        "connection to master lost, attempting to reconnect.");

                try {
                    Registry registryToServer = LocateRegistry.getRegistry(Main.IP, 9000);
                    SlaveToMasterCommunication toMaster = (SlaveToMasterCommunication) registryToServer.lookup("SlaveToMasterCommunication");

                    Main.entries = toMaster.confirmConfiguration(
                            Main.PORT_NUMBER,
                            InetAddress.getLocalHost().toString(),
                            Main.BASE_MAILBOX,
                            Main.BASE_MAILBOX + Main.NUMBER_OF_MAILBOXES - 1);

                    SlaveServer.toMaster = toMaster;

                    Main.print(
                            "[SLAVE: " + SlaveServer.portNumber + "] " +
                            "reconnected to master succesfully");
                } catch (Exception exception){
                    Main.printError(
                            "[SLAVE: " + SlaveServer.portNumber + "] " +
                            "reconnecting to master failed, reattempting...");
                }
            }
        }
    }
}
