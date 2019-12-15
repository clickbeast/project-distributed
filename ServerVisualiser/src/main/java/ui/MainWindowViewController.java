package ui;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.StateManager;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowViewController implements Initializable {

    public Scene scene;
    public Stage stage;

    public StateManager stateManager;


    /* VARIABLES ------------------------------------------------------------------ */



    /* UI SETUP ------------------------------------------------------------------ */


    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing...");

    }

    public void setupComplete() {

    }

    public void loadServerView() {


    }

    public void loadMasterView() {


    }

    public void loadMasterWatcherView() {

    }



    /* UI ACTIONS ------------------------------------------------------------------ */





    /*
     * GETTERS & SETTERS
     * -----------------------------------------------------------------------------
     *
     */


    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }
}
