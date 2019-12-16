import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Chat extends Remote, ClientToMasterCommunication {
    boolean sendMessage(int boxNumber, String tag, String message) throws RemoteException;

    String getMessage(int boxNumber, String tag) throws RemoteException;

    int getLimit() throws RemoteException;
}
