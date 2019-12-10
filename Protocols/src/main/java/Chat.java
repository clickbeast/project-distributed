
public interface Chat {
    boolean sendMessage(int boxNumber, String tag, String message);

    String getMessage(int boxNumber, String tag);

}
