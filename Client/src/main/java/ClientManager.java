
import exceptions.AccountAlreadyExistsException;
import interfaces.ThreadListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Conversation;
import model.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.function.Consumer;

public class ClientManager {


    private MainWindowViewController mainWindowViewController;
    Conversation currentConversation;
    ObservableList<Conversation> conversations;

    private LocalStorageManager localStorageManager;
    private MessageManager messageManager;


    /**
     * Creates a dummy conversation used for UI testing
     *
     * @param name
     * @return
     */
    public Conversation conversationDummy(String name) {
        //TODO: not working now

        Message message = new Message("Hello world", 0, true, 1);
        Message message1 = new Message("Whoop whoop", 0, false, 1);


        ObservableList<Message> messages = FXCollections.observableArrayList();
        messages.add(message);
        messages.add(message1);
        Conversation conversation = new Conversation(0,this.conversations.size() + 1, name, null, null, messages);

        return conversation;

    }


    public ClientManager() {
        conversations = FXCollections.observableArrayList();
        messageManager = new MessageManager();
        ThreadListener listener = new ThreadListener() {

            @Override
            public void threadFinished() {
                System.err.println("You shouldn't be here");
            }

            @Override
            public void newMessage(Message message, Conversation conversation) {
                messageRecieved(conversation, message);
            }
        };
        messageManager.getMessages(listener);
        Conversation conversation = conversationDummy("Vincent");
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
        this.mainWindowViewController.loadApplicationView();
        this.mainWindowViewController.loadInbox();
        this.mainWindowViewController.loadConversation(getCurrentConversation());
        //this.mainWindowViewController.showEmptyConversation();
    }


    public void login(String username, String password, Consumer<Boolean> callback) {
        System.out.println("Logging in");


        int userID = localStorageManager.login(username, password);
        if (userID != 1) {
            callback.accept(true);
        } else {
            callback.accept(false);
        }

        this.loadUserContents();


    }


    public void logout() {
        System.out.println("Logging out");
        this.mainWindowViewController.loadEmptyConversation();
        this.mainWindowViewController.freezeUI();
        //TODO: Dunno if there si more to do for logout?
        messageManager.clearConversations();

        this.mainWindowViewController.loadLoginView();
    }

    public void createAccount(String username, String password, File directoryLocation) {
        System.out.println("Creating account");
        localStorageManager.setPath(directoryLocation.getPath());
        try {
            localStorageManager.addAccount(username, password, "");
        } catch (AccountAlreadyExistsException e) {
            this.mainWindowViewController.loginViewController.createAccountInfoLabel.setText("Account with login " + username + " already exists.");
        }
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

        Conversation conversation = null;
        try {
            conversation = new Conversation(name, location);
        } catch (FileNotFoundException e) {
            //TODO: @simon give notification if wrong file?
        }
        if (conversation != null) {
            localStorageManager.saveConversation(conversation);

            this.mainWindowViewController.loadConversation(conversation);
        }
    }

    /**
     * Creating one that doesn't exist yet
     *
     * @param name
     */
    public void createNewConversation(String name) {


        Conversation c = new Conversation(name,messageManager.getLastBound());
        int id = localStorageManager.saveConversation(c);
        if (id != -1) {
            c.setContactId(id);
        }
        conversations.add(c);
        this.mainWindowViewController.loadConversation(c);

    }


    //TODO @simon changeover to this
    public void sendMessage1(Conversation conversation, String text) {
        Message message = new Message(text, conversation.getUserId(), System.currentTimeMillis(), true, true, true);
        conversation.getMessages().add(message);
        ThreadListener listener = new ThreadListener() {

            @Override
            public void threadFinished() {
                messageDelivered(conversation);
            }

            @Override
            public void newMessage(Message message, Conversation conversation) {
                System.err.println("You shouldn't be here");
            }
        };
        messageManager.sendMessage(conversation, message, listener);
    }

    public synchronized void messageDelivered(Conversation conversation) {
        //TODO:something
    }


    public synchronized void messageRecieved(Conversation conversation, Message message) {
//TODO: Should get called from thread.
    }


    public void deleteConversation(Conversation conversation) {
        this.conversations.remove(conversation);
        localStorageManager.deleteConversation(conversation);
        //DONE: @arne remove from client manager subroutine
        //DONE: @arne remove from server subroutine?
        //NOTE: This will make the thread not check anymore for new, I assume that's enough
        messageManager.removeConversation(conversation);
        this.mainWindowViewController.loadEmptyConversation();
    }


    public MainWindowViewController getMainWindowViewController() {
        return mainWindowViewController;
    }

    public void editPartnerName(String text) {
        System.out.println("edit partner name");
        if (this.currentConversation != null) {
            localStorageManager.updateConversation(text, this.currentConversation.getContactId());
            this.currentConversation.setUserName(text);
        }
        this.mainWindowViewController.reloadUI();
    }


    public void sendMessage(Conversation conversation, String text) {
        //TODO: REVISIT ... some things are currenly wrong beceause of merge
        //TODO: do for real
        Message message = new Message(text, conversation.getUserId(), true,
                Math.toIntExact(System.currentTimeMillis()));
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
