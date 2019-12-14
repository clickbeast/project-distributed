
import exceptions.AccountAlreadyExistsException;
import interfaces.ThreadListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Conversation;
import model.Message;
import ui.Feedback;

import java.io.File;
import java.util.Comparator;
import java.util.function.Consumer;

public class ClientManager {


    private MainWindowViewController mainWindowViewController;
    Conversation currentConversation;
    ObservableList<Conversation> conversations;

    private LocalStorageManager localStorageManager;
    private MessageManager messageManager;


    /**
     * Creates a dummy conversation used for UI testing
     * @param name
     * @return
     */
    public Conversation conversationDummy(String name) {
        //TODO: not working now

        Message message1 = new Message(
                "Message 1",
                0
                , System.currentTimeMillis(),
                true
                ,true
                ,true);

        Message message2 = new Message(
                "Message 2",
                0
                , System.currentTimeMillis(),
                true
                ,true
                ,true);

        ObservableList<Message> messages = FXCollections.observableArrayList();
        messages.add(message1);
        messages.add(message2);

        Conversation conversation = new Conversation(this.conversations.size() + 1, name, null, null, messages);

        return conversation;

    }

    /* LOADING ------------------------------------------------------------------ */


    public ClientManager() {
        conversations = FXCollections.observableArrayList();
        messageManager = new MessageManager();
        Conversation conversation = conversationDummy("Vincent");
        this.conversations.add(conversation);
    }

    public void loadUserContents() {
        //get current conversation
        this.currentConversation = this.getConversations().get(0);
        this.mainWindowViewController.loadApplicationView();
        this.mainWindowViewController.loadInbox();
        this.mainWindowViewController.loadConversation(getCurrentConversation());
        //this.mainWindowViewController.showEmptyConversation();
    }


    public void sortConversationsAccordingToPolicy() {
        //TODO:

    }
    /* ACTIONS ------------------------------------------------------------------ */


    public void createAccount(String username, String password, File directoryLocation) {
        System.out.println("Creating account");
        //TODO: @arne createAccount routine
        this.login(username, password, b -> {
            if (!b.isSucces()) {
                this.mainWindowViewController.loginViewController.createAccountInfoLabel.setText("Login failed -> try" +
                        " to login");
            }
        });
    }

    public void login(String username, String password, Consumer<Feedback> callback) {
        System.out.println("Logging in");
        //TODO: @arne login routine
        boolean fail = false;

        if (fail) {
            callback.accept(new Feedback(false,"Login failed please try again"));
        }


        this.loadUserContents();


    }

    public void logout() {
        System.out.println("Logging out");
        this.mainWindowViewController.loadEmptyConversation();
        this.mainWindowViewController.freezeUI();
        //TODO: @arne  logout routine

        this.mainWindowViewController.loadLoginView();
    }

    public void addNewConversation(String name, File location) {
        //TODO: @arne addNewConversation Routine
        this.mainWindowViewController.loadConversation(this.getCurrentConversation());
    }

    public void createNewConversation(String name) {

        //TODO: @arnecreateNewConversation Routine
        Conversation c = this.conversationDummy(name);
        conversations.add(c);
        this.mainWindowViewController.loadConversation(c);

    }

    public void sendMessage(Conversation conversation, String text) {

        Message message = new Message(text, conversation.getUserId(), System.currentTimeMillis(), true, true, true);
        conversation.getMessages().add(message);
        ThreadListener listener = new ThreadListener() {

            @Override
            public void threadFinished() {
                messageDelivered(conversation);
            }
        };
        messageManager.sendMessage(conversation, message, listener);

        this.mainWindowViewController.messageField.setText("");
        //TODO:@simon CALLABCK MECHANISM
        this.mainWindowViewController.reloadUI();
    }

    public void messageDelivered(Conversation conversation) { }

    public void deleteConversation(Conversation conversation) {
        this.conversations.remove(conversation);
        //TODO: @arne remove from client manager subroutine
        //TODO: @arne remove from server subroutine?
        this.mainWindowViewController.loadEmptyConversation();
    }

    public void editPartnerName(String text) {
        System.out.println("edit partner name");
        if (this.currentConversation != null) {
            //TODO: @arne method for also updating it in the database
            this.currentConversation.setUserName(text);
        }
        this.mainWindowViewController.reloadUI();
    }


    /*
     * GETTERS & SETTERS
     * -----------------------------------------------------------------------------
     *
     */

    public MainWindowViewController getMainWindowViewController() {
        return mainWindowViewController;
    }
    public void setMainWindowViewController(MainWindowViewController mainWindowViewController) {
        this.mainWindowViewController = mainWindowViewController;
    }

    public ObservableList<Conversation> getConversations() {
        return conversations;
    }

    public void getKeyForConversation(int id, File directoryLocation) {


    }

    public void setCurrentConversation(Conversation currentConversation) {
        this.currentConversation = currentConversation;
    }

    public Conversation getCurrentConversation() {
        return currentConversation;
    }

    public void setConversations(ObservableList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public LocalStorageManager getLocalStorageManager() {
        return localStorageManager;
    }

    public void setLocalStorageManager(LocalStorageManager localStorageManager) {
        this.localStorageManager = localStorageManager;
    }


}
