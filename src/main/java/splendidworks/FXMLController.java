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

public class FXMLController implements Initializable {
    
    // metavlites gia allilepidrasi me to GUI
    @FXML private Button registerBtn;
    @FXML private Button signBtn;
    @FXML private TextField usernameRegister;
    @FXML private TextField passwordRegister;
    
    @FXML
    // methodos pou antapokrinete sta patimata koubion
    private void handleButtonAction(ActionEvent event) throws IOException {
        String error;
        error="";
        if(usernameRegister.getText().equals(""))
        {
            error = String.format("No username was given");//ενσωματωνει ενα μνμ με το format που δεινω
            
        }
        if(passwordRegister.getText().equals(""))
        {
            
             error += String.format("\nNo password was given");
             //error +={κανει apent το μνμ που προσθετουμε ή κανει προσθεση στο υπαρχων μνμ}   
        }
        
        
        if (event.getSource() == registerBtn && error=="")//Το getSource επιστρεφει στο κωδικα το κουμπι που πατησε  
        {
            System.out.println("ok");
            changeScene(event);
        }
        if (event.getSource() == signBtn)
        {
            System.out.println("ok2");
            changeScene(event);
            
        }
        System.out.println(error);
    }
    
    // methodos allagis apo parathiro login/register sto kirio parathiro tis efarmogis
    private void changeScene(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SceneMain.fxml"));
        Scene scene = new Scene(root);
        
        scene.getStylesheets().add("/styles/scenemain.css");
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow(); 
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
