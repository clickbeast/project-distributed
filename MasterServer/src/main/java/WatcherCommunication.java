import java.io.File;
import java.io.IOException;

public class WatcherCommunication implements Runnable{
    @Override
    public void run() {
        //Iedere seconde pingen naar de watcher.
        //Indien offline -> herstarten
        while(true){
            try {
                wait(500);
                if(!Main.CONNECTION_TO_WATCHER.ping())
                    restartWatcher();
            }
            catch (Exception e){
                Main.printError(e.toString());
            }
        }
    }

    private void restartWatcher(){
        ProcessBuilder pb;

        pb = new ProcessBuilder(
                "java",
                "-jar",
                "Watcher.jar",
                "restart");

        pb.directory(new File(System.getProperty("user.dir") + "/MasterServer/Watcher"));
        try {
            Process p = pb.start();
            MasterServer.watch(p);
        } catch (IOException e) {
            Main.printError(e.toString());
        }
    }
}
