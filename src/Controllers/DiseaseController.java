package Controllers;

import Classes.Disease;
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

public class DiseaseController implements Initializable {

    @FXML private TableView<Disease> diseaseTable;
    @FXML private TableColumn<Disease, Integer> diseaseIDColumn;
    @FXML private TableColumn<Disease, String> diseaseNameColumn;
    @FXML private TableColumn<Disease, String> diseaseDescription;
    @FXML private TableColumn<Disease, String> diseaseCategory;
    @FXML private Button btnAddDIs;
    @FXML private Button btnCreateDatabase;

    //Method for the Manage Doctors button
    public void handleDocBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("doctors.fxml")); //Set doctors.fxml as Parent
        Scene DocScene = new Scene(root); //Create the Scene "DocScene"
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Create the Stage "window"
        window.setScene(DocScene); //Set the Scene "DocScene" in stage "window"
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
    //Method for the Manage Appointments button
    public void handleAppointBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("appointments.fxml"));
        Scene AppointScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AppointScene);
        window.show();
    }
    //Method for Add Disease button
    public void handleAddDisBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addDisease.fxml"));
        Scene DisScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DisScene);
        window.show();
    }
    //Method for the Create Database button
    public void handleCreateDBbtn(ActionEvent event) {
        try {
            FileWriter myWriter = new FileWriter("database/diseaseDB.txt", true);
            myWriter.write("");
            myWriter.close();
            MessageBox.SimpleMBox("Database created successfully!", "Success");
            Parent root = FXMLLoader.load(getClass().getResource("diseases.fxml"));
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
        //Enter the Object values of the "Disease" class in the "diseaseTable" Table columns
        diseaseIDColumn.setCellValueFactory(new PropertyValueFactory<Disease, Integer>("id"));
        diseaseNameColumn.setCellValueFactory(new PropertyValueFactory<Disease, String>("name"));
        diseaseDescription.setCellValueFactory(new PropertyValueFactory<Disease, String>("description"));
        diseaseCategory.setCellValueFactory(new PropertyValueFactory<Disease, String>("category"));

        diseaseTable.setItems(getDiseases()); //Enter the values returned by the "getDiseases()" method in the "diseaseTable" table
    }
    public ObservableList<Disease> getDiseases() {
        ObservableList<Disease> diseases = FXCollections.observableArrayList();
        String file = "database/diseaseDB.txt";
        String line = "";
        String delimiter = ":";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { //Reading the file diseaseDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                int id = Integer.parseInt(values.get(0));
                //as long as the file contains records objects with the contents of those records are created and entered into the "diseases" list
                diseases.add(new Disease(id, values.get(1), values.get(2), values.get(3)));
            }
        } catch (FileNotFoundException e){ //if the "diseasedb" file does not exist then appropriate actions are taken
            MessageBox.SimpleMBox("Could not connect to Database", "Error");
            btnAddDIs.setVisible(false);
            btnCreateDatabase.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return diseases;
    }
}

