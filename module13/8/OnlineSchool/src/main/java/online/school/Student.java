package online.school;

import java.util.Set;

public class Student {
    private String name;
    private int age;
    private Set<String> courseNames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Set<String> getCourseNames() {
        return courseNames;
    }

    public void setCourseNames(Set<String> courseNames) {
        this.courseNames = courseNames;
    }
}
