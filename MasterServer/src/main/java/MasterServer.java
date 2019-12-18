import sun.awt.image.ImageWatched;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class MasterServer extends UnicastRemoteObject implements SlaveToMasterCommunication,ClientToMasterCommunication,Ping,VisualizerToMasterCommunication{
    private int currentPort;
    //used to execute commands
    private Runtime command;
    public static LinkedList<SlaveServer> slaves;
    public static LinkedList<ServerEntry> entries;
    private static Integer currentNumberOfMailboxes;
    private static Integer numberOfMessages;

    public void run(int numberOfSlaves){
        initiateSlaves(numberOfSlaves);
        Runnable slaveWatcher = new SlaveWatcher();
        Thread thread = new Thread(slaveWatcher);
        thread.start();

        if(Main.SELF_DESTRUCT) {
            Main.print("[MASTER] SHUTTING DOWN");
            System.exit(0);
        }
    }

    private void initiateSlaves(int numberOfSlaves){
        for(int i=0;i<numberOfSlaves;i++)
            createNewServer();
    }

    public MasterServer() throws IOException,RemoteException {
        Main.print("[MASTER] Master server started");
        currentPort = 9001;
        slaves = new LinkedList<>();
        entries = new LinkedList<>();
        command = Runtime.getRuntime();
        currentNumberOfMailboxes = 0;
        numberOfMessages = 0;
    }

    public void createNewServer(){
        synchronized (this) {
            makeNewSlave(Main.NUMBER_OF_MAILBOXES_PER_SLAVE, currentNumberOfMailboxes);
        }
    }

    public static void makeNewSlave(int numberOfMailBoxes, int baseMailbox){
        synchronized (currentNumberOfMailboxes) {
            currentNumberOfMailboxes+=numberOfMailBoxes;
        }
//        print("[MASTER] Spawning new server.");
        try {
            sleep(Main.WAIT_TIME_BETWEEN_SERVER_SPAWNS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startSlave(true, numberOfMailBoxes, baseMailbox);
//        print("[MASTER] New server active.");
    }

    public static void makeNewSlave(int numberOfMailBoxes, int baseMailbox, int portNumber){
        synchronized (currentNumberOfMailboxes){
            currentNumberOfMailboxes+=numberOfMailBoxes;
        }
//        print("[MASTER] Spawning new server.");
        try {
            sleep(Main.WAIT_TIME_BETWEEN_SERVER_SPAWNS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startSlave(true, numberOfMailBoxes, baseMailbox, portNumber);
//        print("[MASTER] New server active.");
    }


    private static void startSlave(boolean watch, int numberOfMailboxes, int baseMailbox, int portNumber) {

        ProcessBuilder pb = new ProcessBuilder(
                "/bin/bash",
                "SlaveServer.sh",
                Integer.toString(numberOfMailboxes),
                Integer.toString(baseMailbox),
                Integer.toString(portNumber));

        pb.directory(new File(Main.PATH_TO_SLAVE_JAR));
        try {
            Process p = pb.start();
            if(watch)
                watch(p);
        } catch (IOException e) {
            Main.printError(e.toString());
        }
    }

    private static void startSlave(boolean watch, int numberOfMailboxes, int baseMailbox) {
        ProcessBuilder pb = new ProcessBuilder(
                "/bin/bash",
                "SlaveServer.sh",
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
    public void kill() throws RemoteException {
        System.exit(0);
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
            try {
                synchronized (slaves) {
                    slaves.removeIf(slaveServer -> slaveServer.getPortNumber() == port);
                    entries.removeIf(serverEntry -> serverEntry.getPortNumber() == port);
                    slaves.add(new SlaveServer(port, ip, startMailbox, endMailbox));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Main.printError(e.toString());
                return null;
            }
        printMailboxes();
        return entries;
    }

    private static void printMailboxes() {
        Main.print("[MASTER]\t ============= ENTRIES =============");
        for (ServerEntry entry : entries) {
            Main.print("[MASTER]\t [" + entry.getStartMailbox() + "," + entry.getEndMailbox() + "] on " + entry.getIp() + ":" + entry.getPortNumber());
        }
        Main.print("[MASTER]\t ===================================");
    }

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }

    @Override
    public int getLimit() throws RemoteException {
        int maxMailbox = 0;

        synchronized (entries){
            for(ServerEntry entry : entries){
                if(entry.getEndMailbox() > maxMailbox)
                    maxMailbox = entry.getEndMailbox();
            }
        }
        return maxMailbox;
    }

    @Override
    public LinkedList<ServerEntry> getSlaveList() throws RemoteException {
        synchronized (entries) {
            return entries;
        }
    }

    @Override
    public void incrementAmountOfMessages() throws RemoteException {
        synchronized (numberOfMessages){
            numberOfMessages++;
            if(numberOfMessages > currentNumberOfMailboxes)
                makeNewSlave(Main.NUMBER_OF_MAILBOXES_PER_SLAVE, currentNumberOfMailboxes);
        }
    }

    @Override
    public void decrementAmountOfMessages() throws RemoteException {
        synchronized (numberOfMessages){
            numberOfMessages--;
        }
    }
}
