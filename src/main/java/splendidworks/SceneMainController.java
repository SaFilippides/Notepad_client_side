
/*
 * SceneMainController.java
 * Klasi controller gia to parathiro SceneMain.fxml
 */
package splendidworks;

import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import model.Note;
import parser.NLPtokenizer;
import parser.NoteJsonParser;

/**
 * FXML Controller class
 *
 * @author filip
 */
public class SceneMainController implements Initializable {

    private static Note note = new Note();
    // metavlites gia allilepidrasi me to GUI
    @FXML
    private ListView<Note> notesListView;
    @FXML
    private ListView<Note> toDoListView;
    @FXML
    private ListView<Note> datepickerlist;
    @FXML
    private ListView<Note> cityNamesListView;
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private DatePicker datepicker;
    @FXML
    private ChoiceBox choice;

    // apothikevi lista apo antikeimena Note
    @FXML
    //Anoigma parathurou neas simeiosis
    private void handleButtonAction2(ActionEvent event) throws IOException {

        Button button = (Button) event.getSource();

        if (button.getText().matches("New")) {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NewNote.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New Note");
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();
            stage.setOnHidden(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    initialize(null, null);
                }
            });
        } else if (button.getText().matches("Logout")) {
            FXMLController.setUser(null);

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/NewNote.fxml"));
            Scene scene = new Scene(root);

            root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

            scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Login or Register");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML
    private void handleCalendar(Event event) throws IOException {

        LocalDate localDate = datepicker.getValue();
        for (Note note : notes) {
            //System.out.println("TEST " + localDate + " " + note.getDate());
            if (localDate.toString().equals(note.getDate())) {
                //System.out.println("check");
                notesByDate.add(note);
            }
        }
        datepickerlist.setItems(notesByDate);
        notesByDate = FXCollections.observableArrayList(); //flush
    }

    public static Note getNoteObject() {
        return note;
    }

    @FXML
    private void editNoteAction(ActionEvent event) throws IOException {

        Button button = (Button) event.getSource();

        // paradeigma dimiourgias json model apo documentation https://docs.oracle.com/javaee/7/api/index.html?javax/json/JsonObjectBuilder.html
        Client client = ClientBuilder.newClient();

        if (button.getText().matches("Update")) {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EditNote.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Note");
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();
            stage.setOnHidden(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    initialize(null, null);

                }
            });
        } else if (button.getText().matches("Delete")) {
            WebTarget target = client.target("http://localhost:8080/Notepad_server_side/note/delete/{beginBy}");
            target = target.resolveTemplate("beginBy", note.getId());
            System.out.println("eee" + note.getId());
            target.request("application/json").get();
            initialize(null, null);
        } else if (button.getText().matches("Export")) {

            XStream xstream = new XStream();
            String xml = xstream.toXML(note);
            PrintWriter writer = new PrintWriter(note.getName() + ".xml", "UTF-8");
            writer.print(xml);
            writer.close();
        }

    }

    private ObservableList<Note> notes = null;
    private ObservableList<Note> notesByDate = FXCollections.observableArrayList();
    private ObservableList<String> cityNames = FXCollections.observableArrayList();
    private ObservableList<Note> cityNamesList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imageView.setImage(null);
        noteTextArea.setText("");
        Client client = ClientBuilder.newClient();
        client.register(NoteJsonParser.class);
        WebTarget clientTarget = client.target("http://localhost:8080/Notepad_server_side/note/view/{beginBy}");
        clientTarget = clientTarget.resolveTemplate("beginBy", FXMLController.getUser().getId());
        GenericType<List<Note>> listc = new GenericType<List<Note>>() {
        };

        try {
            notes = FXCollections.observableArrayList(clientTarget.request("application/json").get(listc));
            NLPtokenizer tokenizer = new NLPtokenizer();

            //sindesi tou optikou antikeimenou notesListView me ti lista notes
            notesListView.setItems(notes);
            toDoListView.setItems(tokenizer.getFilteredNotes(notes));

            //choicebox
            String temp = "";
            for (Note note : notes) {
                if (!temp.equals(note.getCity())) {
                    temp = note.getCity();
                    cityNames.add(temp);
                }
            }
            choice.setItems(cityNames);
            cityNames = FXCollections.observableArrayList();// flush

            // otan o xristis allazei epilogi sto ListView, deikse tin ekastote eikona kai simeiosi sta antistoixa components
            notesListView.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<Note>() {
                @Override
                public void changed(ObservableValue<? extends Note> ov,
                        Note old_val, Note new_val) {
                    if (new_val != null) {
                        imageView.setImage(new Image(new_val.getImageURL()));
                        note = new_val;
                        noteTextArea.setText(new_val.getNote());
                    }
                }
            }
            );

            toDoListView.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<Note>() {
                @Override
                public void changed(ObservableValue<? extends Note> ov,
                        Note old_val, Note new_val) {
                    if (new_val != null) {
                        imageView.setImage(new Image(new_val.getImageURL()));
                        note = new_val;
                        noteTextArea.setText(new_val.getNote());
                    }
                }
            }
            );

            datepickerlist.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<Note>() {
                @Override
                public void changed(ObservableValue<? extends Note> ov,
                        Note old_val, Note new_val) {
                    if (new_val != null) {
                        imageView.setImage(new Image(new_val.getImageURL()));
                        note = new_val;
                        noteTextArea.setText(new_val.getNote());
                    }
                }
            }
            );

            choice.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov,
                        String old_val, String new_val) {

                    for (Note note : notes) {

                        if (new_val.equals(note.getCity())) {
                            //System.out.println("check");
                            cityNamesList.add(note);
                        }
                    }
                    cityNamesListView.setItems(cityNamesList);
                    cityNamesList = FXCollections.observableArrayList(); //flush                    
                }
            }
            );

            cityNamesListView.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<Note>() {
                @Override
                public void changed(ObservableValue<? extends Note> ov,
                        Note old_val, Note new_val) {
                    if (new_val != null) {
                        imageView.setImage(new Image(new_val.getImageURL()));
                        note = new_val;
                        noteTextArea.setText(new_val.getNote());
                    }
                }
            }
            );

        } catch (Exception ex) {

        }
    }

}
