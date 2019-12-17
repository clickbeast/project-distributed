import java.rmi.RemoteException;
import java.util.LinkedList;

public class BackupServer implements SlaveToBackupCommunication{
    public static Ping PING_TO_SLAVE;

    public void setPingToSlave(Ping pingToSlave) {
        this.PING_TO_SLAVE = pingToSlave;
    }

    public LinkedList<ServerEntry> getSlaveList() throws RemoteException {
        return null;
    }
}
