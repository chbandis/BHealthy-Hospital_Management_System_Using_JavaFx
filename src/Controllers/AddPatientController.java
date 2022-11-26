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

    //Method for the Cancel button
    public void handleCancelPatBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("patients.fxml")); //Set patients.fxml as Parent
        Scene PatScene = new Scene(root); //Creating the Scene "PatScene"
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();  //Create the Stage "window"
        window.setScene(PatScene); //Set the "PatScene" Scene in the Stage "window"
        window.show(); //Show the Stage "window"
    }
    //Method for Add Patient button
    public void addDPat(ActionEvent event) throws IOException {
        String name = patName.getText(); //Enter the field values in the corresponding variables
        String tel = patTel.getText();
        String gender = "";
        String dob = String.valueOf(patDOB.getValue());
        String docName = (String) patDoc.getValue();
        String dis = (String) patDis.getValue();
        String treat = patTreat.getText();

        if (patMale.isSelected()){ //Check the selection of RadioButtons
            gender = "Male";
        }
        else if (patFemale.isSelected()){
            gender = "Female";
        }
        //Check for empty fields and display the appropriate message
        if(name.isEmpty() || gender.isEmpty() || patDOB.getValue() == null || tel.isEmpty() || patDoc.getSelectionModel().isEmpty() || patDis.getSelectionModel().isEmpty() || treat.isEmpty()){
            MessageBox.SimpleMBox("Please fill all the fields", "Fill all the fields");
        } else if (containsNumber(name)){ //Check if the field value contains numbers by using the generated containsnumber method
            MessageBox.SimpleMBox("Name should not contain number(s)", "Error"); //below and display the appropriate message
        } else if (!validateMobileNo(patTel)) {
            MessageBox.SimpleMBox("Please enter a valid phone number", "Error");
        }
        else { //if the tests are completed without any error the values are written to the "database" through the "addPatient" method of the "writeToDB" class
            writeToDB.addPatient(id, name, gender, dob, tel, docName, dis, treat);                       //and redirect to stage "window" created below
            Parent root = FXMLLoader.load(getClass().getResource("patients.fxml"));
            Scene DisScene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(DisScene);
            window.show();
        }
    }
    //the containsnumber method that accepts a string variable and returns if that variable contains numbers
    public boolean containsNumber(String str) {
        Pattern p = Pattern.compile( "[0-9]" );
        Matcher m = p.matcher(str);

        return m.find();
    }
    //The validateMobileNo method that accepts a Textfield and returns if its content corresponds to a real ten-digit phone number.
    private boolean validateMobileNo(TextField tel) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(tel.getText());
        return m.find();
    }

    //The docSpecChoice method that sets the values of the appropriate list, depending on the name of the doctor, in the combobox items "patdis"
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

    //use the initialize method to initialize the following values before running the controller
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String line = "";
        String delimiter = ":";
        String Docfile = "database/doctorDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Docfile))) { //Reading the file doctorDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                docs.add(values.get(1)); //Enter the second value of each record that contains the file in the "docs" list
            }
            patDoc.setItems(docs); //Entering the values of the docs list in the ComboBox items "patDoc"
        } catch (FileNotFoundException e){ //if the "doctordb" file does not exist then appropriate actions are taken
            patDoc.setValue("Error connecting to Doctor Database");
            btnSave.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dermDisfile = "database/diseaseDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(dermDisfile))) { //Reading the file diseaseDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                allDis.add(values.get(1));
                if (values.get(3).equals("Dermatology")){ //Enter the second value of each record that contains the file in the appropriate list,
                    dermDis.add(values.get(1));           //depending on the content of the fourth value of each record in the file
                }
                else if (values.get(3).equals("Gastroenterology")){
                    gastroDis.add(values.get(1));
                }
                else if (values.get(3).equals("Neurology")){
                    neuroDis.add(values.get(1));
                }
            }
            patDis.getItems().add("Please select a doctor"); //Enter a value in the ComboBox items "patDis"
        } catch (FileNotFoundException e){ //if the "diseasedb" file does not exist then appropriate actions are taken
            patDis.setValue("Error connecting to Disease Database");
            btnSave.setDisable(true);
        } catch(IOException e){
            e.printStackTrace();
        }

        int lastid = 0;
        String Patfile = "database/patientDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Patfile))) { //Reading the file doctorDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                lastid = Integer.parseInt(values.get(0)); //Enter the first value of the file in the lastid variable
            }
            if(lastid==0) { //if there is no value in the file then the value 1 is entered in the variable id
                id = 1;     //so that registrations start from the number 1 and not from 0
            } else {
                id = lastid + 1; //if there is a value in the file then the first value of the next record that will be made 
            }                    //will be the value of the previous increased by 1
        } catch (IOException e) {
            e.printStackTrace(); //At this point there is no check for the existence of the "patientDB" file, as it is carried out
        }                        //in the previous stage and if the specific file does not exist the present stage does not appear (addpatient.fxml)
    }
}
