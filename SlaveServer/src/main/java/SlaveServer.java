import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SlaveServer extends UnicastRemoteObject implements MasterToSlaveCommunication {
    private BulletinBoard bulletinBoard;
    private SlaveToMasterCommunication toMaster;

    public SlaveServer(int portNumber, SlaveToMasterCommunication toMaster) throws RemoteException {
        bulletinBoard = new BulletinBoard();
        Registry registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind("Chat", bulletinBoard);
        this.toMaster = toMaster;
        System.out.println("Slave server created on port " + portNumber);
    }

    @Override
    public boolean ping() {
        return true;
    }
}
