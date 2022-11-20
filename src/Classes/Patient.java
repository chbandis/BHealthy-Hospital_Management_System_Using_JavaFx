package Classes;

public class Patient {
    private int id;
    private String name;
    private String gender;
    private String dateOfBirth;
    private long tel;
    private String doctor;
    private String disease;
    private String treatment;

    public Patient(int id, String name, String gender, String dateOfBirth, long tel, String doctor, String disease, String treatment){
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.tel = tel;
        this.doctor = doctor;
        this.disease = disease;
        this.treatment = treatment;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public String getGender(){
        return gender;
    }
    public void setDateOfBirth(String dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }
    public String getDateOfBirth(){
        return dateOfBirth;
    }
    public long getTel() { return tel; }
    public void setTel(long tel) { this.tel = tel; }
    public void setDoctor(String doctor){
        this.doctor = doctor;
    }
    public String getDoctor(){
        return doctor;
    }
    public void setDisease(String disease){
        this.disease = disease;
    }
    public String getDisease(){
        return disease;
    }
    public void setTreatment(String treatment){
        this.treatment = treatment;
    }
    public String getTreatment(){
        return treatment;
    }
}
