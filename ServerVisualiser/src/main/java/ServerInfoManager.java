import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ServerInfoManager {

    private static final String MASTER_SERVER_IP = "localhost:7001";

    public void sendMessage(final ThreadListener threadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        List<Slave> slaves = new ArrayList<>();
                        Registry registry = LocateRegistry.getRegistry(MASTER_SERVER_IP.split(":")[0],
                                Integer.parseInt(MASTER_SERVER_IP.split(":")[1]));

                        VisualizerToMasterCommunication visualizer =
                                (VisualizerToMasterCommunication) registry.lookup("VisualizerToMasterCommunication");
                        for (ServerEntry serverEntry : visualizer.getSlaveList()) {
                            Slave slave = new Slave(serverEntry.endMailbox - serverEntry.startMailbox,
                                    serverEntry.startMailbox);

                            try {

                                Registry slaveRegistery = LocateRegistry.getRegistry(serverEntry.getIp(),
                                        serverEntry.portNumber);
                                VisualizerToSlaveCommunication slaveCommunication =
                                        (VisualizerToSlaveCommunication) registry.lookup(
                                                "VisualizerToSlaveCommunication");
                                List<MailBoxEntry> mailBoxEntries = slaveCommunication.getAllMailBoxEntries();
                                MailBoxEntry
                             //   slave.setMailBoxes(mailBoxEntries);
                                for (int i = 0; i < (mailBoxEntries.size()) && i < slave.getMailboxes().length; i++) {
                                    /*slave.setMailBox(i, mailBoxEntries.get(i));
                                    for (MailBoxEntry mailBoxEntry : mailBoxEntries) {
                                        mailBoxEntry
                                    }*/
                                }
                            } catch (RemoteException | NotBoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (RemoteException | NotBoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
