package greenwich.comp1786.datastore;

public class Contact {
    private int id ;
    private  String name;
    private String dob;
    private String email;
    private int avatar;

    public Contact(int id, String name,String dob, String email, int avatar) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.avatar  = avatar;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public String getDob() {return dob;}
    public String getEmail() {return email;}
    public int getAvatar() {return avatar;}
}
