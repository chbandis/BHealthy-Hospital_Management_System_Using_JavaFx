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

    //Method for the Manage Appointments button
    public void handleAppointBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("appointments.fxml")); //Set the appointments.fxml as Parent
        Scene AppointScene = new Scene(root); //Create Scene "AppointScene"
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow(); //Create the Stage "window"
        window.setScene(AppointScene); //Set of Scene "AppointScene" in stage "window"
        window.show(); //Show the Stage "window"
    }
    //Method for the Manage Doctors button
    public void handleDocBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("doctors.fxml"));
        Scene DocScene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(DocScene);
        window.show();
    }
    //Method for the Manage Diseases button
    public void handleDisBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("diseases.fxml"));
        Scene DisScene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(DisScene);
        window.show();
    }
    //Method for the Add Patients button
    public void handleAddPatBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addPatient.fxml"));
        Scene DisScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DisScene);
        window.show();
    }
    //Method for the Create Database button
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

    //use the initialize method to initialize the following values before running the controller
    @Override
    public void initialize(URL url, ResourceBundle rb){
        //Enter the Object values of the "Doctor" class in the "patientTable" Table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("id"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
        patientDOBColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("dateOfBirth"));
        patientTelColumn.setCellValueFactory(new PropertyValueFactory<Patient, Long>("tel"));
        patientGenderColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
        patientDoctorName.setCellValueFactory(new PropertyValueFactory<Patient, String>("doctor"));
        diseaseColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("disease"));
        treatmentColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("treatment"));

        patientTable.setItems(getPatients()); //Enter the values that the "getPatients()" method returns in the "patientTable" table
    }
    public ObservableList<Patient> getPatients() {

        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String file = "database/patientDB.txt";
        String line = "";
        String delimiter = ":";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { //Reading the file patientDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                int id = Integer.parseInt(values.get(0));
                long tel = Long.parseLong(values.get(4));
                //as long as the file contains records, objects with the contents of those records are created and entered into the patients list
                patients.add(new Patient(id, values.get(1), values.get(2), values.get(3), tel, values.get(5), values.get(6), values.get(7)));
            }
        } catch (FileNotFoundException e){ //if the "patientdb" file does not exist then appropriate actions are taken
            MessageBox.SimpleMBox("Could not connect to Database", "Error");
            btnAddPat.setVisible(false);
            btnCreateDatabase.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patients;
    }
}
