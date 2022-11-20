package Controllers;

import Classes.Doctor;
import Classes.MessageBox;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class DoctorController implements Initializable {

    @FXML private TableView<Doctor> DoctorTable;
    @FXML private TableColumn<Doctor, Integer> doctorIDColumn;
    @FXML private TableColumn<Doctor, String> doctorNameColumn;
    @FXML private TableColumn<Doctor, String> doctorSpecialisationColumn;
    @FXML private Button btnAddDoc;
    @FXML private Button btnCreateDatabase;

    //Μέθοδος για το κουμπί Manage Appointments
    public void handleAppointBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("appointments.fxml"));  //Καθορισμός του appointments.fxml ως Parent
        Scene AppointScene = new Scene(root); //Δημιουργία του Scene "AppointScene"
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();  //Δημιουργία του Stage "window"
        window.setScene(AppointScene); //Καθοριμός του Scene "AppointScene" στο Stage "window"
        window.show(); //Εμφάνιση του Stage "window"
    }
    //Μέθοδος για το κουμπί Manage Patients
    public void handlePatBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("patients.fxml"));
        Scene PatScene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(PatScene);
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
    //Μέθοδος για το κουμπί Add Doctor
    public void handleAddDocBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addDoctor.fxml"));
        Scene DisScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DisScene);
        window.show();
    }
    //Μέθοδος για το κουμπί Create Database
    public void handleCreateDBbtn(ActionEvent event) {
        try {
            FileWriter myWriter = new FileWriter("database/doctorDB.txt", true);
            myWriter.write("");
            myWriter.close();
            MessageBox.SimpleMBox("Database created successfully!", "Success");
            Parent root = FXMLLoader.load(getClass().getResource("doctors.fxml"));
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
        //Εισαγωγή των τιμών των Object της κλάσης "Doctor" στα columns του Table "doctorTable"
        doctorIDColumn.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("id"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        doctorSpecialisationColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("specialisation"));

        DoctorTable.setItems(getDoctors()); //Εισαγωγή των τιμών που επιστρέφει η μέθοδος "getDoctors()" στο πίνακα "doctorTable"
    }
    public ObservableList<Doctor> getDoctors() {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        String file = "database/doctorDB.txt";
        String line = "";
        String delimiter = ":";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { //Reading του αρχείου doctorDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                int id = Integer.parseInt(values.get(0));
                //Όσο το αρχείο περιέχει εγγραφές, δημιουργούνται objects με τα περιεχόμενα των εγγραφών αυτών και εισάγωνται στη λίστα "doctors"
                doctors.add(new Doctor(id, values.get(1), values.get(2)));
            }
        } catch (FileNotFoundException e){ //Αν δεν υπάρχει το αρχείο "doctorDB" τότε πραγματοποιούνται οι κατάλληλες ενέργειες
           MessageBox.SimpleMBox("Could not connect to Database", "Error");
           btnAddDoc.setVisible(false);
           btnCreateDatabase.setVisible(true);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return doctors;
    }
}

