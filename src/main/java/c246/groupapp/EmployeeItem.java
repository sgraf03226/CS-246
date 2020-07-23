package c246.groupapp;

/**
 * Stores the basic info fo an individual schedule item that every item has.
 */
public class EmployeeItem {

    private Employee e;
    private String name;
    private String position;
    private String birthday;

    public EmployeeItem(Employee e, String name, String position, String birthday) {
        this.e = e;
        this.name = name;
        this.position = position;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getBirthday() {
        return birthday;
    }

    public Employee getE() {
        return e;
    }


}
