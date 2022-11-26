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

    //Method for the Manage Appointments button
    public void handleAppointBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("appointments.fxml"));  //Set the appointments.fxml as Parent
        Scene AppointScene = new Scene(root); //Create Scene "AppointScene"
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();  //Create the Stage "window"
        window.setScene(AppointScene); //Set of Scene "AppointScene" in stage "window"
        window.show(); //Show the Stage "window"
    }
    //Method for the Manage Patients button
    public void handlePatBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("patients.fxml"));
        Scene PatScene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(PatScene);
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
    //Method for the Add Doctor button
    public void handleAddDocBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addDoctor.fxml"));
        Scene DisScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DisScene);
        window.show();
    }
    //Method for the Create Database button
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

    //use the initialize method to initialize the following values before running the controller
    @Override
    public void initialize(URL url, ResourceBundle rb){
        //Enter the Object values of the "Doctor" class in the Table "doctorTable" columns
        doctorIDColumn.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("id"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        doctorSpecialisationColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("specialisation"));

        DoctorTable.setItems(getDoctors()); //Enter the values returned by the "getDoctors()" method in the doctorTable table
    }
    public ObservableList<Doctor> getDoctors() {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        String file = "database/doctorDB.txt";
        String line = "";
        String delimiter = ":";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { //Reading the file doctorDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                int id = Integer.parseInt(values.get(0));
                //as long as the file contains records, objects with the contents of those records are created and entered into the doctors list
                doctors.add(new Doctor(id, values.get(1), values.get(2)));
            }
        } catch (FileNotFoundException e){ //if the "doctordb" file does not exist then appropriate actions are taken
           MessageBox.SimpleMBox("Could not connect to Database", "Error");
           btnAddDoc.setVisible(false);
           btnCreateDatabase.setVisible(true);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return doctors;
    }
}

