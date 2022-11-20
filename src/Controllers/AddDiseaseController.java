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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddDiseaseController implements Initializable {

    @FXML private TextField disName;
    @FXML private TextArea disDesc;
    @FXML private TextField disCat;
    int id;

    //Μέθοδος για το κουμπί Cancel
    public void handleCancelDisBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("diseases.fxml")); //Καθορισμός του diseases.fxml ως Parent
        Scene DisScene = new Scene(root); //Δημιουργία του Scene "DisScene"
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Δημιουργία του Stage "window"
        window.setScene(DisScene); //Καθοριμός του Scene "DisScene" στο Stage "window"
        window.show(); //Εμφάνιση του Stage "window"
    }
    //Μέθοδος για το κουμπί Add Disease
    public void addDis(ActionEvent event) throws IOException {
        String name = disName.getText(); //Καταχώριση των τιμών των πεδίων στις αντίστοιχες μεταβλητές
        String desc = disDesc.getText();
        String cat = disCat.getText();
        if(name.isEmpty() || desc.isEmpty() || cat.isEmpty()){ //Έλεγχος για κενά πεδία και εμφάνιση του κατάλληλου μηνύματος
            MessageBox.SimpleMBox("Please fill all the fields", "Fill all the fields");
        } else if(containsNumber(name) || containsNumber(desc) || containsNumber(cat)){ //Έλεγχος αν οι τιμές των πεδίων περιέχουν αριθμούς μέσω της μεθόδου "containsNumber"
            MessageBox.SimpleMBox("Textfields should not contain number(s)", "Error");      //που δημιουργείται παρακάτω και εμφάνιση του κατάλληλου μηνύματος
        }
        else { //Αν ολοκληρωθούν οι έλεγχοι χωρίς κάποιο σφάλμα πραγματοποιείται εγγραφή των τιμών στη "βάση" μέσω της μεθόδου "addDisease" της κλάσης "writeToDB"
            writeToDB.addDisease(id, name, desc, cat);                                                   //και ανακατεύθυνση Stage "window" που δημιουργείται παρακάτω
            Parent root = FXMLLoader.load(getClass().getResource("diseases.fxml"));
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
        String Disfile = "database/diseaseDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Disfile))) { //Reading του αρχείου diseaseDB.txt
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
            e.printStackTrace(); //Σε αυτό το σημείο δε πραγματοποιείται έλεγχος για την ύπαρξη του αρχείου "diseaseDB", καθώς πραγματοποιείται
        }                        //στο προηγούμενο Stage και στη περίπτωση που δεν υφίσταται το συγκεκριμένο αρχείο δεν εμφανίζεται το παρόν Stage (addDisease.fxml)
    }
}

