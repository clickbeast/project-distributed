package model;

import ui.MainWindowViewController;

import java.util.List;

public class StateManager {

    private MainWindowViewController mainWindowViewController;


    /* MODEL ------------------------------------------------------------------ */

    private Master master;
    private MasterWatcher masterWatcher;
    private List<Slave> slaves;


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



}
