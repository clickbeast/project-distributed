import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class SlaveServer extends UnicastRemoteObject implements MasterToSlaveCommunication,Ping {
    private BulletinBoard bulletinBoard;
    public static SlaveToMasterCommunication toMaster;
    public static int portNumber;
    public static boolean backup;

    public SlaveServer(int portNumber, SlaveToMasterCommunication toMaster) throws RemoteException {
        bulletinBoard = new BulletinBoard(backup);
        Registry registry = LocateRegistry.createRegistry(portNumber+1);
        registry.rebind("Chat", bulletinBoard);
        this.toMaster = toMaster;
        SlaveServer.portNumber = portNumber;

        Main.print("[SLAVE: " + portNumber + "] " +
                "Bulletin board with " + Main.NUMBER_OF_MAILBOXES +
                " mailboxes [" + Main.BASE_MAILBOX + "," +
                (Main.BASE_MAILBOX + Main.NUMBER_OF_MAILBOXES - 1) +
                "] created on port " + (portNumber+1));
    }

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }

    @Override
    public void sendServerList(LinkedList<ServerEntry> entries) throws RemoteException {
//        synchronized (Main.entries){
            Main.entries = entries;
//        }
        Main.printMailboxes();
    }
}
