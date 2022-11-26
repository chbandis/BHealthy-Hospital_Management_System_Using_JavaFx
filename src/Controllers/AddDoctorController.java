package Controllers;

import Classes.MessageBox;
import Classes.writeToDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddDoctorController implements Initializable {

    @FXML private TextField docName;
    @FXML private ComboBox docSpec;
    @FXML private Button btnSave;
    ArrayList<String> specList = new ArrayList<>();
    int id;

    //Method for the Cancel button
    public void handleCancelDocBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("doctors.fxml")); //Set doctors.fxml as Parent
        Scene DocScene = new Scene(root); //Create the Scene "DocScene"
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Create the Stage "window"
        window.setScene(DocScene); //Set the Scene "DocScene" in the Stage "window"
        window.show(); //Show the Stage "window"
    }
    //Method for the Add Doctor buttonr
    public void addDoc(ActionEvent event) throws IOException {
        String name = docName.getText(); //Enter the field values in the corresponding variables
        String spec = (String) docSpec.getValue();
        if(name.isEmpty() || docSpec.getSelectionModel().isEmpty()){ //Check for empty fields and display the appropriate message
            MessageBox.SimpleMBox("Please fill all the fields", "Fill all the fields");
        } else if(containsNumber(name)){ //Check if the field value contains numbers by using the generated containsnumber method
            MessageBox.SimpleMBox("Name should not contain number(s)", "Error"); //below and display the appropriate message
        }
        else { //if the tests are completed without any error, the values are written to the "database" through the "addDoctor" method of the "writeToDB" class
            writeToDB.addDoctor(id, name, spec);                                                       //and redirect to stage "window" created below
            Parent root = FXMLLoader.load(getClass().getResource("doctors.fxml"));
            Scene DisScene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(DisScene);
            window.show();
        }
    }
    //the containsnumber method that accepts a string variable and returns if that variable contains numbers
    public boolean containsNumber(String s)
    {
        Pattern p = Pattern.compile( "[0-9]" );
        Matcher m = p.matcher( s );

        return m.find();
    }

    //use the initialize method to initialize the following values before running the controller
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int lastid = 0;
        String line = "";
        String delimiter = ":";
        String Docfile = "database/doctorDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Docfile))) { //Reading the file doctorDB.txt
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
            e.printStackTrace(); //At this point, no check is made for the existence of the "doctorDB" file, as it is carried out
        }                        //in the previous stage and if the specific file does not exist the present stage does not appear (addDoctor.fxml)

        String Disfile = "database/diseaseDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Disfile))) { //Reading the file diseaseDB.txt
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                specList.add(values.get(3)); //Enter the fourth value of each record that contains the file in the "specList" list
            }
        } catch (FileNotFoundException e){ //if the "diseasedb" file does not exist then appropriate actions are taken
            docSpec.setValue("Error connecting to Disease Database");
            btnSave.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<String> uniqueSpec = new HashSet<>(specList); //Create a HashSet from the "specList" list to eliminate any values that appear
        for(String s: uniqueSpec)                         //more than once
        {
            docSpec.getItems().add(s);  //Entering hashset values in ComBobox items "docSpec"
        }
    }

}
