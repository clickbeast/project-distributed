import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowViewController implements Initializable {

    public Scene scene;
    public Stage stage;

    public StateManager stateManager;

    /* VARIABLES ------------------------------------------------------------------ */

    public AnchorPane slavePane;
    public ListView<Slave> slaveListView;
    public Label generalInfoLabel;
    public Button button;

    /* UI SETUP ------------------------------------------------------------------ */

    public MainWindowViewController() {


    }

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing...");

    }

    public void setupComplete() {
        loadServerView();
    }

    public void loadServerView() {
        System.out.println("RELOAD VIEW");
        //TODO: @simon
        loadMasterView();
        loadMasterWatcherView();
        loadSlaveListView();

    }

    public void loadMasterView() {
        //TODO: @simon

    }

    public void loadMasterWatcherView() {
        //TODO: @simon
    }

    public MainWindowViewController getMainWindowViewController() {
        return this;
    }

    public void loadSlaveListView() {
        this.getSlavePane().getChildren().clear();
        final ListView<Slave> listView = new ListView<Slave>(this.stateManager.getMaster().getSlaves());
        listView.setCellFactory(new Callback<ListView<Slave>, ListCell<Slave>>() {
            @Override
            public ListCell<Slave> call(ListView<Slave> listView) {
                return new SlaveListViewCell(getMainWindowViewController());
            }
        });

        listView.setFocusTraversable( false );

        this.setSlaveListView(listView);
        this.getSlavePane().getChildren().add(this.getSlaveListView());

        this.slaveListView.setStyle("-fx-border-color: #F4F4F4;");

        //configure bounds
        AnchorPane.setBottomAnchor(listView, 0.0);
        AnchorPane.setTopAnchor(listView,0.0);
        AnchorPane.setRightAnchor(listView,0.0);
        AnchorPane.setLeftAnchor(listView,0.0);

    }


    /* UI ACTIONS ------------------------------------------------------------------ */


    //Same that's get called once in a while
    //TODO: @arne
    public void fetch() {

    }

    //TODO: @Andres
    public void killAllExceptMaster() {
    }

    //TODO: @Andres
    public void killAllExceptMasterWatcher() {

    }

    //TODO: @Andres
    public void killMaster() {

    }

    //TODO: @Andres
    public void killMasterWatcher() {

    }


    //TODO: @Andres
    public void killSlave(Slave slave) {

    }


    /*
     * GETTERS & SETTERS
     * -----------------------------------------------------------------------------
     *
     */


    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }


    public AnchorPane getSlavePane() {
        return slavePane;
    }

    public void setSlavePane(AnchorPane slavePane) {
        this.slavePane = slavePane;
    }

    public ListView<Slave> getSlaveListView() {
        return slaveListView;
    }

    public void setSlaveListView(ListView<Slave> slaveListView) {
        this.slaveListView = slaveListView;
    }

    public Label getGeneralInfoLabel() {
        return generalInfoLabel;
    }

    public void setGeneralInfoLabel(Label generalInfoLabel) {
        this.generalInfoLabel = generalInfoLabel;
    }
}
