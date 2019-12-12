
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Conversation;
import model.Message;


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
    public Button deleteButton;
    public Button sendButton;


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


    public HBox leftToolBarConversation;
    public HBox rightToolBarConversation;

    public ListView<Message> messageListView;
    public ListView<Conversation> inboxListView;


    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing...");
        //this.configureUIElements();

    }


    public void setupComplete() {
        this.configureUIElements();
        //this.clientManager.loadUserContents();
        this.setupDefaultToolbarConversation();
        this.clientManager.loadUserContents();
    }

    public void setupDefaultToolbarConversation() {
        this.editButton = new Button();
        this.getKeyButton = new Button();
        this.deleteButton = new Button();

        this.editButton.setText("Edit");
        this.getKeyButton.setText("key");
        this.deleteButton.setText("Delete");

        this.deleteButton.setOnAction(e->this.deleteConversation());
        this.editButton.setOnAction(e->this.editPartnerName());
        this.getKeyButton.setOnAction(e->this.getKeyForConversation());

        this.partnerNameLabel = new Label();
        this.partnerNameLabel.setText("loading");
        this.partnerNameLabel.setPadding(new Insets(4, 0, 0, 0));
        this.partnerNameLabel.setStyle("-fx-font-weight: bold");

        this.leftToolBarConversation.getChildren().add(this.partnerNameLabel);
        this.rightToolBarConversation.getChildren().clear();
        this.rightToolBarConversation.getChildren().addAll(editButton, getKeyButton, deleteButton);
        this.rightToolBarConversation.setSpacing(5.0);
    }


    public void editPartnerName() {
        if(clientManager.getCurrentConversation() != null) {
            TextField field = new TextField();
            field.setText(this.partnerNameLabel.getText());
            Button button = new Button();
            button.setText("Done");
            button.setDefaultButton(true);
            button.setOnAction(e->{
                this.clientManager.editPartnerName(field.getText());
                this.hideInlineDialog();

            });
            this.inlineDialog(field,button);
        }
    }

    public void deleteConversation() {
        if(clientManager.getCurrentConversation() != null) {
            Label label = new Label();
            label.setText("Are you sure you want to delete this conversation?");
            Button deleteButton = new Button();
            deleteButton.setText("Delete");
            deleteButton.setDefaultButton(true);
            deleteButton.setOnAction(e -> {
                this.clientManager.deleteConversation(clientManager.getCurrentConversation());
                this.hideInlineDialog();
                this.showEmptyConversation();
            });

            this.inlineDialog(label, deleteButton);
        }
    }

    public void inlineDialog(Node node, Button actionButton) {
        this.leftToolBarConversation.getChildren().clear();
        this.rightToolBarConversation.getChildren().clear();
        this.leftToolBarConversation.getChildren().add(node);

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(e-> this.hideInlineDialog());

        rightToolBarConversation.getChildren().add(cancelButton);
        rightToolBarConversation.getChildren().add(actionButton);
    }

    public void hideInlineDialog() {
        this.leftToolBarConversation.getChildren().clear();
        this.rightToolBarConversation.getChildren().clear();
        this.leftToolBarConversation.getChildren().add(partnerNameLabel);
        rightToolBarConversation.getChildren().add(getKeyButton);
        rightToolBarConversation.getChildren().add(editButton);
        rightToolBarConversation.getChildren().add(deleteButton);
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


    public void loadInbox() {
        this.inboxPane.getChildren().removeAll();
        final ListView<Conversation> listView = new ListView<Conversation>(this.clientManager.getConversations());
        listView.setCellFactory(new Callback<ListView<Conversation>, ListCell<Conversation>>() {
            @Override
            public ListCell<Conversation> call(ListView<Conversation> listView) {
                return new ConversationListViewCell();
            }

        });
        
        this.inboxListView = listView;
        
        
        this.getInboxPane().getChildren().add(listView);


        //configure bounds
        AnchorPane.setBottomAnchor(inboxListView, 0.0);
        AnchorPane.setTopAnchor(inboxListView,0.0);
        AnchorPane.setRightAnchor(inboxListView,0.0);
        AnchorPane.setLeftAnchor(inboxListView,0.0);

    }


    public void loadConversation(Conversation conversation) {
        this.clientManager.setCurrentConversation(conversation);
        this.messagePane.getChildren().removeAll();

        this.deleteButton.setDisable(false);
        this.getKeyButton.setDisable(false);
        this.editButton.setDisable(false);


        this.partnerNameLabel.setText(this.clientManager.getCurrentConversation().getUserName());
        final ListView<Message> messageView = new ListView<Message>(this.clientManager.getCurrentConversation().getMessages());
        messageView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> listView) {
                return new MessageListViewCell();
            }
        });

        this.messageListView = messageView;



        this.getMessagePane().getChildren().add(messageView);
        AnchorPane.setBottomAnchor(messageListView, 0.0);
        AnchorPane.setTopAnchor(messageListView,0.0);
        AnchorPane.setRightAnchor(messageListView,0.0);
        AnchorPane.setLeftAnchor(messageListView,0.0);


    }

    public void showEmptyConversation() {
        this.clientManager.setCurrentConversation(null);
        this.messagePane.getChildren().removeAll();
        this.partnerNameLabel.setText("");
        this.deleteButton.setDisable(true);
        this.getKeyButton.setDisable(true);
        this.editButton.setDisable(true);
        this.getMessageField().setDisable(true);
        this.sendButton.setDisable(true);
    }


    private class ConversationListViewCell extends ListCell<Conversation> {
        private HBox content;
        private Text name;
        private Text message;

        public ConversationListViewCell() {
            super();
            name = new Text();
            message = new Text();
            VBox vBox = new VBox(name, message);
            content = new HBox(new Label("[Graphic]"), vBox);
            content.setSpacing(10);
        }

        //TODO: implement this;

        /*
            https://stackoverflow.com/questions/9722418/how-to-handle-listview-item-clicked-action
         */

        @Override
        protected void updateItem(Conversation item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) { // <== test for null item and empty parameter
                name.setText(item.getUserName());
                //get last message
                message.setText(item.getMessages().get(0).getText());
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }




    private class MessageListViewCell extends ListCell<Message> {
        private HBox content;
        private Text name;
        private Text message;

        public MessageListViewCell() {
            super();
            name = new Text();
            message = new Text();
            VBox vBox = new VBox(name, message);
            content = new HBox(new Label("[Graphic]"), vBox);
            content.setSpacing(10);
        }

        //TODO: implement this;

        /*
            https://stackoverflow.com/questions/9722418/how-to-handle-listview-item-clicked-action
         */

        @Override
        protected void updateItem(Message item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) { // <== test for null item and empty parameter
                name.setText(message.getText());
                //get last message
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
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
        this.clientManager.sendMessage(this.clientManager.currentConversation, messageField.getText());
    }

    public File chooseDirectoryLocation() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a directory");
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

/*
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
*/

    public void setLeftStatus(String status) {
        this.leftStatusLabel.setText(status);
    }

    /**
     *
     * UI CONFIG
     *
     */







    //prevent user from using History, commonly used while running algorithm
    public void freezeUI() {

    }

    //allow user to use History again
    public void unFreezeUI() {

    }


    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Button getNewConversationButton() {
        return newConversationButton;
    }

    public void setNewConversationButton(Button newConversationButton) {
        this.newConversationButton = newConversationButton;
    }

    public Label getPartnerNameLabel() {
        return partnerNameLabel;
    }

    public void setPartnerNameLabel(Label partnerNameLabel) {
        this.partnerNameLabel = partnerNameLabel;
    }

    public Label getRightStatusLabel() {
        return rightStatusLabel;
    }

    public void setRightStatusLabel(Label rightStatusLabel) {
        this.rightStatusLabel = rightStatusLabel;
    }

    public Label getLeftStatusLabel() {
        return leftStatusLabel;
    }

    public void setLeftStatusLabel(Label leftStatusLabel) {
        this.leftStatusLabel = leftStatusLabel;
    }

    public Button getEditButton() {
        return editButton;
    }

    public void setEditButton(Button editButton) {
        this.editButton = editButton;
    }

    public Button getGetKeyButton() {
        return getKeyButton;
    }

    public void setGetKeyButton(Button getKeyButton) {
        this.getKeyButton = getKeyButton;
    }

    public Button getSendButton() {
        return sendButton;
    }

    public void setSendButton(Button sendButton) {
        this.sendButton = sendButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(Button loginButton) {
        this.loginButton = loginButton;
    }

    public Button getCreateAccountButton() {
        return createAccountButton;
    }

    public void setCreateAccountButton(Button createAccountButton) {
        this.createAccountButton = createAccountButton;
    }

    public TextArea getMessageField() {
        return messageField;
    }

    public void setMessageField(TextArea messageField) {
        this.messageField = messageField;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public void setUsernameField(TextField usernameField) {
        this.usernameField = usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(PasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public TextField getCreateAccountUsernameField() {
        return createAccountUsernameField;
    }

    public void setCreateAccountUsernameField(TextField createAccountUsernameField) {
        this.createAccountUsernameField = createAccountUsernameField;
    }

    public PasswordField getCreateAccountPasswordField() {
        return createAccountPasswordField;
    }

    public void setCreateAccountPasswordField(PasswordField createAccountPasswordField) {
        this.createAccountPasswordField = createAccountPasswordField;
    }

    public AnchorPane getLeftPane() {
        return leftPane;
    }

    public void setLeftPane(AnchorPane leftPane) {
        this.leftPane = leftPane;
    }

    public AnchorPane getInboxPane() {
        return inboxPane;
    }

    public void setInboxPane(AnchorPane inboxPane) {
        this.inboxPane = inboxPane;
    }

    public AnchorPane getMessagePane() {
        return messagePane;
    }

    public void setMessagePane(AnchorPane messagePane) {
        this.messagePane = messagePane;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }
}
