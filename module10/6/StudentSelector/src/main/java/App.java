import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import skillbox.Student;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class App {

    public static void main(String... args) {
        SessionFactory sessionFactory =
            new MetadataSources(
                new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build()
            )
            .getMetadataBuilder()
            .build()
            .getSessionFactoryBuilder()
            .build();

        List<Student> students;

        try(Session session = sessionFactory.openSession()) {
            students = session.createQuery("FROM Student ORDER BY name", Student.class).list();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        System.out.printf("%-45s %7s %20s%n", "Имя", "Возраст", "Дата регистрации");

        for(Student student: students) {
            System.out.printf(
                "%-45s %7d %20s%n",
                student.getName(),
                student.getAge(),
                student.getRegistrationDate().format(formatter)
            );
        }
    }
}
