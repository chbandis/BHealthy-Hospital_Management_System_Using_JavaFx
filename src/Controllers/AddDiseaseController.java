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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddDiseaseController implements Initializable {

    @FXML private TextField disName;
    @FXML private TextArea disDesc;
    @FXML private TextField disCat;
    int id;

    //Method for the Cancel button
    public void handleCancelDisBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("diseases.fxml")); //Specify diseases.fxml as Parent
        Scene DisScene = new Scene(root); //Creating the Scene "DisScene"
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Create the Stage "window"
        window.setScene(DisScene); //Set the "DisScene" Scene in the "window" Stage
        window.show(); //Show the Stage "window"
    }
    //Method for Add Disease button
    public void addDis(ActionEvent event) throws IOException {
        String name = disName.getText(); //Enter the field values in the corresponding variables
        String desc = disDesc.getText();
        String cat = disCat.getText();
        if(name.isEmpty() || desc.isEmpty() || cat.isEmpty()){ //Check for empty fields and display the appropriate message
            MessageBox.SimpleMBox("Please fill all the fields", "Fill all the fields");
        } else if(containsNumber(name) || containsNumber(desc) || containsNumber(cat)){ //Check if field values contain numbers by using the containsnumber method
            MessageBox.SimpleMBox("Textfields should not contain number(s)", "Error");      //created below and display the appropriate message
        }
        else { //if the tests are completed without any error, the values are written to the "database" through the "addDisease" method of the "writeToDB" class
            writeToDB.addDisease(id, name, desc, cat);                                                   //and redirect to stage "window" created below
            Parent root = FXMLLoader.load(getClass().getResource("diseases.fxml"));
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
        String Disfile = "database/diseaseDB.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(Disfile))) { //Reading the file diseaseDB.txt
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
            e.printStackTrace(); //At this point there is no check for the existence of the "diseaseDB" file, as it is performed
        }                        //in the previous stage and if the specific file does not exist the present stage does not appear (adddisease.fxml)
    }
}

