package Controllers;

import Classes.MessageBox;
import Classes.writeToDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddDoctorController implements Initializable {

    @FXML private TextField docName;
    @FXML private ComboBox docSpec;
    @FXML private Button btnSave;
    ArrayList<String> specList = new ArrayList<>();
    int id;

    //Μέθοδος για το κουμπί Cancel
    public void handleCancelDocBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("doctors.fxml")); //Καθορισμός του doctors.fxml ως Parent
        Scene DocScene = new Scene(root); //Δημιουργία του Scene "DocScene"
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Δημιουργία του Stage "window"
        window.setScene(DocScene); //Καθοριμός του Scene "DocScene" στο Stage "window"
        window.show(); //Εμφάνιση του Stage "window"
    }
    //Μέθοδος για το κουμπί Add Doctor
    public void addDoc(ActionEvent event) throws IOException {
        String name = docName.getText(); //Καταχώριση των τιμών των πεδίων στις αντίστοιχες μεταβλητές
        String spec = (String) docSpec.getValue();
        if(name.isEmpty() || docSpec.getSelectionModel().isEmpty()){ //Έλεγχος για κενά πεδία και εμφάνιση του κατάλληλου μηνύματος
            MessageBox.SimpleMBox("Please fill all the fields", "Fill all the fields");
        } else if(containsNumber(name)){ //Έλεγχος αν η τιμή του πεδίου περιέχει αριθμούς μέσω της μεθόδου "containsNumber" που δημιουργείται
            MessageBox.SimpleMBox("Name should not contain number(s)", "Error"); //παρακάτω και εμφάνιση του κατάλληλου μηνύματος
        }
        else { //Αν ολοκληρωθούν οι έλεγχοι χωρίς κάποιο σφάλμα πραγματοποιείται εγγραφή των τιμών στη "βάση" μέσω της μεθόδου "addDoctor" της κλάσης "writeToDB"
            writeToDB.addDoctor(id, name, spec);                                                       //και ανακατεύθυνση Stage "window" που δημιουργείται παρακάτω
            Parent root = FXMLLoader.load(getClass().getResource("doctors.fxml"));
            Scene DisScene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(DisScene);
            window.show();
        }
    }
    //Η μέθοδος containsNumber που δέχεται μία μεταβλητή τύπου "String" και επιστρέφει αν αυτή η μεταβλητή περιέχει αριθμούς.
    public boolean containsNumber(String s)
    {
        Pattern p = Pattern.compile( "[0-9]" );
        Matcher m = p.matcher( s );

        return m.find();
    }

    //Χρήση της μεθόδου initialize ώστε να αρχικοποιηθούν οι παρακάτω τιμές πριν την εκτέλεση του Controller
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int lastid = 0;
        String line = "";
        String delimiter = ":";
        String Docfile = "database/doctorDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Docfile))) { //Reading του αρχείου doctorDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                 lastid = Integer.parseInt(values.get(0)); //Καταχώριση της πρώτης τιμής του αρχείου στη μεταβλητή lastid
            }
            if(lastid==0) { //Αν δεν υπάρχει τιμή στο αρχείο τότε καταχωρείται στη μεταβλητή id η τιμή 1
                id = 1;     //ώστε οι εγγραφές να ξεκινήσουν από τον αριθμό 1 και όχι απο το 0
            } else {
                id = lastid + 1; //Αν υπάρχει τιμή στο αρχείο τότε η πρώτη τιμή της επόμενης εγγραφής που θα πραγματοποιηθεί θα
            }                    //θα είναι η τιμή της προηγούμενης αυξημένη κατά 1
        } catch (IOException e) {
            e.printStackTrace(); //Σε αυτό το σημείο δε πραγματοποιείται έλεγχος για την ύπαρξη του αρχείου "doctorDB", καθώς πραγματοποιείται
        }                        //στο προηγούμενο Stage και στη περίπτωση που δεν υφίσταται το συγκεκριμένο αρχείο δεν εμφανίζεται το παρόν Stage (addDoctor.fxml)

        String Disfile = "database/diseaseDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Disfile))) { //Reading του αρχείου diseaseDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                specList.add(values.get(3)); //Καταχώριση της τέταρτης τιμής της κάθε εγγραφής που περιέχει το αρχείο στη λίστα "specList"
            }
        } catch (FileNotFoundException e){ //Αν δεν υπάρχει το αρχείο "diseaseDB" τότε πραγματοποιούνται οι κατάλληλες ενέργειες
            docSpec.setValue("Error connecting to Disease Database");
            btnSave.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<String> uniqueSpec = new HashSet<>(specList); //Δημιουργία ενός HashSet από τη λίστα "specList" ώστε να εξαλειφθούν τυχόν τιμές που εμφανίζονται
        for(String s: uniqueSpec)                         //παραπάνω από μία φορά
        {
            docSpec.getItems().add(s);  //Εισαγωγή των τιμών του HashSet στα items του ComBobox "docSpec"
        }
    }

}
