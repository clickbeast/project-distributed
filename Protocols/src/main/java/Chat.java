import java.rmi.Remote;

public interface Chat extends Remote {
    boolean sendMessage(int boxNumber, String tag, String message);

    String getMessage(int boxNumber, String tag);

}
