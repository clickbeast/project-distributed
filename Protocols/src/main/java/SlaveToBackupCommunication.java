import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface SlaveToBackupCommunication extends Remote {
    LinkedList<ServerEntry> getSlaveList() throws RemoteException;
}
