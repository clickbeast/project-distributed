import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientToMasterCommunication extends Remote {
    String getServerWithMailbox(int mailbox) throws RemoteException;
}
