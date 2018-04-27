/*
 * SceneMainController.java
 * Klasi controller gia to parathiro Scene.fxml
 */
package splendidworks;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.json.Json;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

public class FXMLController implements Initializable {

    // metavlites gia allilepidrasi me to GUI
    @FXML
    private Button registerBtn;
    @FXML
    private Button signBtn;
    @FXML
    private TextField usernameRegister;
    @FXML
    private TextField passwordRegister;
    @FXML
    private TextField usernameSignIn;
    @FXML
    private TextField passwordSignIn;

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
                String result = target.request(MediaType.APPLICATION_JSON_TYPE).post(data, String.class);

                //changeScene(event);
            }

        }
        if (event.getSource() == signBtn) {

            if (usernameSignIn.getText().equals("")) //o elegxos gia to sign in.
            {
                error = String.format("You need to have a username");
            }
            if (passwordSignIn.getText().equals("")) {
                error += String.format("\nYou need to have a password");
            }
            if (error == "") {
                System.out.println("ok2");
                changeScene(event);
            }

        }
        System.out.println(error);
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
