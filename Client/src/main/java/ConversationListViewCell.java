import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Conversation;

public class ConversationListViewCell extends ListCell<Conversation> {

    private HBox content;
    private Text name;
    private Label message;
    private Text timestamp;
    MainWindowViewController mainWindowViewController;

    public ConversationListViewCell(MainWindowViewController mainWindowViewController) {
        super();
        name = new Text();
        name.setFont(Font.font("Helvetica", FontWeight.BLACK, 13.0));
        message = new Label();
        timestamp = new Text();
        timestamp.setTextAlignment(TextAlignment.RIGHT);


        HBox boxy = new HBox(timestamp);
        boxy.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(timestamp, Priority.ALWAYS);

        HBox hBox = new HBox();
        hBox.getChildren().add(name);
        //hBox.getChildren().add(boxy);



        VBox vBox = new VBox(hBox,timestamp, message);
        content = new HBox(vBox);
        content.setSpacing(10);
        this.mainWindowViewController = mainWindowViewController;
    }

    @Override
    protected void updateItem(Conversation conversation, boolean empty) {
        super.updateItem(conversation, empty);
        if (conversation != null && !empty) {
            name.setText(conversation.getUserName());
            if (!conversation.getMessages().isEmpty()) {

                int lastmsg=conversation.getMessages().size()-1;
                message.setText(conversation.getMessages().get(lastmsg).getText().length() > 50 ?
                        conversation.getMessages().get(lastmsg).getText().substring(0, 50) :
                        conversation.getMessages().get(lastmsg).getText());
                message.setWrapText(true);
                timestamp.setText(conversation.getMessages().get(lastmsg).getFormattedTimeStamp());
                System.out.println(conversation.getMessages().get(lastmsg).getFormattedTimeStamp());
                //TODO: adjust
                if (!conversation.getMessages().get(lastmsg).isSeen()) {
                    this.setStyle("-fx-background-color: #187592");
                }
            }else{
                message.setText("");
                timestamp.setText("");
            }
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}

