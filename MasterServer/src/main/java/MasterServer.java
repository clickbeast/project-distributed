import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Scanner;

public class MasterServer extends UnicastRemoteObject implements SlaveToMasterCommunication {
    private int currentPort;
    //used to execute commands
    private Runtime command;
    private LinkedList<SlaveServer> slaves;

    public MasterServer() throws IOException {
        System.out.println("[MASTER] Master server started");
        currentPort = 9001;
        slaves = new LinkedList<>();
        command = Runtime.getRuntime();

        makeNewSlave(0);
    }

    private void makeNewSlave(int numberOfMailBoxes){
        System.out.println("[MASTER] Spawning new server.");
        executeCommand("java -jar SlaveServer\\SlaveServer.jar", true);
        System.out.println("[MASTER] New server active.");
    }

    //executes a command and prints the results in the console
    private void executeCommand(String command, boolean waitUntilFinished){
        command = makeCommand(command);

        final Process process;
        try {
            process = this.command.exec(command);
            watch(process);

            if(waitUntilFinished)
                process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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
                        System.out.println("[MASTER] " + line);
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
    public int getPort() throws RemoteException{
        int ret = currentPort;
        currentPort += 2;
        System.out.println("[MASTER] Port number " + ret + " handed out.");
        return ret;
    }

    @Override
    public boolean confirmConfiguration(int portNumber, String ip) throws RemoteException {
        Scanner sc = new Scanner(ip);
        sc.useDelimiter("/");
        sc.next();
        ip = sc.next();

        System.out.println("[MASTER] Confirmation received for port: " + portNumber + " and IP-address: " + ip);
        try{
            slaves.add(new SlaveServer(portNumber, ip));
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
