import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SlaveServer {
    private MasterToSlaveCommunication toSlave;
    private int portNumber;
    private String ip;
    private int startMailbox,endMailbox;

    public SlaveServer(int portNumber, String ip, int startMailbox, int endMailbox) throws RemoteException, NotBoundException {
        this.portNumber = portNumber;
        this.ip = ip;
        this.startMailbox = startMailbox;
        this.endMailbox = endMailbox;

//        System.out.println("[MASTER] Trying to make RMI connection to " + ip + ":" + portNumber);
        Registry registry = LocateRegistry.getRegistry(ip, portNumber);
        this.toSlave = (MasterToSlaveCommunication) registry.lookup("MasterToSlaveCommunication");
        System.out.println("[MASTER] New slave connected on port " + portNumber);

        if(this.toSlave.ping())
            System.out.println("[MASTER] ping to " + portNumber + " succesfull.");
        else
            System.out.println("[MASTER] ping to " + portNumber + " failed.");
    }

    public boolean containtsMailbox(int mailbox){
        if (startMailbox <= mailbox && endMailbox >= mailbox)
            return true;
        return false;
    }

    public String getAddress(){
        String address = "";

        address += ip;
        address += ":";
        address += portNumber;

        return address;
    }

    public MasterToSlaveCommunication communication(){
        return this.toSlave;
    }
}
