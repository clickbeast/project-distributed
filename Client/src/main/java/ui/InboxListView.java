package ui;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;




public class InboxListView extends ListView<HBox> {



    private int processId;

    public InboxListView() {
        super();
        configurePageTable();
    }


    public void configurePageTable() {
        this.setMouseTransparent( true );
        this.setFocusTraversable( false );
        this.setPlaceholder(new Label("No Content In PageTable"));

    }


    //import the right data
    public void fillWithMessages(List<String> messages) {

    }



    private void addCellWithData(String message) {
        HBox hbox = new HBox();

        //Label config
        Label c1 = new Label("Bonjour");
        Label c2 = new Label("Hello");


        HBox.setHgrow(c1, Priority.ALWAYS);
        HBox.setHgrow(c2, Priority.ALWAYS);



        //Wrap in HBOX again
        HBox w1 = new HBox(c1);
        HBox w2 = new HBox(c2);



        HBox.setHgrow(w1, Priority.ALWAYS);
        HBox.setHgrow(w2, Priority.ALWAYS);


        hbox.getChildren().add(w1);
        hbox.getChildren().add(w2);

        this.getItems().add(hbox);
    }

    public void reset() {
        this.getItems().clear();
    }


    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

}



