import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ServerInfoManager {

    private static final String MASTER_SERVER_IP = "localhost:7001";

    public void sendMessage(final ThreadListener threadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Registry registry = LocateRegistry.getRegistry(MASTER_SERVER_IP.split(":")[0],
                                Integer.parseInt(MASTER_SERVER_IP.split(":")[1]));

                        VisualizerToMasterCommunication visualizer =
                                (VisualizerToMasterCommunication) registry.lookup("Chat");

                    } catch (RemoteException | NotBoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
