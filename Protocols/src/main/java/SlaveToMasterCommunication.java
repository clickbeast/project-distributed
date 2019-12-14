import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SlaveToMasterCommunication extends Remote {
    int getPort() throws RemoteException;
    boolean confirmConfiguration(int portNumber, String ip, int startMailbox, int endMailbox) throws RemoteException;
}
