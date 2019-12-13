package ui;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.Conversation;

public class ConversationListViewCell extends ListCell<Conversation> {
    private HBox content;
    private Text name;
    private Text message;

    public ConversationListViewCell() {
        super();
        name = new Text();
        name.setFont(Font.font("Helvetica", FontWeight.BLACK,13.0));

        message = new Text();
        VBox vBox = new VBox(name, message);
        content = new HBox(new Label("[Graphic]"), vBox);
        content.setSpacing(10);
    }

    //TODO: implement this;

        /*
            https://stackoverflow.com/questions/9722418/how-to-handle-listview-item-clicked-action
         */

    @Override
    protected void updateItem(Conversation item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter

            name.setText(item.getUserName());
            //get last message
            message.setText(item.getMessages().get(0).getText());
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
