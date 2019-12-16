import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface SlaveToMasterCommunication extends Remote {
    int getPort() throws RemoteException;
    LinkedList<ServerEntry> confirmConfiguration(int portNumber, String ip, int startMailbox, int endMailbox) throws RemoteException;
    boolean ping() throws RemoteException;
}
