import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        Chat stub = (Chat) Naming.lookup("rmi://localhost:5000/sonoo");
        System.out.println(stub.sendMessage(0, "abc", "123"));
        System.out.println(stub.getMessage(0, "abc"));
    }
}
