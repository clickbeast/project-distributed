import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Scanner;

public class MasterServer extends UnicastRemoteObject implements SlaveToMasterCommunication,ClientToMasterCommunication{
    private int currentPort;
    //used to execute commands
    private Runtime command;
    private LinkedList<SlaveServer> slaves;
    private int currentNumberOfMailboxes;

    public void run(){
        createNewServer();
        createNewServer();
        createNewServer();
    }

    public MasterServer() throws IOException {
        System.out.println("[MASTER] Master server started");
        currentPort = 9001;
        slaves = new LinkedList<>();
        command = Runtime.getRuntime();
        currentNumberOfMailboxes = 0;
    }

    public void createNewServer(){
        synchronized (this) {
            makeNewSlave(Main.NUMBER_OF_MAILBOXES_PER_SLAVE, currentNumberOfMailboxes);
            currentNumberOfMailboxes += Main.NUMBER_OF_MAILBOXES_PER_SLAVE;
        }
    }

    private void makeNewSlave(int numberOfMailBoxes, int baseMailbox){
//        System.out.println("[MASTER] Spawning new server.");
        startSlave(true, numberOfMailBoxes, baseMailbox);
//        System.out.println("[MASTER] New server active.");
    }

    private static void startSlave(boolean watch, int numberOfMailboxes, int baseMailbox) {
        ProcessBuilder pb = new ProcessBuilder(
                "java",
                "-jar",
                "SlaveServer.jar",
                Integer.toString(numberOfMailboxes),
                Integer.toString(baseMailbox));

        pb.directory(new File(System.getProperty("user.dir") + "/MasterServer/SlaveServer"));
        try {
            Process p = pb.start();
            if(watch)
                watch(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void watch(final Process process) {
        new Thread() {
            public void run() {
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    while ((line = input.readLine()) != null) {
                        System.out.println("\t" + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static String makeCommand(String command){
        String cmd = "";
        if(Main.ON_WINDOWS){
            cmd += "cmd.exe /c ";
        }
        cmd += command;
        return cmd;
    }

    @Override
    public String getServerWithMailbox(int mailbox) throws RemoteException {
        SlaveServer slaveServer;

        synchronized (slaves) {
            for (SlaveServer slave : slaves) {
                if(slave.containtsMailbox(mailbox))
                    return slave.getAddress();
            }
        }

        return null;
    }

    @Override
    public int getPort() throws RemoteException{
        int ret = currentPort;
        currentPort += 2;
        System.out.println("[MASTER] Port number " + ret + " handed out.");
        return ret;
    }

    @Override
    public boolean confirmConfiguration(int portNumber, String ip, int startMailbox, int endMailbox) throws RemoteException {
        Scanner sc = new Scanner(ip);
        sc.useDelimiter("/");
        sc.next();
        ip = sc.next();

        System.out.println("[MASTER] Confirmation received for port: " + portNumber + " and IP-address: " + ip);
        try{
            slaves.add(new SlaveServer(portNumber, ip, startMailbox, endMailbox));
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
