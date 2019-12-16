import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main{
    public static Ping PING_TO_MASTER;
    public static String IP_OF_MASTER = "localhost";
    public static int PORT_FOR_PINGING_TO_MASTER= 8998;
    public static int PORT_FOR_PINGING_TO_WATCHER = 7000;

    public static void main(String[] args) throws RemoteException {
        print("[WATCHER] started");
        MasterCommunication masterCommunication = new MasterCommunication();

        Registry registry = LocateRegistry.createRegistry(PORT_FOR_PINGING_TO_WATCHER);
        registry.rebind("Ping", masterCommunication);


        if(args.length==0){
            //this means the watcher was started
            // to startup the entire system
            startMaster(true);
        }

        masterCommunication.setup();
        masterCommunication.run();
    }

    public static void printError(String string){
        System.err.println(string);
    }

    public static void print(String string){
        System.out.println(string);
    }

    public static void startMaster(boolean startEntireSystem) {
        ProcessBuilder pb;
        print("[WATCHER] starting master server...");
        if(startEntireSystem)
            pb = new ProcessBuilder("/bin/bash", "MasterServer.sh");
        else
            pb = new ProcessBuilder("/bin/bash", "MasterServer.sh", "restart");

        pb.directory(new File("/home/adegeter/Documents/school/project-distributed/Watcher/MasterServer"));
        try {
            Process p = pb.start();
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
}
