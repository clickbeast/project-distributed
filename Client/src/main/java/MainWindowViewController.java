
import javafx.fxml.Initializable;
import javafx.scene.Scene;


import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowViewController implements Initializable {

    public Scene scene;

    //handles the main application
    private ClientManager clientManager;


    /**
        VARIABLES -  ELEMENTS
     */




    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing...");
        this.configureUIElements();

    }



    //Configures all Elements when starting the application_
    public void configureUIElements() {
        System.out.println("Configuring UI Elements");


    }





    //get called when Main is finshed with doing its things
    public void startFinished() {
        System.out.println("Finished setup of program");

    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }



    /**
     * UI ACTIONS
     *
     */





    /**
     *
     * UI CONFIG
     *
     */






    /**
     *
     * UI FILL
     *
     */


    //changes the content being shown by the UI
    public void updateUIBasedOnNewState(UIState uiState) {



    }


    /**
     *
     * UI CHANGES
     *
     *
     */



    //prevent user from using History, commonly used while running algorithm
    public void freezeUI() {



    }

    //allow user to use History again
    public void unFreezeUI() {

    }


    /**
     *  Alerts
     */


}
