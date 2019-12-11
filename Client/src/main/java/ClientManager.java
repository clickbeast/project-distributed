import java.io.File;

public class ClientManager {


    private MainWindowViewController mainWindowViewController;

    public ClientManager() {

    }

    public MainWindowViewController getMainWindowViewController() {
        return mainWindowViewController;
    }

    public void setMainWindowViewController(MainWindowViewController mainWindowViewController) {
        this.mainWindowViewController = mainWindowViewController;
    }




    /**
     *
     * Subject to change!
     */


    public void getConversations() {


    }

    public void getConversation() {


    }

    public void sendMessage(int id, String text) {


    }


    public void deleteConversation(int id) {
        System.out.println("deleting conversation");
    }


    public void getKeyForConversation(int id, File directoryLocation) {


    }

    public void createAccount(String username, String password, File directoryLocation) {

    }

    public void addNewConversation(String name, File location) {

    }
}
