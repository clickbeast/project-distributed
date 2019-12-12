import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class MasterServer extends UnicastRemoteObject implements SlaveToMasterCommunication {
    private int currentPort;
    private int currentIp;
    //used to execute commands
    private Runtime command;
    private LinkedList<SlaveServer> slaves;
    private static String dockerImageName = "slave-server";

    public MasterServer() throws IOException {
        currentPort = 9001;
        currentIp = 1;
        slaves = new LinkedList<>();
        command = Runtime.getRuntime();
        System.out.println("Master server started");

        BuildDockerImageOfSlave();
        makeNewSlave(0);
    }

    private void makeNewSlave(int numberOfMailBoxes){
        System.out.println("Spawning new docker image.");
        executeCommand("docker run --add-host slave-server:10.0.0." + currentIp++ + " slave-server", false);
        System.out.println("New Docker image active.");
    }

    private void BuildDockerImageOfSlave() throws IOException {
        System.out.println("Starting to build new Docker image.");
        executeCommand("docker build -t " + dockerImageName + " .", true);
        System.out.println("Docker image built succesfully.");
    }

    //executes a command and prints the results in the console
    private void executeCommand(String command, boolean waitUntilFinished){
        command = makeCommand(command);

        final Process process;
        try {
            process = this.command.exec(command, null, new File(Main.ROOT_PATH + "\\Docker"));
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
                        System.out.println(line);
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
        System.out.println("Port number " + ret + " handed out.");
        return ret;
    }

    @Override
    public boolean confirmConfiguration(int portNumber, String ip) throws RemoteException {
        System.out.println("Confirmation received for port: " + portNumber + " and IP-address: " + ip);
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
