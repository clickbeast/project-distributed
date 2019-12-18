import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Ping extends Remote {
    boolean ping() throws RemoteException;
    void kill() throws RemoteException;
}
