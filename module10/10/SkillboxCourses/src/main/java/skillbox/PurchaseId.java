package skillbox;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PurchaseId implements Serializable {
    @Column(name = "student_name")
    private String studentName;

    @Column(name = "course_name")
    private String courseName;

    public PurchaseId() {}

    public PurchaseId(String studentName, String courseName) {
        this.studentName = studentName;
        this.courseName = courseName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
