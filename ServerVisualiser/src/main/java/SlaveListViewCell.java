import javafx.scene.control.ListCell;
import model.Slave;

public class SlaveListViewCell extends ListCell<Slave> {

    @Override
    protected void updateItem(Slave slave, boolean empty) {
        super.updateItem(slave, empty);
        if (slave != null && !empty) {
            //TODO: @Simon
            setGraphic(null);
        } else {
            setGraphic(null);
        }
    }



}
