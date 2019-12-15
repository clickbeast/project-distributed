import javafx.collections.ObservableList;
import model.Master;
import model.MasterWatcher;
import model.Slave;

import java.util.List;

public class StateManager {

    private  MainWindowViewController mainWindowViewController;


    /* MODEL ------------------------------------------------------------------ */

    private Master master;
    private MasterWatcher masterWatcher;
    private ObservableList<Slave> slaves;


    /* SETUP ------------------------------------------------------------------ */


    public StateManager() {


    }


    //TODO: QUESTION SERVERS EVERY 10 SECONDS
    //TODO


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

    public MasterWatcher getMasterWatcher() {
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
    }
}
