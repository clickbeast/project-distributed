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
    public int getPort() throws RemoteException{
        int ret = currentPort;
        currentPort += 2;
        return ret;
    }

    @Override
    public boolean confirmPort(int portNumber) throws RemoteException {
        try{
            slaves.add(new SlaveServer(portNumber));
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
