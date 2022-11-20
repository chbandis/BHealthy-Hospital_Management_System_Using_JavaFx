package Controllers;

import Classes.MessageBox;
import Classes.writeToDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddPatientController implements Initializable {

    @FXML private TextField patName;
    @FXML private DatePicker patDOB;
    @FXML private TextField patTel;
    @FXML private RadioButton patMale;
    @FXML private RadioButton patFemale;
    @FXML private ComboBox patDoc;
    @FXML private ComboBox patDis;
    @FXML private TextArea patTreat;
    @FXML private Button btnSave;

    ObservableList<String> docs = FXCollections.observableArrayList();
    ObservableList<String> dermDis = FXCollections.observableArrayList();
    ObservableList<String> gastroDis = FXCollections.observableArrayList();
    ObservableList<String> neuroDis = FXCollections.observableArrayList();
    ObservableList<String> allDis = FXCollections.observableArrayList();
    int id;

    //Μέθοδος για το κουμπί Cancel
    public void handleCancelPatBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("patients.fxml")); //Καθορισμός του patients.fxml ως Parent
        Scene PatScene = new Scene(root); //Δημιουργία του Scene "PatScene"
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();  //Δημιουργία του Stage "window"
        window.setScene(PatScene); //Καθοριμός του Scene "PatScene" στο Stage "window"
        window.show(); //Εμφάνιση του Stage "window"
    }
    //Μέθοδος για το κουμπί Add Patient
    public void addDPat(ActionEvent event) throws IOException {
        String name = patName.getText(); //Καταχώριση των τιμών των πεδίων στις αντίστοιχες μεταβλητές
        String tel = patTel.getText();
        String gender = "";
        String dob = String.valueOf(patDOB.getValue());
        String docName = (String) patDoc.getValue();
        String dis = (String) patDis.getValue();
        String treat = patTreat.getText();

        if (patMale.isSelected()){ //Έλεγχος επιλογής των RadioButtons
            gender = "Male";
        }
        else if (patFemale.isSelected()){
            gender = "Female";
        }
        //Έλεγχος για κενά πεδία και εμφάνιση του κατάλληλου μηνύματος
        if(name.isEmpty() || gender.isEmpty() || patDOB.getValue() == null || tel.isEmpty() || patDoc.getSelectionModel().isEmpty() || patDis.getSelectionModel().isEmpty() || treat.isEmpty()){
            MessageBox.SimpleMBox("Please fill all the fields", "Fill all the fields");
        } else if (containsNumber(name)){ //Έλεγχος αν η τιμή του πεδίου περιέχει αριθμούς μέσω της μεθόδου "containsNumber" που δημιουργείται
            MessageBox.SimpleMBox("Name should not contain number(s)", "Error"); //παρακάτω και εμφάνιση του κατάλληλου μηνύματος
        } else if (!validateMobileNo(patTel)) {
            MessageBox.SimpleMBox("Please enter a valid phone number", "Error");
        }
        else { //Αν ολοκληρωθούν οι έλεγχοι χωρίς κάποιο σφάλμα πραγματοποιείται εγγραφή των τιμών στη "βάση" μέσω της μεθόδου "addPatient" της κλάσης "writeToDB"
            writeToDB.addPatient(id, name, gender, dob, tel, docName, dis, treat);                       //και ανακατεύθυνση Stage "window" που δημιουργείται παρακάτω
            Parent root = FXMLLoader.load(getClass().getResource("patients.fxml"));
            Scene DisScene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(DisScene);
            window.show();
        }
    }
    //Η μέθοδος containsNumber που δέχεται μία μεταβλητή τύπου "String" και επιστρέφει αν αυτή η μεταβλητή περιέχει αριθμούς.
    public boolean containsNumber(String str) {
        Pattern p = Pattern.compile( "[0-9]" );
        Matcher m = p.matcher(str);

        return m.find();
    }
    //Η μέθοδος validateMobileNo που δέχεται ένα Textfield και επιστρέφει αν το περιεχόμενο του αντιστοιχεί σε πραγματικό αριθμό τηλεφώνου δέκα ψηφίων.
    private boolean validateMobileNo(TextField tel) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(tel.getText());
        return m.find();
    }

    //Η μέθοδος docSpecChoice που θέτει τις τιμές της κατάλληλης λίστας, ανάλογα με το όνομα του γιατρού, στα items του Combobox "patDis"
    @FXML
    private void docSpecChoice(){
        if(patDoc.getValue().equals("Dr Cheek")){
            patDis.setItems(dermDis);
        }
        else if(patDoc.getValue().equals("Dr Brown")){
            patDis.setItems(gastroDis);
        }
        else if(patDoc.getValue().equals("Dr Johnathan Treat Paine")){
            patDis.setItems(neuroDis);
        } else {
            patDis.setItems(allDis);
        }
    }

    //Χρήση της μεθόδου initialize ώστε να αρχικοποιηθούν οι παρακάτω τιμές πριν την εκτέλεση του Controller
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String line = "";
        String delimiter = ":";
        String Docfile = "database/doctorDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Docfile))) { //Reading του αρχείου doctorDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                docs.add(values.get(1)); //Καταχώριση της δεύτερης τιμής της κάθε εγγραφής που περιέχει το αρχείο στη λίστα "docs"
            }
            patDoc.setItems(docs); //Εισαγωγή των τιμών της λίστας docs στα items του ComboBox "patDoc"
        } catch (FileNotFoundException e){ //Αν δεν υπάρχει το αρχείο "doctorDB" τότε πραγματοποιούνται οι κατάλληλες ενέργειες
            patDoc.setValue("Error connecting to Doctor Database");
            btnSave.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dermDisfile = "database/diseaseDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(dermDisfile))) { //Reading του αρχείου diseaseDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                allDis.add(values.get(1));
                if (values.get(3).equals("Dermatology")){ //Καταχώριση της δεύτερης τιμής της κάθε εγγραφής που περιέχει το αρχείο στη κατάλληλη λίστα, ανάλογα
                    dermDis.add(values.get(1));           //με το περιεχόμενο της τέταρτης τιμής κάθε εγγραφής του αρχείου
                }
                else if (values.get(3).equals("Gastroenterology")){
                    gastroDis.add(values.get(1));
                }
                else if (values.get(3).equals("Neurology")){
                    neuroDis.add(values.get(1));
                }
            }
            patDis.getItems().add("Please select a doctor"); //Εισαγωγή τιμής στα items του ComboBox "patDis"
        } catch (FileNotFoundException e){ //Αν δεν υπάρχει το αρχείο "diseaseDB" τότε πραγματοποιούνται οι κατάλληλες ενέργειες
            patDis.setValue("Error connecting to Disease Database");
            btnSave.setDisable(true);
        } catch(IOException e){
            e.printStackTrace();
        }

        int lastid = 0;
        String Patfile = "database/patientDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Patfile))) { //Reading του αρχείου doctorDB.txt
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
            e.printStackTrace(); //Σε αυτό το σημείο δε πραγματοποιείται έλεγχος για την ύπαρξη του αρχείου "patientDB", καθώς πραγματοποιείται
        }                        //στο προηγούμενο Stage και στη περίπτωση που δεν υφίσταται το συγκεκριμένο αρχείο δεν εμφανίζεται το παρόν Stage (addPatient.fxml)
    }
}
