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
import javafx.stage.Stage;

public class FXMLController implements Initializable {
    
    // metavlites gia allilepidrasi me to GUI
    @FXML private Button registerBtn;
    @FXML private Button signBtn;
    
    
    @FXML
    // methodos pou antapokrinete sta patimata koubion
    private void handleButtonAction(ActionEvent event) throws IOException {
        
        if (event.getSource() == registerBtn)
        {
            System.out.println("ok");
            changeScene(event);
        }
        else if (event.getSource() == signBtn)
        {
            System.out.println("ok2");
            changeScene(event);
        }
        
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
        stage.show();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
