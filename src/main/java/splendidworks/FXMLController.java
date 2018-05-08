/*
 * SceneMainController.java
 * Klasi controller gia to parathiro Scene.fxml
 */
package splendidworks;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.json.Json;
import javax.ws.rs.NotAcceptableException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import model.AppUser;
import parser.UserJsonParser;

public class FXMLController implements Initializable {

    private static AppUser user;

    public static AppUser getUser() {
        return user;
    }

    public static void setUser(AppUser user) {
        FXMLController.user = user;
    }

    // metavlites gia allilepidrasi me to GUI
    @FXML
    private Button registerBtn;
    @FXML
    private Button signBtn;
    @FXML
    private TextField usernameRegister;
    @FXML
    private PasswordField passwordRegister;
    @FXML
    private TextField usernameSignIn;
    @FXML
    private PasswordField passwordSignIn;
    @FXML
    private Label toggleLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Label headerLabel;  
    

    @FXML
    // methodos pou antapokrinete sta patimata koubion
    private void handleButtonAction(ActionEvent event) throws IOException {
        String error;
        error = "";

        if (event.getSource() == registerBtn)//Το getSource επιστρεφει στο κωδικα το κουμπι που πατησε  
        {
            if (usernameRegister.getText().equals("")) {
                error = String.format("No username was given");//ενσωματωνει ενα μνμ με το format που δεινω

            }
            if (passwordRegister.getText().equals("")) {

                error += String.format("\nNo password was given");
                //error +={κανει apent το μνμ που προσθετουμε ή κανει προσθεση στο υπαρχων μνμ}   
            }

            if (error == "") {

                // paradeigma dimiourgias json model apo documentation https://docs.oracle.com/javaee/7/api/index.html?javax/json/JsonObjectBuilder.html
                String json = Json.createObjectBuilder()
                        .add("password", passwordRegister.getText())
                        .add("username", usernameRegister.getText())
                        .build()
                        .toString();

                // Set up our client and target our JAX-RS service 
                Client client = ClientBuilder.newClient();
                WebTarget target = client.target("http://localhost:8080/Notepad_server_side/user/add/");

                // Build our request JSON into an 'Entity'. Replace 'myData' with your JSON
                Entity<String> data = Entity.entity(json, MediaType.APPLICATION_JSON_TYPE);
                System.out.print("op" + data.getEntity());
                // Then send a post request to the target service
                try {
                    String result = target.request(MediaType.APPLICATION_JSON_TYPE).post(data, String.class);

                    client = ClientBuilder.newClient();
                    client.register(UserJsonParser.class);
                    WebTarget clientTarget = client.target("http://localhost:8080/Notepad_server_side/user/checkbyyname/{username}/{password}");
                    clientTarget = clientTarget.resolveTemplate("username", usernameRegister.getText());
                    clientTarget = clientTarget.resolveTemplate("password", passwordRegister.getText());
                    GenericType<List<AppUser>> listc = new GenericType<List<AppUser>>() {
                    };
                    setUser(clientTarget.request("application/json").get(listc).get(0));
                    
                    changeScene(event);
                } catch (NotAcceptableException e) 
                {
                    error = String.format("User allready exists");
                }

            }

        }
        if (event.getSource() == signBtn) {

            if (usernameSignIn.getText().equals("")) //o elegxos gia to sign in.
            {
                error = String.format("Please provide a username");
            }
            if (passwordSignIn.getText().equals("")) {
                error += String.format("\nPlease provide a password");
            }
            if (error == "") {
                
                try
                {
                    Client client = ClientBuilder.newClient();
                    client.register(UserJsonParser.class);
                    WebTarget clientTarget = client.target("http://localhost:8080/Notepad_server_side/user/checkbyyname/{username}/{password}");
                    clientTarget = clientTarget.resolveTemplate("username", usernameSignIn.getText());
                    clientTarget = clientTarget.resolveTemplate("password", passwordSignIn.getText());
                    
                    GenericType<List<AppUser>> listc = new GenericType<List<AppUser>>() {
                    };
                    setUser(clientTarget.request("application/json").get(listc).get(0));
                    changeScene(event);
                }
                catch(NotAcceptableException e)
                {
                    error = String.format("Username or password is incorrect");
                }
                
            }

        }
        errorLabel.setText(error);
    }
    
    @FXML
    /** methodos antikatastasis components signIn/register */
    private void handleToggleAction(MouseEvent event) throws IOException {
        
        String signInText = String.format("Not a member? Click here to register instead!");
        String registerText = String.format("Allready a member? Click here to login instead!");
        
        
        if (toggleLabel.getText().equals(signInText))
        {
            toggleLabel.setText(registerText);
            headerLabel.setText("Register");
            
            registerBtn.setVisible(true);
            signBtn.setVisible(false);
            
            usernameRegister.setVisible(true);
            usernameSignIn.setVisible(false);
                    
            passwordRegister.setVisible(true);
            passwordSignIn.setVisible(false);
                    
        }
        else
        {
            toggleLabel.setText(signInText);
            headerLabel.setText("Sign In");
            
            registerBtn.setVisible(false);
            signBtn.setVisible(true);
            
            usernameRegister.setVisible(false);
            usernameSignIn.setVisible(true);
                    
            passwordRegister.setVisible(false);
            passwordSignIn.setVisible(true);
        }
    }

    // methodos allagis apo parathiro login/register sto kirio parathiro tis efarmogis
    private void changeScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SceneMain.fxml"));
        Scene scene = new Scene(root);

        scene.getStylesheets().add("/styles/scenemain.css");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Notepad");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
