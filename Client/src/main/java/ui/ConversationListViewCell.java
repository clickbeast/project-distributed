package ui;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Conversation;
public class ConversationListViewCell extends ListCell<Conversation> {

    private HBox content;
    private Text name;
    private Text message;
    private Text timestamp;

    public ConversationListViewCell() {
        super();
        name = new Text();
        name.setFont(Font.font("Helvetica", FontWeight.BLACK,13.0));
        message = new Text();
        timestamp = new Text();
        VBox vBox = new VBox(name, message);
        content = new HBox(vBox, timestamp);
        timestamp.setTextAlignment(TextAlignment.RIGHT);
        content.setSpacing(10);
    }

        /*
            https://stackoverflow.com/questions/9722418/how-to-handle-listview-item-clicked-action
         */

    @Override
    protected void updateItem(Conversation conversation, boolean empty) {
        super.updateItem(conversation, empty);
        if (conversation != null && !empty) {
            name.setText(conversation.getUserName());
            message.setText(conversation.getMessages().get(0).getText());
            timestamp.setText(conversation.getMessages().get(0).getFormattedTimeStamp());
            //TODO: adjust
            if(!conversation.getMessages().get(0).isSeen()) {
                this.setStyle("-fx-background-color: #187592");
            }
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}

