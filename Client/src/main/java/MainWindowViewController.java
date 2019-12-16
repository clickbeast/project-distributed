
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.AnchorPane;


import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainWindowViewController implements Initializable {

    public Scene scene;

    //handles the main application
    private ClientManager clientManager;


    /**
        VARIABLES -  ELEMENTS
     */

    //Left Toolbar
    public Button newConversationButton;


    //Right Toolbar
    public Label partnerNameLabel;
    public Button editButton;
    public Button getKeyButton;
    public Button sendButton;
    public TextArea messageField;


    //Left panel
    public AnchorPane leftPanel;
    //Right panel
    public AnchorPane rightPanel;





    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing...");
        this.configureUIElements();

    }



    //Configures all Elements when starting the application_
    public void configureUIElements() {
        System.out.println("Configuring UI Elements");
        leftPanel.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // TODO : Show visual feedback in left side
                System.out.println("feedback");
            }
        });
        leftPanel.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    System.out.println(db.getFiles().toString());
                    success = true;
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });


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


    public void addNewConversation() {

    }


    public void editConversation() {


    }


    public void getKeyForConversation (){

    }

    public void deleteConversation() {
        int id;


        Optional<ButtonType> result = new Alert(
                Alert.AlertType.INFORMATION,
                "No more instuctions Left. Do you wish to restart?",
                ButtonType.NO, ButtonType.YES
        ).showAndWait();



        if(result.get() == ButtonType.YES) {
            System.out.println("Resetting everything" + "");

            //delete conversation


        }else{
            //do nothing
            System.out.println("NO CALLED");
        }
    }



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
     *  UI UTIL
     */

    public void showFileSafeDialog() {
        //TODO

    }

}
