import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

/**
 * Source:
 * https://stackoverflow.com/questions/16652062/javafx-2-textarea-how-to-stop-it-from-consuming-enter-and-tab
 */


class SpecialTextArea extends TextArea {
    final TextArea myTextArea = this;

    SpecialTextArea() {
        addEventFilter(KeyEvent.KEY_PRESSED, new TabAndEnterHandler());
    }

    class TabAndEnterHandler implements EventHandler<KeyEvent> {
        private KeyEvent recodedEvent;

        @Override public void handle(KeyEvent event) {
            if (recodedEvent != null) {
                recodedEvent = null;
                return;
            }

            Parent parent = myTextArea.getParent();
            if (parent != null) {
                switch (event.getCode()) {
                    case ENTER:
                        if (event.isControlDown()) {
                            recodedEvent = recodeWithoutControlDown(event);
                            myTextArea.fireEvent(recodedEvent);
                        } else {
                            Event parentEvent = event.copyFor(parent, parent);
                            myTextArea.getParent().fireEvent(parentEvent);
                        }
                        event.consume();
                        break;

                    case TAB:
                        if (event.isControlDown()) {
                            recodedEvent = recodeWithoutControlDown(event);
                            myTextArea.fireEvent(recodedEvent);
                        } else {
                            ObservableList<Node> children = parent.getChildrenUnmodifiable();
                            int idx = children.indexOf(myTextArea);
                            if (idx >= 0) {
                                for (int i = idx + 1; i < children.size(); i++) {
                                    if (children.get(i).isFocusTraversable()) {
                                        children.get(i).requestFocus();
                                        break;
                                    }
                                }
                                for (int i = 0; i < idx; i++) {
                                    if (children.get(i).isFocusTraversable()) {
                                        children.get(i).requestFocus();
                                        break;
                                    }
                                }
                            }
                        }
                        event.consume();
                        break;
                }
            }
        }

        private KeyEvent recodeWithoutControlDown(KeyEvent event) {
            return new KeyEvent(
                    event.getEventType(),
                    event.getCharacter(),
                    event.getText(),
                    event.getCode(),
                    event.isShiftDown(),
                    false,
                    event.isAltDown(),
                    event.isMetaDown()
            );
        }
    }
}