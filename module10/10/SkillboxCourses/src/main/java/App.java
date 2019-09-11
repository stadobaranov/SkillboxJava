import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import skillbox.Course;
import skillbox.Purchase;
import skillbox.PurchaseId;
import skillbox.Student;
import skillbox.Subscription;
import skillbox.SubscriptionId;
import skillbox.Teacher;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class App {
    private final Session session;
    private final DateTimeFormatter dateTimeFormatter;

    public App(Session session) {
        this.session = session;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }

    public void executeQueries() {
        showStudentDetails(42);
        showTeacherDetails(42);
        showCourseDetails(42);
        showSubscriptionDetails(42, 41);
        showPurchaseDetails("Бурда Рубен", "Excel");
    }

    private void showStudentDetails(int id) {
        Student student = session.get(Student.class, id);

        if(student == null) {
            System.out.printf("Студент с идентификатором %d не существует%n%n", id);
        }
        else {
            List<Course> courses = student.getCourses();

            if(courses.isEmpty()) {
                System.out.println("Студент %s еще не подписан ни на один курс%n%n");
            }
            else {
                System.out.printf(
                    "Студент %s подписан на курсы: %s%n%n",
                    student.getName(),
                    courses.stream().map((c) -> "\"" + c.getName() + "\"").collect(Collectors.joining(", "))
                );
            }
        }
    }

    private void showTeacherDetails(int id) {
        Teacher teacher = session.get(Teacher.class, id);

        if(teacher == null) {
            System.out.printf("Преподаватель с идентификатором %d не существует%n%n", id);
        }
        else {
            List<Course> courses = teacher.getCourses();

            if(courses.isEmpty()) {
                System.out.printf("Преподаватель %s еще не ведет ни одного курса%n%n", teacher.getName());
            }
            else {
                System.out.printf(
                    "Преподаватель %s ведет курсы: %s%n%n",
                    teacher.getName(),
                    courses.stream().map((c) -> "\"" + c.getName() + "\"").collect(Collectors.joining(", "))
                );
            }
        }
    }

    private void showCourseDetails(int id) {
        Course course = session.get(Course.class, id);

        if(course == null) {
            System.out.printf("Курс с идентификатором %d не существует%n%n", id);
        }
        else {
            Teacher teacher = course.getTeacher();

            if(teacher == null) {
                System.out.printf("У курса \"%s\" отсутствует преподаватель%n", course.getName());
            }
            else {
                System.out.printf("Преподаватель курса \"%s\": %s%n", course.getName(), teacher.getName());
            }

            List<Student> students = course.getStudents();

            if(students.isEmpty()) {
                System.out.printf("На курсе \"%s\" еще пока нет курсантов%n%n", course.getName());
            }
            else {
                System.out.printf(
                    "Список курсантов \"%s\": %s%n%n",
                    course.getName(),
                    students.stream().map(Student::getName).collect(Collectors.joining(", "))
                );
            }
        }
    }

    private void showSubscriptionDetails(int studentId, int courseId) {
        // Student student = new Student();
        // student.setId(studentId);
        //
        // Course course = new Course();
        // course.setId(courseId);
        //
        // Subscription subscription = session.get(Subscription.class, new SubscriptionId(student, course));
        //
        // if(subscription == null) {
        //     System.out.printf(
        //         "Студен с идентификатором %d не подписан на курс с идентификатором %d%n%n",
        //         studentId,
        //         courseId
        //     );
        // }
        // else {
        //     System.out.printf(
        //         "Студен с идентификатором %d подписан на курс с идентификатором %d с %s%n%n",
        //         studentId,
        //         courseId,
        //         subscription.getSubscriptionDate().format(dateTimeFormatter)
        //     );
        // }

        Student student = session.get(Student.class, studentId);

        if(student == null) {
            System.out.printf("Студент с идентификатором %d не существует%n%n", studentId);
        }
        else {
            Course course = session.get(Course.class, courseId);

            if(course == null) {
                System.out.printf("Курс с идентификатором %d не существует%n%n", courseId);
            }
            else {
                Subscription subscription = session.get(Subscription.class, new SubscriptionId(student, course));

                if(subscription == null) {
                    System.out.printf(
                        "Студен %s не подписан на курс \"%s\"%n%n",
                        student.getName(),
                        course.getName()
                    );
                }
                else {
                    System.out.printf(
                        "Студен %s подписан на курс \"%s\" с %s%n%n",
                        student.getName(),
                        course.getName(),
                        subscription.getSubscriptionDate().format(dateTimeFormatter)
                    );
                }
            }
        }
    }

    private void showPurchaseDetails(String studentName, String courseName) {
        Purchase purchase = session.get(Purchase.class, new PurchaseId(studentName, courseName));

        if(purchase == null) {
            System.out.printf("Студент %s не осуществлял покупку курса \"%s\"%n%n", studentName, courseName);
        }
        else {
            System.out.printf(
                "Студент %s осуществил покупку курса \"%s\" %s%n%n",
                studentName,
                courseName,
                purchase.getSubscriptionDate().format(dateTimeFormatter)
            );
        }
    }

    public static void main(String... args) {
        ServiceRegistry serviceRegistry =
            new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        SessionFactory sessionFactory =
            new MetadataSources(serviceRegistry)
                .getMetadataBuilder()
                .build()
                .getSessionFactoryBuilder()
                .build();

        try(Session session = sessionFactory.openSession()) {
            new App(session).executeQueries();
        }
        finally {
            sessionFactory.close();
        }
    }
}
