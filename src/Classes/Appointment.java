package Classes;

public class Appointment {
    private int id;
    private String patientName;
    private Long patientTel;
    private String doctorName;
    private String date;
    private String hour;
    private String disease;
    private String treatment;

    public Appointment(int id, String patientName, Long patientTel, String doctorName, String date, String hour, String disease, String treatment){
        this.id = id;
        this.patientName = patientName;
        this.patientTel = patientTel;
        this.doctorName = doctorName;
        this.date = date;
        this.hour = hour;
        this.disease = disease;
        this.treatment = treatment;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public Long getPatientTel() { return patientTel; }
    public void setPatientTel(Long patientTel) { this.patientTel = patientTel; }
    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return date;
    }
    public String getHour() {
        return hour;
    }
    public void setHour(String hour) {
        this.hour = hour;
    }
    public void setDisease(String disease){ this.disease = disease; }
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