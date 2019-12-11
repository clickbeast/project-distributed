import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static Chat chat;

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 5000);
        chat = (Chat) registry.lookup("Chat");

        System.out.println(chat.sendMessage(0, "abc", "123"));
        System.out.println(chat.getMessage(0, "abc"));
    }
}
