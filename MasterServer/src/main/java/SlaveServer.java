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

        ServerEntry entry = new ServerEntry(startMailbox,endMailbox,ip,portNumber);
        synchronized (MasterServer.entries) {
            MasterServer.entries.add(entry);
        }

//        Main.print("[MASTER] Trying to make RMI connection to " + ip + ":" + portNumber);
        Registry registry = LocateRegistry.getRegistry(ip, portNumber);
        this.toSlave = (MasterToSlaveCommunication) registry.lookup("MasterToSlaveCommunication");
        Main.print("[MASTER] New slave connected on port " + portNumber);

        if(this.toSlave.ping())
            Main.print("[MASTER] ping to " + portNumber + " succesfull.");
        else
            Main.print("[MASTER] ping to " + portNumber + " failed.");
    }

    public void reconnect(){
        boolean reconnected = false;

        while (!reconnected){
            try{
                Registry registry = LocateRegistry.getRegistry(ip,portNumber);
                this.toSlave = (MasterToSlaveCommunication) registry.lookup("MasterToSlaveCommunication");
            } catch (RemoteException | NotBoundException e){
                e.printStackTrace();
            }
        }
    }

    public int getPortNumber() {
        return portNumber;
    }

    public boolean containtsMailbox(int mailbox){
        if (startMailbox <= mailbox && endMailbox >= mailbox)
            return true;
        return false;
    }

    public String getAddress(){
        String address = "";

        address += (ip+1);
        address += ":";
        address += portNumber;

        return address;
    }

    public MasterToSlaveCommunication communication(){
        return this.toSlave;
    }
}
