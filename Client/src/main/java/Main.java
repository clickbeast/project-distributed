
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

    private ClientManager clientManager;

    // keep a reference to the main window controller
    private MainWindowViewController mainWindowViewController;

//    public void start(Stage primaryStage) throws Exception {
//
//        // region UI SETUP
//
//        this.clientManager = new ClientManager();
//
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
//
//        // provide the controller with a reference of the simulationManager
//        loader.setControllerFactory(c -> {
//            if (c == MainWindowViewController.class) {
//                MainWindowViewController mc = new MainWindowViewController();
//                mc.setClientManager(this.clientManager);
//                mainWindowViewController = mc;
//                return mc;
//            } else {
//                try {
//                    return c.newInstance();
//                } catch (Exception exc) {
//                    throw new RuntimeException(exc);
//                }
//            }
//        });
//
//        Parent flowPane = loader.load();
//
//        primaryStage.setTitle("Linkonardo Da Criptni");
//        // primaryStage.setResizable(false);
//        primaryStage.setScene(new Scene(flowPane, 1200, 860));
//        primaryStage.setMinWidth(800);
//        primaryStage.setMinHeight(750);
//        primaryStage.show();
//
//        this.clientManager.setMainWindowViewController(mainWindowViewController);
//        mainWindowViewController.scene = primaryStage.getScene();
//
//        // endregion
//
//        /**
//         *
//         * RUN YOUR TESTS BELOW THIS
//         */
//
//
//    }


    @Override
    public void start(Stage primaryStage) throws Exception {





        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));


        // provide the controller with a reference of the simulationManager
//        loader.setControllerFactory(c -> {
//            if (c == MainWindowViewController.class) {
//                MainWindowViewController mc = new MainWindowViewController();
//                mc.setClientManager(this.clientManager);
//                mainWindowViewController = mc;
//                return mc;
//            } else {
//                try {
//                    return c.newInstance();
//                } catch (Exception exc) {
//                    throw new RuntimeException(exc);
//                }
//            }
//        });





        Scene scene = new Scene(root, 300, 275);

        primaryStage.setTitle("Da Cripti");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}