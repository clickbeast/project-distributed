import java.rmi.RemoteException;

public interface ClientToMasterCommunication {
    String getServerWithMailbox(int mailbox) throws RemoteException;
}
