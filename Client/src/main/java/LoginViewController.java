import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.*;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class LoginViewController implements Initializable {


    /* VARIABLES ------------------------------------------------------------------ */


    public TextField usernameField;
    public PasswordField passwordField;
    public TextField createUsernameField;
    public PasswordField createPasswordField;
    public TextField createPasswordField2;
    public Button loginButton;
    public Button createAccountButton;
    public AnchorPane container;

    public Label createAccountInfoLabel;
    public Label loginAccountInfoLabel;


    public MainWindowViewController mainWindowViewController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize Login View");

        this.loginButton.setDefaultButton(true);
        this.createAccountButton.setDefaultButton(true);

        //configure bounds
        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setTopAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0);
    }

    /* UI ACTIONS ------------------------------------------------------------------ */


    public void createUserAction() {

        if (this.createPasswordField.getText().isEmpty() || this.createPasswordField2.getText().isEmpty()
                || this.createUsernameField.getText().isEmpty()) {
            this.loginAccountInfoLabel.setText("Please fill in all fields.");
            return;

        }

        if (!this.createPasswordField.getText().equals(this.createPasswordField2.getText())) {
            this.createAccountInfoLabel.setText("Please use matching passwords");
            return;
        }
        File directoryLocation = this.mainWindowViewController.chooseDirectoryLocation();
        this.mainWindowViewController.getClientManager().createAccount(createUsernameField.getText(),
                createPasswordField.getText(), directoryLocation);
    }

    public void loginUserAction() {

        if (this.usernameField.getText().isEmpty() || this.passwordField.getText().isEmpty()) {
            this.loginAccountInfoLabel.setText("Please fill in all fields.");
            return;

        }

        this.mainWindowViewController.getClientManager().login(this.usernameField.getText(),
                this.passwordField.getText(),
                (b) -> {
                    if (!b.isSucces()) {
                        this.loginAccountInfoLabel.setText(b.getMessage());
                    }
                });
    }

    public void changeDBAction() {
        System.out.println("Changing DB");

    }
    /*
     * GETTERS & SETTERS
     * -----------------------------------------------------------------------------
     *
     */


    public TextField getUsernameField() {
        return usernameField;
    }

    public void setUsernameField(TextField usernameField) {
        this.usernameField = usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(PasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public TextField getCreateUsernameField() {
        return createUsernameField;
    }

    public void setCreateUsernameField(TextField createUsernameField) {
        this.createUsernameField = createUsernameField;
    }

    public PasswordField getCreatePasswordField() {
        return createPasswordField;
    }

    public void setCreatePasswordField(PasswordField createPasswordField) {
        this.createPasswordField = createPasswordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(Button loginButton) {
        this.loginButton = loginButton;
    }

    public Button getCreateAccountButton() {
        return createAccountButton;
    }

    public void setCreateAccountButton(Button createAccountButton) {
        this.createAccountButton = createAccountButton;
    }

    public AnchorPane getContainer() {
        return container;
    }

    public void setContainer(AnchorPane container) {
        this.container = container;
    }

    public MainWindowViewController getMainWindowViewController() {
        return mainWindowViewController;
    }

    public void setMainWindowViewController(MainWindowViewController mainWindowViewController) {
        this.mainWindowViewController = mainWindowViewController;
    }
}
