package Controllers;

import Classes.MessageBox;
import Classes.Patient;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> idColumn;
    @FXML private TableColumn<Patient, String> patientNameColumn;
    @FXML private TableColumn<Patient, String> patientDOBColumn;
    @FXML private TableColumn<Patient, Long> patientTelColumn;
    @FXML private TableColumn<Patient, String> patientGenderColumn;
    @FXML private TableColumn<Patient, String> patientDoctorName;
    @FXML private TableColumn<Patient, String> diseaseColumn;
    @FXML private TableColumn<Patient, String> treatmentColumn;
    @FXML private Button btnAddPat;
    @FXML private Button btnCreateDatabase;

    //Μέθοδος για το κουμπί Manage Appointments
    public void handleAppointBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("appointments.fxml")); //Καθορισμός του appointments.fxml ως Parent
        Scene AppointScene = new Scene(root); //Δημιουργία του Scene "AppointScene"
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow(); //Δημιουργία του Stage "window"
        window.setScene(AppointScene); //Καθοριμός του Scene "AppointScene" στο Stage "window"
        window.show(); //Εμφάνιση του Stage "window"
    }
    //Μέθοδος για το κουμπί Manage Doctors
    public void handleDocBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("doctors.fxml"));
        Scene DocScene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(DocScene);
        window.show();
    }
    //Μέθοδος για το κουμπί Manage Diseases
    public void handleDisBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("diseases.fxml"));
        Scene DisScene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(DisScene);
        window.show();
    }
    //Μέθοδος για το κουμπί Add Patients
    public void handleAddPatBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addPatient.fxml"));
        Scene DisScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DisScene);
        window.show();
    }
    //Μέθοδος για το κουμπί Create Database
    public void handleCreateDBbtn(ActionEvent event) {
        try {
            FileWriter myWriter = new FileWriter("database/patientDB.txt", true);
            myWriter.write("");
            myWriter.close();
            MessageBox.SimpleMBox("Database created successfully!", "Success");
            Parent root = FXMLLoader.load(getClass().getResource("patients.fxml"));
            Scene DisScene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(DisScene);
            window.show();
        }catch (IOException d) {
            MessageBox.SimpleMBox("Error creating database", "Database");
            d.printStackTrace();
        }
    }

    //Χρήση της μεθόδου initialize ώστε να αρχικοποιηθούν οι παρακάτω τιμές πριν την εκτέλεση του Controller
    @Override
    public void initialize(URL url, ResourceBundle rb){
        //Εισαγωγή των τιμών των Object της κλάσης "Doctor" στα columns του Table "patientTable"
        idColumn.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("id"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
        patientDOBColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("dateOfBirth"));
        patientTelColumn.setCellValueFactory(new PropertyValueFactory<Patient, Long>("tel"));
        patientGenderColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
        patientDoctorName.setCellValueFactory(new PropertyValueFactory<Patient, String>("doctor"));
        diseaseColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("disease"));
        treatmentColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("treatment"));

        patientTable.setItems(getPatients()); //Εισαγωγή των τιμών που επιστρέφει η μέθοδος "getPatients()" στο πίνακα "patientTable"
    }
    public ObservableList<Patient> getPatients() {

        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String file = "database/patientDB.txt";
        String line = "";
        String delimiter = ":";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { //Reading του αρχείου patientDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                int id = Integer.parseInt(values.get(0));
                long tel = Long.parseLong(values.get(4));
                //Όσο το αρχείο περιέχει εγγραφές, δημιουργούνται objects με τα περιεχόμενα των εγγραφών αυτών και εισάγωνται στη λίστα "patients"
                patients.add(new Patient(id, values.get(1), values.get(2), values.get(3), tel, values.get(5), values.get(6), values.get(7)));
            }
        } catch (FileNotFoundException e){ //Αν δεν υπάρχει το αρχείο "patientDB" τότε πραγματοποιούνται οι κατάλληλες ενέργειες
            MessageBox.SimpleMBox("Could not connect to Database", "Error");
            btnAddPat.setVisible(false);
            btnCreateDatabase.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patients;
    }
}
