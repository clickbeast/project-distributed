import com.sun.jmx.snmp.SnmpVarBind;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sun.plugin.javascript.navig.Anchor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SlaveListViewCell extends ListCell<Slave> {

    public TreeView<String> slaveTreeView;
    public TitledPane slaveTitledPane;


    public SlaveListViewCell() {
        super();

    }

    @Override
    protected void updateItem(Slave slave, boolean empty) {
        super.updateItem(slave, empty);
        if (slave != null && !empty) {

            /*Node itemRoot = null;

            try {
                itemRoot = FXMLLoader.load(getClass().getResource(("slaveView.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.setSlaveTitledPane( (TitledPane) itemRoot.lookup("#slaveTitledPane"));
            this.getSlaveTitledPane().lookup("#a");
            AnchorPane pane = ((AnchorPane) itemRoot.lookup("#a"));*/

            this.setSlaveTreeView(new TreeView<String>());
            AnchorPane.setBottomAnchor(this.getSlaveTreeView(), 0.0);
            AnchorPane.setTopAnchor(this.getSlaveTreeView(),0.0);
            AnchorPane.setRightAnchor(this.getSlaveTreeView(),0.0);
            AnchorPane.setLeftAnchor(this.getSlaveTreeView(),0.0);
            this.loadSlaveTreeView(slave);

            VBox box = new VBox();
            box.setSpacing(5.0);
            box.getChildren().add(new Button("Kill Slave"));
            box.getChildren().add(new Separator());
            box.getChildren().add(this.getSlaveTreeView());
            TitledPane titledPane = new TitledPane();
            titledPane.setText("Slave 1");
            titledPane.setContent(box);

            AnchorPane.setBottomAnchor(titledPane, 0.0);
            AnchorPane.setTopAnchor(titledPane,0.0);
            AnchorPane.setRightAnchor(titledPane,0.0);
            AnchorPane.setLeftAnchor(titledPane,0.0);



            //get last message
            this.setStyle("-fx-background-color: #F4F4F4;");

            setGraphic(titledPane);

        } else {
            setGraphic(null);
        }
    }

    public void loadSlaveTreeView(Slave slave) {

        TreeItem<String> root = new TreeItem<>("root");


        TreeItem<String> title = new TreeItem<>("Slave");
        TreeItem<Button> kill = new TreeItem<Button>(new Button("Kill Slave"));


        TreeItem<String> t_b = new TreeItem<String>("Mailboxes");
        t_b.setExpanded(true);

        TreeItem<String> b_1 = new TreeItem<String>("Mailbox: 1");
        b_1.setExpanded(true);

        TreeItem<String> t_m = new TreeItem<String>("Messages");
        t_m.setExpanded(true);

        TreeItem<String> m = new TreeItem<String>("Message: 102");
        m.setExpanded(true);

        TreeItem<String> m_c_1 = new TreeItem<String>("text: hello");
        m_c_1.setExpanded(true);

        TreeItem<String> m_c_2 = new TreeItem<String>("tag: 12");
        m_c_2.setExpanded(true);

        t_b.getChildren().add(b_1);
        b_1.getChildren().add(t_m);
        t_m.getChildren().add(m);
        m.getChildren().add(m_c_1);
        m.getChildren().add(m_c_2);


        //TODO: read out slave

        this.slaveTreeView.setRoot(t_b);
        this.slaveTreeView.setShowRoot(true);

    }


    public TreeView<String> getSlaveTreeView() {
        return slaveTreeView;
    }

    public void setSlaveTreeView(TreeView<String> slaveTreeView) {
        this.slaveTreeView = slaveTreeView;
    }

    public TitledPane getSlaveTitledPane() {
        return slaveTitledPane;
    }

    public void setSlaveTitledPane(TitledPane slaveTitledPane) {
        this.slaveTitledPane = slaveTitledPane;
    }
}
