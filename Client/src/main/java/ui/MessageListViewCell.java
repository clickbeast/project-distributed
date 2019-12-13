package ui;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Message;

public class MessageListViewCell extends ListCell<Message> {
    private HBox content;
    private Text message;
    private Text date;

    public MessageListViewCell() {
        super();
        message = new Text();
        date = new Text();
        VBox vBox = new VBox(message, date);
        content = new HBox(new Label(""), vBox);
        content.setSpacing(5);
    }

    //TODO: implement this;

        /*
            https://stackoverflow.com/questions/9722418/how-to-handle-listview-item-clicked-action
         */

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            message.setText(item.getText());
            //get last message
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
