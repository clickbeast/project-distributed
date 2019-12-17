import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;

import java.io.IOException;

public class SlaveListViewCell extends ListCell<Slave> {

    public TreeView<Slave> slaveTreeView;
    public TitledPane slaveTitledPane;


    public SlaveListViewCell() {
        super();

    }

    @Override
    protected void updateItem(Slave slave, boolean empty) {
        super.updateItem(slave, empty);
        if (slave != null && !empty) {

            Node itemRoot = null;

            try {
                itemRoot = FXMLLoader.load(getClass().getResource(("slaveView.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.setSlaveTitledPane((TitledPane) itemRoot.lookup("#slaveTitledPane"));
            this.setSlaveTreeView((TreeView<Slave>) itemRoot.lookup("#slaveTreeView"));

            //get last message
            this.setStyle("-fx-background-color: #F4F4F4;");


            this.getSlaveTitledPane().setText(slave.getName());
            this.loadSlaveTreeView(slave);

            setGraphic(itemRoot);
        } else {
            setGraphic(null);
        }
    }

    public void loadSlaveTreeView(Slave slave) {
        //TODO:
    }


    public TreeView<Slave> getSlaveTreeView() {
        return slaveTreeView;
    }

    public void setSlaveTreeView(TreeView<Slave> slaveTreeView) {
        this.slaveTreeView = slaveTreeView;
    }

    public TitledPane getSlaveTitledPane() {
        return slaveTitledPane;
    }

    public void setSlaveTitledPane(TitledPane slaveTitledPane) {
        this.slaveTitledPane = slaveTitledPane;
    }
}
