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

public class NewAppointmentController implements Initializable {

    @FXML private ComboBox patName;
    @FXML private TextField patTel;
    @FXML private ComboBox docName;
    @FXML private DatePicker appointDate;
    @FXML private ComboBox Hpicker;
    @FXML private ComboBox Mpicker;
    @FXML private ComboBox dis;
    @FXML private TextArea treat;
    @FXML private Button btnSave;

    ObservableList<String> docs = FXCollections.observableArrayList();
    ObservableList<String> dermDis = FXCollections.observableArrayList();
    ObservableList<String> gastroDis = FXCollections.observableArrayList();
    ObservableList<String> neuroDis = FXCollections.observableArrayList();
    ObservableList<String> allDis = FXCollections.observableArrayList();
    int id;

    //Μέθοδος για το κουμπί Cancel
    public void handleCancelAppointBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("appointments.fxml")); //Καθορισμός του appointments.fxml ως Parent
        Scene AppointScene = new Scene(root); //Δημιουργία του Scene "AppointScene"
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Δημιουργία του Stage "window"
        window.setScene(AppointScene); //Καθοριμός του Scene "AppointScene" στο Stage "window"
        window.show(); //Εμφάνιση του Stage "window"
    }
    //Μέθοδος για το κουμπί New Appointment
    public void newAppoint(ActionEvent event) throws IOException {
        String pname = (String)  patName.getValue(); //Καταχώριση των τιμών των πεδίων στις αντίστοιχες μεταβλητές
        String tel = patTel.getText();
        String dname = (String) docName.getValue();
        String date = String.valueOf(appointDate.getValue());
        String datetime = Hpicker.getValue() + "." + Mpicker.getValue();
        String patdis = (String) dis.getValue();
        String pattreat = treat.getText();

        //Έλεγχος για κενά πεδία και εμφάνιση του κατάλληλου μηνύματος
        if(patName.getSelectionModel().isEmpty() || tel.isEmpty() || docName.getSelectionModel().isEmpty() || appointDate.getValue() == null  || Hpicker.getSelectionModel().isEmpty() || Mpicker.getSelectionModel().isEmpty() || dis.getSelectionModel().isEmpty() || pattreat.isEmpty()){
            MessageBox.SimpleMBox("Please fill all the fields", "Fill all the fields");
        } else if (!validateMobileNo(patTel)) {
            MessageBox.SimpleMBox("Please enter a valid phone number", "Error");
        } else { //Αν ολοκληρωθεί ο παραπάνω έλεγχος χωρίς κάποιο σφάλμα πραγματοποιείται εγγραφή των τιμών στη "βάση" μέσω της μεθόδου "newAppointment" της κλάσης "writeToDB"
            writeToDB.newAppointment(id, pname, tel, dname, date, datetime, patdis, pattreat);                        //και ανακατεύθυνση Stage "window" που δημιουργείται παρακάτω
            Parent root = FXMLLoader.load(getClass().getResource("appointments.fxml"));
            Scene DisScene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(DisScene);
            window.show();
        }
    }
    //Η μέθοδος validateMobileNo που δέχεται ένα Textfield και επιστρέφει αν το περιεχόμενο του αντιστοιχεί σε πραγματικό αριθμό τηλεφώνου δέκα ψηφίων.
    private boolean validateMobileNo(TextField tel) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(tel.getText());
        return m.find();
    }

    //Η μέθοδος docSpecChoice που θέτει τις τιμές της κατάλληλης λίστας, ανάλογα με το όνομα του γιατρού, στα items του Combobox "dis"
   @FXML
    private void docSpecChoice(){
        if(docName.getValue().equals("Dr Cheek")){
            dis.setItems(dermDis);
        }
        else if(docName.getValue().equals("Dr Brown")){
            dis.setItems(gastroDis);
        }
        else if(docName.getValue().equals("Dr Johnathan Treat Paine")){
            dis.setItems(neuroDis);
        } else {
            dis.setItems(allDis);
        }
    }

    //Χρήση της μεθόδου initialize ώστε να αρχικοποιηθούν οι παρακάτω τιμές πριν την εκτέλεση του Controller
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Hpicker.setValue("Hour"); //Εισαγωγή των κατάλληλων τιμών στα ComboBoxes "Hpicker" και "Mpicker"
        Mpicker.setValue("Minutes");
        for(int l=8; l<10; l++){
            Hpicker.getItems().add("0" + l);
        }
        for(int i=10; i<22; i++){
            Hpicker.getItems().add(i);
        }
        for(int k=0; k<10; k++){
            Mpicker.getItems().add("0" + k);
        }
        for(int j=10; j<61; j++){
            Mpicker.getItems().add(j);
        }

        String line = "";
        String delimiter = ":";
        String Docfile = "database/doctorDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Docfile))) { //Reading του αρχείου doctorDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                docs.add(values.get(1)); //Καταχώριση της δεύτερης τιμής της κάθε εγγραφής που περιέχει το αρχείο στη λίστα "docs"
            }
            docName.setItems(docs); //Εισαγωγή των τιμών της λίστας docs στα items του ComboBox "docName"
        } catch (FileNotFoundException e){ //Αν δεν υπάρχει το αρχείο "doctorDB" τότε πραγματοποιούνται οι κατάλληλες ενέργειες
            docName.setValue("Error connecting to Doctor Database");
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
                    dermDis.add(values.get(1));            //με το περιεχόμενο της τέταρτης τιμής κάθε εγγραφής του αρχείου
                }
                else if (values.get(3).equals("Gastroenterology")){
                    gastroDis.add(values.get(1));
                }
                else if (values.get(3).equals("Neurology")){
                    neuroDis.add(values.get(1));
                }
            }
            dis.getItems().add("Please select a doctor"); //Εισαγωγή τιμής στα items του ComboBox "dis"
        } catch (FileNotFoundException e){ //Αν δεν υπάρχει το αρχείο "diseaseDB" τότε πραγματοποιούνται οι κατάλληλες ενέργειες
            dis.setValue("Error connecting to Disease Database");
            btnSave.setDisable(true);
        } catch(IOException e){
            e.printStackTrace();
        }

        String Patfile = "database/patientDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Patfile))) { //Reading του αρχείου patientDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                patName.getItems().add(values.get(1)); //Καταχώριση της δεύτερης τιμής της κάθε εγγραφής που περιέχει το αρχείο στo ComboBox "patName"
            }
        } catch (FileNotFoundException e){ //Αν δεν υπάρχει το αρχείο "patientDB" τότε πραγματοποιούνται οι κατάλληλες ενέργειες
            patName.setValue("Error connecting to Patient Database");
            btnSave.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int lastid = 0;
        String Appointfile = "database/appointmentDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Appointfile))) { //Reading του αρχείου appointmentDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                lastid = Integer.parseInt(values.get(0)); //Καταχώριση της πρώτης τιμής του αρχείου στη μεταβλητή lastid
            }
            if(lastid==0) {  //Αν δεν υπάρχει τιμή στο αρχείο τότε καταχωρείται στη μεταβλητή id η τιμή 1
                id = 1;      //ώστε οι εγγραφές να ξεκινήσουν από τον αριθμό 1 και όχι απο το 0
            } else {
                id = lastid + 1;  //Αν υπάρχει τιμή στο αρχείο τότε η πρώτη τιμή της επόμενης εγγραφής που θα πραγματοποιηθεί θα
            }                     //θα είναι η τιμή της προηγούμενης αυξημένη κατά 1
        } catch (IOException e) {
            e.printStackTrace();  //Σε αυτό το σημείο δε πραγματοποιείται έλεγχος για την ύπαρξη του αρχείου "appointmentDB", καθώς πραγματοποιείται
        }                         //στο προηγούμενο Stage και στη περίπτωση που δεν υφίσταται το συγκεκριμένο αρχείο δεν εμφανίζεται το παρόν Stage (newAppointment.fxml)
    }
}




