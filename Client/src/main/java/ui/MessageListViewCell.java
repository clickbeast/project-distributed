package ui;

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

    Text  partnerMessage;
    Label partnerTimeStamp;
    Label userMessage;
    Label userTimeStamp;



    public MessageListViewCell() {
        super();


    }

    //TODO: implement this;

        /*
            https://stackoverflow.com/questions/9722418/how-to-handle-listview-item-clicked-action
         */

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);
        if (message != null && !empty) { // <== test for null item and empty parameter

/*
            Node itemRoot = null;

            try {
                itemRoot = FXMLLoader.load(getClass().getResource(("message.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
*/

            //partnerMessage = (Label) itemRoot.lookup("#item_Label_AppName");


            //get last message
            //this.setStyle("-fx-background-color: black;");


            this.setGraphic(null);
        } else {
            setGraphic(null);
        }
    }

    public Node loadPartnerMessage() {
        return null;
    }

    public Node loadUserMessage() {
        return null;
    }


}
