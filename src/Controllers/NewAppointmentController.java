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

    //Method for the Cancel button
    public void handleCancelAppointBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("appointments.fxml")); //Set appointments.fxml as Parent
        Scene AppointScene = new Scene(root); //Create Scene "AppointScene"
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Create the Stage "window"
        window.setScene(AppointScene); //Set of Scene "AppointScene" in stage "window"
        window.show(); //Show the Stage "window"
    }
    //Method for the New Appointment button
    public void newAppoint(ActionEvent event) throws IOException {
        String pname = (String)  patName.getValue(); //Enter the field values in the corresponding variables
        String tel = patTel.getText();
        String dname = (String) docName.getValue();
        String date = String.valueOf(appointDate.getValue());
        String datetime = Hpicker.getValue() + "." + Mpicker.getValue();
        String patdis = (String) dis.getValue();
        String pattreat = treat.getText();

        //Check for empty fields and display the appropriate message
        if(patName.getSelectionModel().isEmpty() || tel.isEmpty() || docName.getSelectionModel().isEmpty() || appointDate.getValue() == null  || Hpicker.getSelectionModel().isEmpty() || Mpicker.getSelectionModel().isEmpty() || dis.getSelectionModel().isEmpty() || pattreat.isEmpty()){
            MessageBox.SimpleMBox("Please fill all the fields", "Fill all the fields");
        } else if (!validateMobileNo(patTel)) {
            MessageBox.SimpleMBox("Please enter a valid phone number", "Error");
        } else { //If the above check is completed without any error, the values are written to the "database" through the "newAppointment" method of the "writeToDB" class
            writeToDB.newAppointment(id, pname, tel, dname, date, datetime, patdis, pattreat);                        //and redirect to stage "window" created below
            Parent root = FXMLLoader.load(getClass().getResource("appointments.fxml"));
            Scene DisScene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(DisScene);
            window.show();
        }
    }
    //The validateMobileNo method that accepts a Textfield and returns if its content corresponds to a real ten-digit phone number.
    private boolean validateMobileNo(TextField tel) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(tel.getText());
        return m.find();
    }

    //The docSpecChoice method that sets the values of the appropriate list, depending on the name of the doctor, in the Combobox items "dis"
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

    //use the initialize method to initialize the following values before running the controller
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Hpicker.setValue("Hour"); //Entering the appropriate values in the ComboBoxes "Hpicker" and "Mpicker"
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
        try (BufferedReader br = new BufferedReader(new FileReader(Docfile))) { //Reading the file doctorDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                docs.add(values.get(1)); //Enter the second value of each record that contains the file in the "docs" list
            }
            docName.setItems(docs); //Entering the values of the docs list in the ComboBox items "docName"
        } catch (FileNotFoundException e){ //if the "doctordb" file does not exist then appropriate actions are taken
            docName.setValue("Error connecting to Doctor Database");
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
                    dermDis.add(values.get(1));            //depending on the content of the fourth value of each record in the file
                }
                else if (values.get(3).equals("Gastroenterology")){
                    gastroDis.add(values.get(1));
                }
                else if (values.get(3).equals("Neurology")){
                    neuroDis.add(values.get(1));
                }
            }
            dis.getItems().add("Please select a doctor"); //Enter a value in the ComboBox items "dis"
        } catch (FileNotFoundException e){ //if the "diseasedb" file does not exist then appropriate actions are taken
            dis.setValue("Error connecting to Disease Database");
            btnSave.setDisable(true);
        } catch(IOException e){
            e.printStackTrace();
        }

        String Patfile = "database/patientDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Patfile))) { //Reading the file patientDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                patName.getItems().add(values.get(1)); //Enter the second value of each record that contains the file in the "patName" ComboBox
            }
        } catch (FileNotFoundException e){ //if the "patientdb" file does not exist then appropriate actions are taken
            patName.setValue("Error connecting to Patient Database");
            btnSave.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int lastid = 0;
        String Appointfile = "database/appointmentDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Appointfile))) { //Reading the file appointmentDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                lastid = Integer.parseInt(values.get(0)); //Enter the first value of the file in the lastid variable
            }
            if(lastid==0) {  //if there is no value in the file then the value 1 is entered in the variable id
                id = 1;      //so that registrations start from the number 1 and not from 0
            } else {
                id = lastid + 1;  //if there is a value in the file then the first value of the next record that will be made 
            }                     //will be the value of the previous increased by 1
        } catch (IOException e) {
            e.printStackTrace();  //At this point there is no check for the existence of the "appointmentDB" file, as it is carried out
        }                         //in the previous Stage and in case the specific file does not exist, the present Stage does not appear (newAppointment.fxml)
    }
}
