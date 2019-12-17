import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static PrintWriter writer;
    public static boolean LOG = true;

    public static int PORT_NUMBER;
    public static int PORT_FOR_PINGING_TO_SLAVE;

    public static void main(String[] args){
        PORT_NUMBER = Integer.parseInt(args[0]);
        PORT_FOR_PINGING_TO_SLAVE = Integer.parseInt(args[1]);

        BackupServer backupServer = new BackupServer();

        try {
            Registry registry = LocateRegistry.createRegistry(PORT_NUMBER);
            registry.rebind("SlaveToBackupCommunication", backupServer);

            Registry pingRegistry = LocateRegistry.getRegistry(PORT_FOR_PINGING_TO_SLAVE);
            Ping ping = (Ping) pingRegistry.lookup("Ping");

            backupServer.setPingToSlave(ping);
        } catch (RemoteException e){
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }


    }

    public static void restartSlave(){
        
    }

    public static void print(String string) {
        System.out.println(string);
        LOG(string);
    }

    public static void printError(String string) {
        System.out.println(string);
        LOG(string);
    }

    public static void LOG(String string) {
        synchronized (writer) {
            if (LOG) {
                writer.println(string);
            }
        }
    }
}
