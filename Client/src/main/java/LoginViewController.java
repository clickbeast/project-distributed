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


        //configure bounds
        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setTopAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0);
    }

    public void createUserAction() {
        File directoryLocation = this.mainWindowViewController.chooseDirectoryLocation();
        if (!this.createPasswordField.equals(this.createPasswordField2)) {
            this.createAccountInfoLabel.setText("Please use matching passwords");
            return;
        }
        this.mainWindowViewController.getClientManager().createAccount(createUsernameField.getText(),
                createPasswordField.getText(), directoryLocation);
    }

    public void loginUserAction() {
        System.out.println(this.usernameField.getText());
        System.out.println(this.passwordField.getText());


        this.mainWindowViewController.getClientManager().login(this.usernameField.getText(),
                this.passwordField.getText(),
                (b) -> {
                    if(b==false) {
                        this.
                    }
                });
    }


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
