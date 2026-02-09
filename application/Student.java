package application;

public class Student {

    private String name;
    private String email;
    private String department;

    public Student(String name, String email, String department) {
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    // Required for UPDATE operation
    public void setDepartment(String department) {
        this.department = department;
    }
}
