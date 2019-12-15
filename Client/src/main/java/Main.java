
import com.sun.security.ntlm.Client;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {


    // keep a reference to the main window controller
    private MainWindowViewController mainWindowViewController;

    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));

        // provide the controller with a reference of the simulationManager
        loader.setControllerFactory(c -> {
            if (c == MainWindowViewController.class) {
                MainWindowViewController mc = new MainWindowViewController();
                mainWindowViewController = mc;
                return mc;
            } else {
                try {
                    return c.newInstance();
                } catch (Exception exc) {
                    throw new RuntimeException(exc);
                }
            }
        });

        Parent flowPane = loader.load();

        ClientManager clientManager = new ClientManager();
        mainWindowViewController.setClientManager(clientManager);
        clientManager.setMainWindowViewController(mainWindowViewController);

        primaryStage.setTitle("Da Cripti");
        primaryStage.setScene(new Scene(flowPane, 1200, 860));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(750);
        primaryStage.show();

        mainWindowViewController.scene = primaryStage.getScene();
        mainWindowViewController.stage = primaryStage;

        primaryStage.getScene().getStylesheets().add(getClass().getResource("listViewStyle.css").toExternalForm());

        // endregion
        this.mainWindowViewController.setupComplete();



        /**
         *
         * RUN YOUR TESTS BELOW THIS
         */


    }




    public static void main(String[] args) {
        launch(args);
    }
}