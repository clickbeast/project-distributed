import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class StateManager {

    private MainWindowViewController mainWindowViewController;


    /* MODEL ------------------------------------------------------------------ */

    private Master master;

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
