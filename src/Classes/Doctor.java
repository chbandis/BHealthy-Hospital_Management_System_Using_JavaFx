package Classes;

public class Doctor {
    private int id;
    private String name;
    private String specialisation;

    public Doctor(int id, String name, String disease){
        this.id = id;
        this.name = name;
        this.specialisation =  disease;
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
    public void setSpecialisation(String specialisation){
        this.specialisation = specialisation;
    }
    public String getSpecialisation(){
        return specialisation;
    }
}
