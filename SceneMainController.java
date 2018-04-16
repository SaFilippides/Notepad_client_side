/*
 * SceneMainController.java
 * Klasi controller gia to parathiro SceneMain.fxml
 */
package splendidworks;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Note;

/**
 * FXML Controller class
 *
 * @author filip
 */
public class SceneMainController implements Initializable {

    // metavlites gia allilepidrasi me to GUI
    @FXML private ListView<Note> notesListView;
    @FXML private ImageView imageView;
    @FXML private TextArea noteTextArea;
    
    // apothikevi lista apo antikeimena Note
    private final ObservableList<Note> notes =
            FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // fortosi  listas ObservableList<Note> me antikeimena Note
        notes.add(new Note("sidirodromos", "/images/img1.jpg", "Απεραντο και ωραιο"));
        notes.add(new Note("kataraktis", "/images/img2.jpg", "Πολυ νερο"));
        notes.add(new Note("paralia", "/images/img3.jpg", "Μιλαμε για πολυ ΜΠΑΝΙΟ"));
        notes.add(new Note("skiouros", "/images/img4.jpg", "Αστο αν στον φαει την γαμησες"));
        notes.add(new Note("paralia2", "/images/img5.jpg", " ΠΦΦ...Στην Πρωτη γαμας και φανταζεσαι"));
        
        //sindesi tou optikou antikeimenou notesListView me ti lista notes
        notesListView.setItems(notes);
        
        // otan o xristis allazei epilogi sto ListView, deikse tin ekastote eikona kai simeiosi sta antistoixa components
        notesListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Note>() {
                @Override
                public void changed(ObservableValue<? extends Note> ov, 
                    Note old_val, Note new_val) 
                    {
                        imageView.setImage(new Image(new_val.getImageURL()));
                        System.out.println(new_val.getImageURL());
                        noteTextArea.setText(new_val.getNote());
                    }
               }
        );
        
        
    }    
    
}
