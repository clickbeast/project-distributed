import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ServerInfoManager {

    private static final String MASTER_SERVER_IP = "localhost:7001";

    public void checkBoxes(Master master, final ThreadListener threadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("a");
                while (true) {
                    try {
                        List<Slave> slaves = master.getSlaves();
                        Registry registry = LocateRegistry.getRegistry(MASTER_SERVER_IP.split(":")[0],
                                Integer.parseInt(MASTER_SERVER_IP.split(":")[1]));

                        VisualizerToMasterCommunication visualizer =
                                (VisualizerToMasterCommunication) registry.lookup("VisualizerToMasterCommunication");
                        LinkedList<ServerEntry> a = visualizer.getSlaveList();
                        for (ServerEntry serverEntry : visualizer.getSlaveList()) {
                            Slave slave =
                                    slaves.stream().filter(x -> x.getName().equals(serverEntry.getIp() + ":" + (serverEntry.getPortNumber())))
                                            .findAny().orElse(new Slave(serverEntry.endMailbox - serverEntry.startMailbox + 1,
                                            serverEntry.startMailbox, serverEntry.ip + ":" + serverEntry.portNumber));
                            master.addSlave(slave);
                            try {

                                Registry slaveRegistery = LocateRegistry.getRegistry(serverEntry.getIp(),
                                        serverEntry.portNumber + 10000);
                                VisualizerToSlaveCommunication slaveCommunication =
                                        (VisualizerToSlaveCommunication) slaveRegistery.lookup(
                                                "VisualizerToSlaveCommunication");
                                List<MailBoxEntry> mailBoxEntries = slaveCommunication.getAllMailBoxEntries();
                                //MailBoxEntry
                                //   slave.setMailBoxes(mailBoxEntries);
                                for (int i = 0; i < (mailBoxEntries.size()); i++) {
                                    slave.addMessage(mailBoxEntries.get(i));
                                }
                            } catch (RemoteException | NotBoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (RemoteException | NotBoundException e) {
                        e.printStackTrace();
                    }
                    threadListener.onUpdate(master);
                    System.out.println("abc");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
