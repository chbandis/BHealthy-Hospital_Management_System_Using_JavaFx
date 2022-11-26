package Classes;

import java.io.FileWriter;
import java.io.IOException;

public class writeToDB {

    //Registration in the disease database
    public static void addDisease(int id, String name, String desc, String cat){
        try {
            FileWriter myWriter = new FileWriter("database/diseaseDB.txt", true);
            myWriter.write(id + ":" + name + ":" + desc + ":" + cat +"\n");
            MessageBox.SimpleMBox("Disease succesfully added to database", "Database");
            myWriter.close();
        }catch (IOException e) {
            MessageBox.SimpleMBox("Error adding disease to database", "Database");
            e.printStackTrace();
        }
    }
    //Registration in the database of doctors
    public static void addDoctor(int id, String name, String spec){
        try {
            FileWriter myWriter = new FileWriter("database/doctorDB.txt", true);
            myWriter.write(id + ":" + name + ":" + spec +"\n");
            MessageBox.SimpleMBox("Doctor succesfully added to database", "Database");
            myWriter.close();
        }catch (IOException e) {
            MessageBox.SimpleMBox("Error adding doctor to database", "Database");
            e.printStackTrace();
        }
    }
    //Registration in the patient database
    public static void addPatient(int id, String name, String gender, String dob, String tel, String doc, String dis, String treat){
        try {
            FileWriter myWriter = new FileWriter("database/patientDB.txt", true);
            myWriter.write(id + ":" + name + ":" + gender + ":" + dob + ":" + tel + ":" + doc + ":" + dis + ":" + treat +"\n");
            MessageBox.SimpleMBox("Patient succesfully added to database", "Database");
            myWriter.close();
        }catch (IOException e) {
            MessageBox.SimpleMBox("Error adding patient to database", "Database");
            e.printStackTrace();
        }
    }
    //Registration in the appointment database 
    public static void newAppointment(int id, String patName, String patTel, String docName, String date, String datetime, String dis, String treat){
        try {
            FileWriter myWriter = new FileWriter("database/appointmentDB.txt", true);
            myWriter.write(id + ":" + patName + ":" + patTel + ":" + docName + ":" + date + ":" + datetime +":" + dis + ":" + treat +"\n");
            MessageBox.SimpleMBox("Appointment succesfully created", "Database");
            myWriter.close();
        }catch (IOException e) {
            MessageBox.SimpleMBox("Error creating appointment", "Database");
            e.printStackTrace();
        }
    }
}
