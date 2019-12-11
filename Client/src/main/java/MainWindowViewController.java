
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainWindowViewController implements Initializable {

    public Scene scene;
    public Stage stage;


    //handles the main application


    /**
        VARIABLES -  ELEMENTS
     */

    //Left Toolbar
    public Button newConversationButton;


    public Label partnerNameLabel;

    public Label rightStatusLabel;
    public Label leftStatusLabel;

    public Button editButton;
    public Button getKeyButton;
    public Button sendButton;
    public Button deleteButton;


    public Button loginButton;
    public Button createAccountButton;



    public TextArea messageField;
    public TextField usernameField;
    public PasswordField passwordField;

    public TextField createAccountUsernameField;
    public PasswordField createAccountPasswordField;


    public AnchorPane leftPane;
    public AnchorPane inboxPane;
    public AnchorPane messagePane;
    private ClientManager clientManager;


    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing...");
        //this.configureUIElements();

    }


    public void setupComplete() {
        this.configureUIElements();
    }


    //Configures all Elements when starting the application_
    public void configureUIElements() {
        System.out.println("Configuring UI Elements");
        leftPane.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // TODO : Show visual feedback in left side
                System.out.println("feedback");
            }
        });
        leftPane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    System.out.println(db.getFiles().toString());
                    success = true;
                }

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


    public void callBackIndicateReceivedMessage() {
        //indicate that a message has been received on the new message
    }

    public void callBackReceivedMessage(int id) {

        //check if message is on foreground
    }

    /**
     * UI ACTIONS
     *
     */




    public void addNewConversation() {
        TextInputDialog dialog = new TextInputDialog("walter");
        dialog.setTitle("New conversation");
        dialog.setHeaderText("Enter a name for your partner. After creation choose a location for the key and send it" +
                "deliver it to your partner");
        dialog.setContentText("Please enter a name for your partner:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            System.out.println("Your name: " + result.get());
        }

        // The Java 8 way to get the response value (with lambda expression).
        result.ifPresent(name -> {
            File location = this.chooseDirectoryLocation();
            this.clientManager.addNewConversation(name, location);
            this.setLeftStatus("Creating conversation");

        });

    }

    public void showConversation(int id) {

    }

    public void editConversation() {
        String currentPartnerName = "";
        System.out.println("Edit conversation");
        TextInputDialog dialog = new TextInputDialog(currentPartnerName);
        dialog.setTitle("Edit name");
        dialog.setContentText("Please enter your partner name:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();

        // The Java 8 way to get the response value (with lambda expression).
        result.ifPresent(name -> System.out.println("Your name: " + name));
    }

    public void getKeyForConversation (){
        System.out.println("get key");
        File directoryLocation = this.chooseDirectoryLocation();
        int id = 0;
        this.clientManager.getKeyForConversation(id, directoryLocation);
    }

    public void sendMessage() {
        int id = 0;
        this.clientManager.sendMessage(id, messageField.getText());
        //TODO: show message in list
        //TODO: highlight when send/deleiviry is complete
    }

    public File chooseDirectoryLocation() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("JavaFX Projects");
        File selectedDirectory = chooser.showDialog(this.scene.getWindow());


        return selectedDirectory;
    }


    //Temp
    @FXML
    void onOpenDialog(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newMessageDialog.fxml"));
        Parent parent = fxmlLoader.load();
        NewMessageDialogController dialogController = fxmlLoader.<NewMessageDialogController>getController();
        //ialogController.setAppMainObservableList(tvObservableList);

        Scene scene = new Scene(parent, 300, 200);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void createAccount() {
        File directoryLocation = this.chooseDirectoryLocation();
        this.clientManager.createAccount(createAccountUsernameField.getText(), createAccountPasswordField.getText(), directoryLocation);
    }

    public void deleteConversation() {
        int id = 0;


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

    public void setLeftStatus(String status) {
        this.leftStatusLabel.setText(status);
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

}
