package splendidworks;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.json.Json;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import utility.DateStamp;
import utility.GeoLocation;

/**
 * FXML Controller class
 *
 * @author filip
 */
public class EditNoteController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private FileChooser fileChooser;
    private File selectedFile;
    private String imgName = "";
    private String location = "";
    private String date = "";
    

    @FXML
    private TextField noteNameTf;

    @FXML
    private TextArea noteTa;

    @FXML
    private Label fileChooserLbl;
    
    @FXML
    private CheckBox locationCheck;
    
    @FXML
    private CheckBox dateCheck;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        noteNameTf.setText(SceneMainController.getNoteObject().getName());
        noteTa.setText(SceneMainController.getNoteObject().getNote());
    }

    @FXML
    // methodos pou antapokrinete sta patimata koubion
    private void handleButtonAction3(ActionEvent event) throws IOException {

        if (!imgName.equals("")) {
            imgName = "http://localhost:8080/uploads/" + imgName;

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost post = new HttpPost("http://localhost:8080/Notepad_server_side/upload/image");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("file", selectedFile, ContentType.DEFAULT_BINARY, selectedFile.getName());
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            HttpResponse response = httpclient.execute(post);

        }
        
        if (locationCheck.isSelected())
        {
            location = GeoLocation.getCity();
        }
        if (dateCheck.isSelected())
        {
            date = DateStamp.getDate();
        }
        
        
        // paradeigma dimiourgias json model apo documentation https://docs.oracle.com/javaee/7/api/index.html?javax/json/JsonObjectBuilder.html
        String json = Json.createObjectBuilder()
                .add("name", noteNameTf.getText())
                .add("note", noteTa.getText())
                .add("imagePath", imgName)
                .add("user_id", FXMLController.getUser().getId())
                .add("date", date)
                .add("city", location)
                .build()
                .toString();

        // Set up our client and target our JAX-RS service 
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/Notepad_server_side/note/update");
        // Build our request JSON into an 'Entity'. Replace 'myData' with your JSON
        Entity<String> data = Entity.entity(json, MediaType.APPLICATION_JSON_TYPE);

        // Then send a post request to the target service
        String result = target.request(MediaType.APPLICATION_JSON_TYPE).put(data, String.class);
        closeButtonAction();
    }

    @FXML
    // methodos pou antapokrinete sta patimata koubion
    private void handleFileChooser(ActionEvent event) throws IOException {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.setInitialDirectory(new File("C:\\Users"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        Stage stage = (Stage) noteTa.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            fileChooserLbl.setText(selectedFile.getAbsolutePath());
            imgName = selectedFile.getName();
        }

    }

    @FXML
    private void closeButtonAction() {
        // handle gia to stage
        Stage stage = (Stage) noteTa.getScene().getWindow();
        // kleisimo stage...
        stage.close();
    }

}
