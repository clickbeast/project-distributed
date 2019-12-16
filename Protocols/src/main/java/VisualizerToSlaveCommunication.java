import java.rmi.Remote;
import java.util.List;

public interface VisualizerToSlaveCommunication extends Remote {
    List<MailBoxEntry> getAllMailBoxEntries();
}
