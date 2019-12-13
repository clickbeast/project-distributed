package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ToolBarLabel extends Label {


    public ToolBarLabel(String text) {
        super(text);
        this.setPadding(new Insets(4, 0, 0, 0));
        this.setFont(Font.font("Helvetica", FontWeight.BLACK,13.0));
    }
}
