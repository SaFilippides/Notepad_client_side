/*
 * SceneMainController.java
 * Klasi controller gia to parathiro SceneMain.fxml
 */
package splendidworks;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import model.Note;
import parser.NoteJsonParser;

/**
 * FXML Controller class
 *
 * @author filip
 */
public class SceneMainController implements Initializable {

    // metavlites gia allilepidrasi me to GUI
    @FXML
    private ListView<Note> notesListView;
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private Button newBtn;

    // apothikevi lista apo antikeimena Note
    @FXML

    //Anoigma parathurou neas simeiosis
    private void handleButtonAction2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/NewNote.fxml"));
        Scene scene = new Scene(root);

        scene.getStylesheets().add("/styles/newnote.css");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("New Note");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private ObservableList<Note> notes = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Client client = ClientBuilder.newClient();
        client.register(NoteJsonParser.class);
        WebTarget clientTarget = client.target("http://localhost:8080/Notepad_server_side/note/view/{beginBy}");
        clientTarget = clientTarget.resolveTemplate("beginBy", FXMLController.getUser().getId());
        GenericType<List<Note>> listc = new GenericType<List<Note>>() {
        };
        
        try
        {
            notes = FXCollections.observableArrayList(clientTarget.request("application/json").get(listc));

            //sindesi tou optikou antikeimenou notesListView me ti lista notes
            notesListView.setItems(notes);

            // otan o xristis allazei epilogi sto ListView, deikse tin ekastote eikona kai simeiosi sta antistoixa components
            notesListView.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<Note>() {
                @Override
                public void changed(ObservableValue<? extends Note> ov,
                        Note old_val, Note new_val) {
                    if (!new_val.getImageURL().isEmpty())
                    {    
                        imageView.setImage(new Image(new_val.getImageURL()));
                        System.out.println(new_val.getImageURL());
                    }


                    noteTextArea.setText(new_val.getNote());
                    }
                }
            );
        }
        catch(Exception ex)
        {
            
        }
    }

}
