import sun.awt.image.ImageWatched;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Scanner;

public class MasterServer extends UnicastRemoteObject implements SlaveToMasterCommunication,ClientToMasterCommunication,Ping{
    private int currentPort;
    //used to execute commands
    private Runtime command;
    public static LinkedList<SlaveServer> slaves;
    public static LinkedList<ServerEntry> entries;
    private int currentNumberOfMailboxes;

    public void run(int numberOfSlaves){
        initiateSlaves(numberOfSlaves);
        SlaveWatcher slaveWatcher = new SlaveWatcher();
        slaveWatcher.run();
    }

    private void initiateSlaves(int numberOfSlaves){
        for(int i=0;i<numberOfSlaves;i++)
            createNewServer();
    }

    public MasterServer() throws IOException {
        Main.print("[MASTER] Master server started");
        currentPort = 9001;
        slaves = new LinkedList<>();
        entries = new LinkedList<>();
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
//        print("[MASTER] Spawning new server.");
        startSlave(true, numberOfMailBoxes, baseMailbox);
//        print("[MASTER] New server active.");
    }

    private static void startSlave(boolean watch, int numberOfMailboxes, int baseMailbox) {
        ProcessBuilder pb = new ProcessBuilder(
                "java",
                "-jar",
                "SlaveServer.jar",
                Integer.toString(numberOfMailboxes),
                Integer.toString(baseMailbox));

        pb.directory(new File(Main.PATH_TO_SLAVE_JAR));
        try {
            Process p = pb.start();
            if(watch)
                watch(p);
        } catch (IOException e) {
            Main.printError(e.toString());
        }
    }

    public static void watch(final Process process) {
        new Thread() {
            public void run() {
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    while ((line = input.readLine()) != null) {
                        Main.print("\t" + line);
                    }
                } catch (IOException e) {
                    Main.printError(e.toString());
                }
            }
        }.start();
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
        Main.print("[MASTER] Port number " + ret + " handed out.");
        return ret;
    }

    @Override
    public LinkedList<ServerEntry> confirmConfiguration(int port, String ip, int startMailbox, int endMailbox) throws RemoteException {
        Scanner sc = new Scanner(ip);
        sc.useDelimiter("/");
        sc.next();
        ip = sc.next();

        Main.print("[MASTER] Confirmation received for port: " + port + " and IP-address: " + ip + " for range [" + startMailbox + "," + endMailbox + "]");
        try{
            synchronized (slaves) {
                slaves.removeIf(slaveServer -> slaveServer.getPortNumber() == port);
                entries.removeIf(serverEntry -> serverEntry.getPortNumber() == port);
                slaves.add(new SlaveServer(port, ip, startMailbox, endMailbox));

                synchronized (entries) {
                    for (SlaveServer server : slaves) {
                        server.communication().sendServerList(entries);
                    }
                }
            }
        }
        catch (Exception e){
            Main.printError(e.toString());
            return null;
        }
        return entries;
    }

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }
}
