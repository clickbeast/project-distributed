import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SlaveToMasterCommunication extends Remote {
    int getPort() throws RemoteException;
    boolean confirmPort(int portNumber) throws RemoteException;
}
