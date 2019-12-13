import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Conversation;
import model.Message;

import java.io.File;
import java.util.function.Consumer;

public class ClientManager {


    private MainWindowViewController mainWindowViewController;
    Conversation currentConversation;
    ObservableList<Conversation> conversations;

    private LocalStorageManager localStorageManager;


    /**
     * Creates a dummy conversation used for UI testing
     * @param name
     * @return
     */
    public Conversation conversationDummy(String name) {
        Message message = new Message("Hello world", 0, true, 1);
        Message message1 = new Message("Whoop whoop", 0, false, 1);

        ObservableList<Message> messages = FXCollections.observableArrayList();
        messages.add(message);
        messages.add(message1);
        Conversation conversation = new Conversation(this.conversations.size() + 1, name, null, null, messages);

        return conversation;
    }


    public ClientManager() {
        conversations = FXCollections.observableArrayList();
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


    public void login(String username, String password, Consumer<Boolean> callback) {
        System.out.println("Logging in");



        //TODO: @arne login routine
        boolean fail = false;

        if (fail) {
            callback.accept(false);
        } else {
            callback.accept(true);
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

    public void createAccount(String username, String password, File directoryLocation) {
        System.out.println("Creating account");
        //TODO: @arne createAccount routine
        //TODO:@Simon handle callback fail please :) -> will be replaced by feedback callback
        this.login(username, password, b -> {
            if (!b) {
                this.mainWindowViewController.loginViewController.createAccountInfoLabel.setText("Login failed -> try" +
                        " to login");
            }
        });
    }


    /**
     * Adding one  that doesn't exist
     *
     * @param name
     * @param location
     */
    public void addNewConversation(String name, File location) {
        //create conversation

        //TODO: @arne addNewConversation Routine
        this.mainWindowViewController.loadConversation(this.getCurrentConversation());
    }

    /**
     * Creating one that doesn't exist yet
     *
     * @param name
     */
    public void createNewConversation(String name) {

        //TODO: @arnecreateNewConversation Routine
        Conversation c = this.conversationDummy(name);
        conversations.add(c);
        this.mainWindowViewController.loadConversation(c);

    }


    public void deleteConversation(Conversation conversation) {
        this.conversations.remove(conversation);
        //TODO: @arne remove from client manager subroutine
        //TODO: @arne remove from server subroutine?
        this.mainWindowViewController.loadEmptyConversation();
    }


    public MainWindowViewController getMainWindowViewController() {
        return mainWindowViewController;
    }

    public void editPartnerName(String text) {
        System.out.println("edit partner name");
        if (this.currentConversation != null) {
            //TODO: @arne method for also updating it in the database

            this.currentConversation.setUserName(text);
        }
        this.mainWindowViewController.reloadUI();
    }



    public void sendMessage(Conversation conversation, String text) {

        //TODO: do for real
        Message message = new Message(text, conversation.getUserId(), true, System.currentTimeMillis());
        conversation.getMessages().add(message);

        this.mainWindowViewController.messageField.setText("");
        //TODO:@simon CALLABCK MECHANISM
        this.mainWindowViewController.reloadUI();
    }

    /*
     * GETTERS & SETTERS
     * -----------------------------------------------------------------------------
     *
     */

    public void setMainWindowViewController(MainWindowViewController mainWindowViewController) {
        this.mainWindowViewController = mainWindowViewController;
    }

    public ObservableList<Conversation> getConversations() {
        return conversations;
    }


    public void delivered() {

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
