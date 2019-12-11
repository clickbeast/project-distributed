import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class MasterServer extends UnicastRemoteObject implements SlaveToMasterCommunication {
    private int currentPort;
    private LinkedList<SlaveServer> slaves;

    public MasterServer() throws RemoteException {
        currentPort = 9001;
        slaves = new LinkedList<>();
        System.out.println("Master server started");
    }

    @Override
    public int getPort() {
        try {
            slaves.add(new SlaveServer(currentPort));
            return currentPort++;
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
