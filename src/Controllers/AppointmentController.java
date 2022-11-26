package Controllers;

import Classes.Appointment;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;


public class AppointmentController implements Initializable {

    @FXML private TableView<Appointment> appointTable;
    @FXML private TableColumn<Appointment, Integer> idColumn;
    @FXML private TableColumn<Appointment, String> patientNameColumn;
    @FXML private TableColumn<Appointment, Long> patientTelColumn;
    @FXML private TableColumn<Appointment, String> doctorNameColumn;
    @FXML private TableColumn<Appointment, String> dateColumn;
    @FXML private TableColumn<Appointment, String> hourColumn;
    @FXML private TableColumn<Appointment, String> disColumn;
    @FXML private TableColumn<Appointment, String> treatColumn;
    @FXML private Button btnNewAppoint;
    @FXML private Button btnCreateDatabase;

    //Method for the Manage Doctors button
    public void handleDocBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("doctors.fxml")); //Set doctors.fxml as Parent
        Scene DocScene = new Scene(root); //Creating the Scene "DocScene"
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Create the Stage "window"
        window.setScene(DocScene); //Definition of Scene "DocScene" in stage "window"
        window.show(); //Show the Stage "window"
    }
    //Method for the Manage Patients button
    public void handlePatBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("patients.fxml"));
        Scene PatScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(PatScene);
        window.show();
    }
    //Method for the Manage Diseases button
    public void handleDisBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("diseases.fxml"));
        Scene DisScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DisScene);
        window.show();
    }
    //Method for the New Appointment button
    public void handleNewAppointBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("newAppointment.fxml"));
        Scene DisScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DisScene);
        window.show();
    }
    //Method for the Create Database button
    public void handleCreateDBbtn(ActionEvent event) {
        try {
            FileWriter myWriter = new FileWriter("database/appointmentDB.txt", true);
            myWriter.write("");
            myWriter.close();
            MessageBox.SimpleMBox("Database created successfully!", "Success");
            Parent root = FXMLLoader.load(getClass().getResource("appointments.fxml"));
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
    public void initialize(URL url, ResourceBundle rb) {
        //Enter the object values of the "Appointment" class in the "appointTable" Table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientName"));
        patientTelColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Long>("patientTel"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("doctorName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("date"));
        hourColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("hour"));
        disColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("disease"));
        treatColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("treatment"));

        appointTable.setItems(getAppointments()); //Enter the values returned by the "getAppointments() method" in the appointTable table
    }
    public ObservableList<Appointment> getAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String file = "database/appointmentDB.txt";
        String line = "";
        String delimiter = ":";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { //Reading the file appointmentDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                int id = Integer.parseInt(values.get(0));
                long tel = Long.parseLong(values.get(2));
                //as long as the file contains records, objects with the contents of those records are created and entered into the appointments list
                appointments.add(new Appointment(id, values.get(1), tel, values.get(3), values.get(4), values.get(5), values.get(6), values.get(7)));
            }
        } catch (FileNotFoundException e){ //if the "appointmentsdb" file does not exist then appropriate actions are taken
            MessageBox.SimpleMBox("Could not connect to Database", "Error");
            btnNewAppoint.setVisible(false);
            btnCreateDatabase.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointments;
    }
}
