import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class StateManager {

    private MainWindowViewController mainWindowViewController;


    /* MODEL ------------------------------------------------------------------ */

    private Master master;
    private KillManager killManager;

    /* SETUP ------------------------------------------------------------------ */

  /*  public Slave slaveDummy(String name) {
        return new Slave(3,1);
    }*/

    public StateManager() {
        // TEMP
        /*slaves = FXCollections.observableArrayList();
        slaves.add(this.slaveDummy("Slave 1"));
        slaves.add(this.slaveDummy("Slave 2"));
        slaves.add(this.slaveDummy("Slave 3"));*/

       /* Message message = new Message("1","hello");
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        Mailbox mailbox = new Mailbox(1,messages);
        Mailbox[] mailboxes =
        slaves.get(0).setMailboxes();*/

       killManager = new KillManager();
        ServerInfoManager serverInfoManage = new ServerInfoManager();
        this.master = new Master();
        ThreadListener lister = new ThreadListener() {
            @Override
            public void onUpdate(Master m) {
                System.out.println("yes");
                Platform.runLater(() -> mainWindowViewController.loadServerView());
            }
        };
        serverInfoManage.checkBoxes(master, lister);


    }

    /* UI ACTIONS ------------------------------------------------------------------ */
//TODO: @Andres
    public void killAllExceptMaster() {
        killMasterWatcher();
        for(Slave slave : master.slaves){
            killSlave(slave);
        }
    }

    //TODO: @Andres
    public void killAllExceptMasterWatcher() {
        killMaster();
        for(Slave slave : master.slaves){
            killSlave(slave);
        }
    }

    //TODO: @Andres
    public void killMaster() {
        killManager.killMaster(master);
    }

    //TODO: @Andres
    public void killMasterWatcher() {
        killManager.killMasterWatcher();
    }


    //TODO: @Andres
    public void killSlave(Slave slave) {
        killManager.killSlave(slave);
    }

    /*
     * GETTERS & SETTERS
     * -----------------------------------------------------------------------------
     *
     */

    public void setMainWindowViewController(MainWindowViewController mainWindowViewController) {
        this.mainWindowViewController = mainWindowViewController;
    }

    public MainWindowViewController getMainWindowViewController() {
        return mainWindowViewController;
    }

  public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

  /*  public MasterWatcher getMasterWatcher() {
        return masterWatcher;
    }

    public void setMasterWatcher(MasterWatcher masterWatcher) {
        this.masterWatcher = masterWatcher;
    }

    public ObservableList<Slave> getSlaves() {
        return slaves;
    }

    public void setSlaves(ObservableList<Slave> slaves) {
        this.slaves = slaves;
    }*/
}
