
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Conversation;
import model.Message;
import ui.ToolBarLabel;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowViewController implements Initializable {

    public Scene scene;
    public Stage stage;


    /* VARIABLES ------------------------------------------------------------------ */


    //Left Toolbar
    public Button newConversationButton;

    public Button logoutButton;

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

    public LoginViewController loginViewController;


    /* UI SETUP ------------------------------------------------------------------ */


    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing...");
        //this.configureUIElements();

    }

    public void setupComplete() {
        //this.clientManager.loadUserContents();
//        this.leftStatusLabel.setText("");
        //      this.rightStatusLabel.setText("");
        this.setupDefaultToolbarConversation();
        this.freezeUI();
        //this.clientManager.loadUserContents();
        //this.setupDefaultToolbarConversation();
        this.loadLoginView();
    }

    public void setupDefaultToolbarConversation() {
        this.editButton = new Button();
        this.getKeyButton = new Button();
        this.deleteButton = new Button();

        this.editButton.setText("Edit");
        this.getKeyButton.setText("key");
        this.deleteButton.setText("Delete");

        this.deleteButton.setOnAction(e -> this.deleteConversationAction());
        this.editButton.setOnAction(e -> this.editPartnerNameAction());
        this.getKeyButton.setOnAction(e -> this.getKeyForConversationAction());

        this.partnerNameLabel = new Label();
        this.partnerNameLabel.setText("loading");
        this.partnerNameLabel.setPadding(new Insets(4, 0, 0, 0));
        this.partnerNameLabel.setFont(Font.font("Helvetica", FontWeight.BLACK, 13.0));

        this.leftToolBarConversation.getChildren().add(this.partnerNameLabel);
        this.rightToolBarConversation.getChildren().clear();
        this.rightToolBarConversation.getChildren().addAll(editButton, getKeyButton, deleteButton);
        this.rightToolBarConversation.setSpacing(5.0);
    }

    public void reloadUI() {
        this.loadConversation(this.clientManager.getCurrentConversation());
        this.loadInbox();
    }

    public void freezeUI() {
        this.deleteButton.setDisable(true);
        this.getKeyButton.setDisable(true);
        this.editButton.setDisable(true);
        this.sendButton.setDisable(true);
        this.logoutButton.setDisable(true);
        this.newConversationButton.setDisable(true);
        this.partnerNameLabel.setText("");
        this.inboxPane.getChildren().clear();
        this.messagePane.getChildren().clear();
        this.messageField.setDisable(true);
    }


    /* VIEW LOADING ------------------------------------------------------------------ */


    public void loadLoginView() {
        this.inboxPane.getChildren().clear();
        this.messagePane.getChildren().clear();


        Parent parent = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginView.fxml"));
        loader.setControllerFactory(c -> {
            if (c == LoginViewController.class) {
                LoginViewController lv = new LoginViewController();
                lv.setMainWindowViewController(this);
                this.loginViewController = lv;
                return lv;
            } else {
                try {
                    return c.newInstance();
                } catch (Exception exc) {
                    throw new RuntimeException(exc);
                }
            }
        });

        try {
            parent = loader.load();
        } catch (IOException e) {
            System.out.println(e);
        }

        this.freezeUI();
        if (parent != null) {
            //configure bounds
            inboxPane.getChildren().add(parent);

            AnchorPane.setBottomAnchor(parent, 0.0);
            AnchorPane.setTopAnchor(parent, 0.0);
            AnchorPane.setRightAnchor(parent, 0.0);
            AnchorPane.setLeftAnchor(parent, 0.0);
        }

    }

    public void loadApplicationView() {
        this.newConversationButton.setDisable(false);
        this.logoutButton.setDisable(false);
    }


    public void loadInbox() {
        this.inboxPane.getChildren().removeAll();
        this.clientManager.sortConversationsAccordingToPolicy();
        final ListView<Conversation> listView = new ListView<Conversation>(this.clientManager.getConversations());
        listView.setCellFactory(new Callback<ListView<Conversation>, ListCell<Conversation>>() {
            @Override
            public ListCell<Conversation> call(ListView<Conversation> listView) {
                return new ConversationListViewCell(clientManager.getMainWindowViewController());
            }
        });

        this.inboxListView = listView;
        this.newConversationButton.setDisable(false);
        this.logoutButton.setDisable(false);


        this.getInboxPane().getChildren().add(listView);


        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Conversation>() {
            public void changed(ObservableValue<? extends Conversation> ov,
                                final Conversation oldvalue, final Conversation newvalue) {

                //TODO: IMPROVE (this is temp)
                System.out.println("Selected:" + newvalue);
                clientManager.setCurrentConversation(newvalue);
                clientManager.getMainWindowViewController().reloadUI();
            }
        });

        //configure bounds
        AnchorPane.setBottomAnchor(inboxListView, 0.0);
        AnchorPane.setTopAnchor(inboxListView, 0.0);
        AnchorPane.setRightAnchor(inboxListView, 0.0);
        AnchorPane.setLeftAnchor(inboxListView, 0.0);


    }

    public void loadConversation(Conversation conversation) {
        if (conversation == null) {
            System.out.println("No current conversation");
            this.loadEmptyConversation();
        }

        this.clientManager.setCurrentConversation(conversation);
        this.messagePane.getChildren().removeAll();

        this.deleteButton.setDisable(false);
        this.getKeyButton.setDisable(false);
        this.editButton.setDisable(false);
        this.sendButton.setDisable(false);
        this.messageField.setDisable(false);

        this.inboxListView.getSelectionModel().select(conversation);

        this.partnerNameLabel.setText(this.clientManager.getCurrentConversation().getUserName());
        final ListView<Message> messageView =
                new ListView<Message>(this.clientManager.getCurrentConversation().getMessages());
        messageView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> listView) {
                return new MessageListViewCell(clientManager.getMainWindowViewController());
            }
        });

        this.messageListView = messageView;

        messageField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                this.sendMessageAction();
            }
        });

        this.getMessagePane().getChildren().add(messageView);

        AnchorPane.setBottomAnchor(messageListView, 0.0);
        AnchorPane.setTopAnchor(messageListView, 0.0);
        AnchorPane.setRightAnchor(messageListView, 0.0);
        AnchorPane.setLeftAnchor(messageListView, 0.0);


    }

    public void createNewConversation() {
        this.loadEmptyConversation();
        TextField field = new TextField();
        field.setPromptText("Choose Parnter Name");
        Button create = new Button();
        create.setText("Create");
        create.setDefaultButton(true);
        create.setOnAction(e -> {
            this.hideInlineDialog();
            this.clientManager.createNewConversation(field.getText());
            ToolBarLabel label = new ToolBarLabel("Do you want to save the key for your partner?");
            Button getKey = new Button("Save key");
            getKey.setDefaultButton(true);
            getKey.setOnAction(a -> {
                this.getKeyForConversationAction();
            });
            this.inlineDialog(label, getKey);
        });

        this.inlineDialog(field, create);
    }

    public void loadEmptyConversation() {
        this.clientManager.setCurrentConversation(null);
        this.messagePane.getChildren().clear();
        this.partnerNameLabel.setText("");
        this.deleteButton.setDisable(true);
        this.getKeyButton.setDisable(true);
        this.editButton.setDisable(true);
        this.getMessageField().setDisable(true);
        this.sendButton.setDisable(true);
    }

    public void addNewConversation() {
        File fileLocation = this.chooseFileLocation();
        if (fileLocation == null) {
            this.hideInlineDialog();

        }

        TextField field = new TextField();
        field.setPromptText("Choose Partner Name");
        Button create = new Button();
        create.setText("Import");
        create.setDefaultButton(true);
        create.setOnAction(e -> {
            this.hideInlineDialog();
            this.clientManager.addNewConversation(field.getText(), fileLocation);
        });
        this.inlineDialog(field,create);
    }




    /* UI ACTIONS ------------------------------------------------------------------ */


    public void deleteConversationAction() {
        if (clientManager.getCurrentConversation() != null) {
            Label label = new Label();
            label.setText("Are you sure you want to delete this conversation?");
            label.setPadding(new Insets(4, 0, 0, 0));
            label.setFont(Font.font("Helvetica", FontWeight.BLACK, 13.0));
            Button deleteButton = new Button();
            deleteButton.setText("Delete");
            deleteButton.setDefaultButton(true);
            deleteButton.setOnAction(e -> {
                this.clientManager.deleteConversation(clientManager.getCurrentConversation());
                this.hideInlineDialog();
                this.loadEmptyConversation();
            });
            this.inlineDialog(label, deleteButton);
        }
    }

    public void editPartnerNameAction() {
        if (clientManager.getCurrentConversation() != null) {
            TextField field = new TextField();
            field.setText(this.partnerNameLabel.getText());
            Button button = new Button();
            button.setText("Done");
            button.setDefaultButton(true);
            button.setOnAction(e -> {
                this.clientManager.editPartnerName(field.getText());
                this.hideInlineDialog();
            });
            this.inlineDialog(field, button);

        }
    }

    public void newConversationAction() {
        this.loadEmptyConversation();
        ToolBarLabel label = new ToolBarLabel("Choose option");
        Button addNewConversation = new Button();
        addNewConversation.setText("Import Conversation");
        addNewConversation.setOnAction(e -> {
            this.hideInlineDialog();
            this.addNewConversation();
        });
        Button createNewConversation = new Button();
        createNewConversation.setDefaultButton(true);
        createNewConversation.setText("Create Conversation");
        createNewConversation.setOnAction(e -> {
            this.hideInlineDialog();
            createNewConversation();
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(createNewConversation, addNewConversation);
        hBox.setSpacing(10);

        this.inlineDialog(label, hBox);
    }

    public void sendMessageAction() {
        if (this.clientManager.currentConversation == null) {
            this.showMessage("No Conversation selected");
            return;
        }

        this.clientManager.sendMessage(this.clientManager.currentConversation, messageField.getText());
    }


    public void getKeyForConversationAction() {
        System.out.println("get key");
        File directoryLocation = this.chooseDirectoryLocation();
        int id = 0;
        try {
            this.clientManager.currentConversation.writeBoardKeysToFile(new File(directoryLocation + File.separator +
                    "Key.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void logoutAction() {
        this.clientManager.logout();
    }

    /* UI PROMPTS ------------------------------------------------------------------ */

    public void inlineDialog(Node node, Node actionButton) {
        this.leftToolBarConversation.getChildren().clear();
        this.rightToolBarConversation.getChildren().clear();
        this.leftToolBarConversation.getChildren().add(node);

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(e -> this.hideInlineDialog());

        rightToolBarConversation.getChildren().add(actionButton);
        rightToolBarConversation.getChildren().add(cancelButton);
    }

    public void hideInlineDialog() {
        this.leftToolBarConversation.getChildren().clear();
        this.rightToolBarConversation.getChildren().clear();
        this.leftToolBarConversation.getChildren().add(partnerNameLabel);
        rightToolBarConversation.getChildren().add(getKeyButton);
        rightToolBarConversation.getChildren().add(editButton);
        rightToolBarConversation.getChildren().add(deleteButton);
    }

    public File chooseDirectoryLocation() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a directory");
        File selectedDirectory = chooser.showDialog(this.scene.getWindow());

        return selectedDirectory;
    }

    public File chooseFileLocation() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(this.scene.getWindow());
        return selectedFile;
    }


    /*
     * GETTERS & SETTERS
     * -----------------------------------------------------------------------------
     *
     */


    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setLeftStatus(String status) {
        this.leftStatusLabel.setText(status);
    }

    public void showMessage(String message) {
        System.out.println(message);
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
