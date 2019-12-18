import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class KillManager {

    public void killMaster(Master master){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Registry registry = LocateRegistry.getRegistry("localhost", 8998);
                    Ping ping = (Ping) registry.lookup("Ping");
                    ping.kill();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void killSlave(Slave slave){
        Runnable runnable =  new Runnable() {
            @Override
            public void run() {
                try{
                    Registry registry = LocateRegistry.getRegistry(slave.getName().split(":")[0], Integer.parseInt(slave.getName().split(":")[1]));
                    Ping ping = (Ping) registry.lookup("Ping");
                    ping.kill();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void killMasterWatcher(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    Registry pingToWatcher = LocateRegistry.getRegistry("localhost", 7000);
                    Ping ping = (Ping) pingToWatcher.lookup("Ping");
                    ping.kill();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
