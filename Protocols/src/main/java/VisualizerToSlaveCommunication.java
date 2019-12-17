import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VisualizerToSlaveCommunication extends Remote {
    List<MailBoxEntry> getAllMailBoxEntries() throws RemoteException;
}
