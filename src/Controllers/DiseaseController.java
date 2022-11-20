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

    //Μέθοδος για το κουμπί Manage Doctors
    public void handleDocBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("doctors.fxml")); //Καθορισμός του doctors.fxml ως Parent
        Scene DocScene = new Scene(root); //Δημιουργία του Scene "DocScene"
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Δημιουργία του Stage "window"
        window.setScene(DocScene); //Καθοριμός του Scene "DocScene" στο Stage "window"
        window.show(); //Εμφάνιση του Stage "window"
    }
    //Μέθοδος για το κουμπί Manage Patients
    public void handlePatBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("patients.fxml"));
        Scene PatScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(PatScene);
        window.show();
    }
    //Μέθοδος για το κουμπί Manage Appointments
    public void handleAppointBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("appointments.fxml"));
        Scene AppointScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AppointScene);
        window.show();
    }
    //Μέθοδος για το κουμπί Add Disease
    public void handleAddDisBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addDisease.fxml"));
        Scene DisScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DisScene);
        window.show();
    }
    //Μέθοδος για το κουμπί Create Database
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
    //Χρήση της μεθόδου initialize ώστε να αρχικοποιηθούν οι παρακάτω τιμές πριν την εκτέλεση του Controller
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Εισαγωγή των τιμών των Object της κλάσης "Disease" στα columns του Table "diseaseTable"
        diseaseIDColumn.setCellValueFactory(new PropertyValueFactory<Disease, Integer>("id"));
        diseaseNameColumn.setCellValueFactory(new PropertyValueFactory<Disease, String>("name"));
        diseaseDescription.setCellValueFactory(new PropertyValueFactory<Disease, String>("description"));
        diseaseCategory.setCellValueFactory(new PropertyValueFactory<Disease, String>("category"));

        diseaseTable.setItems(getDiseases()); //Εισαγωγή των τιμών που επιστρέφει η μέθοδος "getDiseases()" στο πίνακα "diseaseTable"
    }
    public ObservableList<Disease> getDiseases() {
        ObservableList<Disease> diseases = FXCollections.observableArrayList();
        String file = "database/diseaseDB.txt";
        String line = "";
        String delimiter = ":";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { //Reading του αρχείου diseaseDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                int id = Integer.parseInt(values.get(0));
                //Όσο το αρχείο περιέχει εγγραφές, δημιουργούνται objects με τα περιεχόμενα των εγγραφών αυτών και εισάγωνται στη λίστα "diseases"
                diseases.add(new Disease(id, values.get(1), values.get(2), values.get(3)));
            }
        } catch (FileNotFoundException e){ //Αν δεν υπάρχει το αρχείο "diseaseDB" τότε πραγματοποιούνται οι κατάλληλες ενέργειες
            MessageBox.SimpleMBox("Could not connect to Database", "Error");
            btnAddDIs.setVisible(false);
            btnCreateDatabase.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return diseases;
    }
}

