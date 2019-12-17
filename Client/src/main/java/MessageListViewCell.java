import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Message;

import java.io.IOException;

public class MessageListViewCell extends ListCell<Message> {

    Label  partnerMessage;
    Label partnerTimeStamp;
    Label userMessage;
    Label userTimeStamp;

    MainWindowViewController mainWindowViewController;

    public MessageListViewCell(MainWindowViewController mainWindowViewController) {
        super();
        this.mainWindowViewController = mainWindowViewController;
    }

    //TODO: implement this;

        /*
            https://stackoverflow.com/questions/9722418/how-to-handle-listview-item-clicked-action
         */

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);
        if (message != null && !empty) { // <== test for null item and empty parameter


            Node itemRoot = null;
            try {
                itemRoot = FXMLLoader.load(getClass().getResource(("message.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            partnerMessage = (Label) itemRoot.lookup("#partnerMessageLabel");
            userMessage = (Label) itemRoot.lookup("#userMessageLabel");

            if(message.isFromUser()) {
                userMessage.setText(message.getText());
                this.calibrateWidth(userMessage);
                this.calibrateWidth(partnerMessage);
                partnerMessage.setText("");
                partnerMessage.setStyle("");
                if(!message.isDelivered()) {
                    userMessage.setStyle("-fx-background-color: none" +
                            "; -fx-border-color:  #C0E8F1" +
                            "; -fx-border-radius: 5");
                }
            }else{
                partnerMessage.setText(message.getText());
                this.calibrateWidth(partnerMessage);
                this.calibrateWidth(userMessage);
                userMessage.setText("");
                userMessage.setStyle("");

            }
            setWrapText(true);


            setMinWidth(mainWindowViewController.getMessagePane().getWidth()-30);
            setMaxWidth(mainWindowViewController.getMessagePane().getWidth()-30);
            setPrefWidth(mainWindowViewController.getMessagePane().getWidth()-30);



            setGraphic(itemRoot);

            //get last message
            //this.setStyle("-fx-background-color: black;");
            System.out.println(message);


            //this.setGraphic(null);
        } else {
            setGraphic(null);
        }
    }


    public void calibrateWidth(Label node) {
        node.setMaxWidth(this.mainWindowViewController.getMessagePane().getWidth()-100);
    }

    public Node loadPartnerMessage() {
        return null;
    }

    public Node loadUserMessage() {
        return null;
    }


}
