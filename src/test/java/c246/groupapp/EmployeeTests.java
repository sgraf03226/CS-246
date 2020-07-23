package c246.groupapp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmployeeTests {
    @Test
    public void TestEmployeeName(){
        Employee e = new Employee("Greg");
        //e.setName("Greg");
        assertEquals("Greg", e.getName());
    }
}
