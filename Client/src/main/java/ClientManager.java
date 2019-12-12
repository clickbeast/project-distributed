import com.sun.tools.javac.util.Convert;
import exceptions.AccountAlreadyExistsException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdk.internal.dynalink.linker.MethodTypeConversionStrategy;
import model.Conversation;
import model.Message;

import java.io.File;

public class ClientManager {


    private MainWindowViewController mainWindowViewController;
    Conversation currentConversation;
    ObservableList<Conversation> conversations;

    private LocalStorageManager localStorageManager;


    public ClientManager() {

        conversations = FXCollections.observableArrayList();

        //example

        Message message = new Message("Hello world", 0, true,1);
        Message message1 = new Message("Whoop whoop", 0, false,1);

        ObservableList<Message> messages = FXCollections.observableArrayList();
        messages.add(message);
        messages.add(message1);
        Conversation conversation = new Conversation(0,"partner",null,null,messages);
        this.conversations.add(conversation);



        //Auto login
        /*localStorageManager.createDatabase();
        localStorageManager.initializeAccountsDatabase();
        try {
            localStorageManager.addAccount("simon","root","dkfj");
        } catch (AccountAlreadyExistsException e) {
            e.printStackTrace();
        }
        localStorageManager.initializeConversationsDatabase();*/


    }

    public void loadUserContents() {
        //get current conversation
        this.currentConversation = this.getConversations().get(0);
        this.mainWindowViewController.loadInbox();
        this.mainWindowViewController.loadConversation(getCurrentConversation());
    }



    public MainWindowViewController getMainWindowViewController() {
        return mainWindowViewController;
    }

    public void setMainWindowViewController(MainWindowViewController mainWindowViewController) {
        this.mainWindowViewController = mainWindowViewController;
    }


    /*
     * GETTERS & SETTERS
     * -----------------------------------------------------------------------------
     *
     */


    public ObservableList<Conversation> getConversations() {
        return conversations;
    }

    public void sendMessage(Conversation conversation, String text) {
        Message message = new Message(text, conversation.getUserId(),true, System.currentTimeMillis());
        conversation.getMessages().add(message);
    }


    public void deleteConversation(Conversation conversation) {
        this.mainWindowViewController.showEmptyConversation();
    }


    public void getKeyForConversation(int id, File directoryLocation) {


    }

    public void createAccount(String username, String password, File directoryLocation) {

    }

    public void addNewConversation(String name, File location) {

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

    public void editPartnerName(String text) {
        System.out.println("edit partner name");
    }
}
