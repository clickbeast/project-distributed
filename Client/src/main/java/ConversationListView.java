import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ConversationListView extends Application {


    private static class Message {
        private String name;
        private String lastMessage;
        private int id;


        private int price;
        public String getName() {
            return name;
        }


        public int getPrice() {
            return price;
        }
        public Message(String name, int price) {
            super();
            this.name = name;
            this.price = price;
        }


    }




    @Override
    public void start(Stage primaryStage) {
        ObservableList<Message> data = FXCollections.observableArrayList();
        data.addAll(new Message("Cheese", 123), new Message("Horse", 456), new Message("Jam", 789));

        final ListView<Message> listView = new ListView<Message>(data);
        listView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> listView) {
                return new CustomListCell();
            }

        });

    }

    private class CustomListCell extends ListCell<Message> {
        private HBox content;
        private Text name;
        private Text price;

        public CustomListCell() {
            super();
            name = new Text();
            price = new Text();
            VBox vBox = new VBox(name, price);
            content = new HBox(new Label("[Graphic]"), vBox);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(Message item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) { // <== test for null item and empty parameter
                name.setText(item.getName());
                price.setText(String.format("%d $", item.getPrice()));
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }

}