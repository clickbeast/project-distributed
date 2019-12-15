import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Slave;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowViewController implements Initializable {

    public Scene scene;
    public Stage stage;

    public StateManager stateManager;



    /* VARIABLES ------------------------------------------------------------------ */

    public AnchorPane slavePane;
    public ListView<Slave> slaveListView;
    public Label generalInfo;
    public Button button;

    /* UI SETUP ------------------------------------------------------------------ */


    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing...");

    }

    public void setupComplete() {
        //TODO
    }

    public void loadServerView() {
        //TODO

    }

    public void loadMasterView() {
        //TODO

    }

    public void loadMasterWatcherView() {
        //TODO
    }

    public void loadSlaveListView() {

        final ListView<Slave> listView = new ListView<Slave>(this.stateManager.getSlaves());
        listView.setCellFactory(new Callback<ListView<Slave>, ListCell<Slave>>() {
            @Override
            public ListCell<Slave> call(ListView<Slave> listView) {
                return new SlaveListViewCell();
            }
        });

        this.setSlaveListView(listView);
        this.getSlavePane().getChildren().add(this.getSlaveListView());


        //configure bounds
        AnchorPane.setBottomAnchor(listView, 0.0);
        AnchorPane.setTopAnchor(listView,0.0);
        AnchorPane.setRightAnchor(listView,0.0);
        AnchorPane.setLeftAnchor(listView,0.0);

    }


    /* UI ACTIONS ------------------------------------------------------------------ */

    public void fetch() {

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

    public Label getGeneralInfo() {
        return generalInfo;
    }

    public void setGeneralInfo(Label generalInfo) {
        this.generalInfo = generalInfo;
    }
}
