package model;

public class Slave {



    private String name;
    private Slave backup;


    public Slave() {
    }

    public Slave(String name, Slave backup) {
        this.backup = backup;
        this.name = name;
    }

    public Slave getBackup() {
        return backup;
    }

    public void setBackup(Slave backup) {
        this.backup = backup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
