package c246.groupapp;


/**
 * Employee class enables storage of important information about the employees
 *
 */
public class Employee {
    private String name;
    private String position;
    private boolean isManager;

    public boolean getisManager() {
        return isManager;
    }

    public Employee() {
    }

    public void setIsManager(boolean manager) {
        this.isManager = manager;
    }



    /**
     * non default Employee constructor
     * @param name string becomes name of new employee
     */
    public Employee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
