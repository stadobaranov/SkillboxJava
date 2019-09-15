import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class App {
    public static void main(String... args) {
        // ALTER TABLE `PurchaseList`
        // ADD COLUMN `student_id` INT(10) UNSIGNED FIRST,
        // ADD COLUMN `course_id` INT(10) UNSIGNED AFTER `student_id`;

        executeSession();

        // ALTER TABLE `PurchaseList`
        // CHANGE COLUMN `student_id` `student_id` INT(10) UNSIGNED NOT NULL FIRST,
        // CHANGE COLUMN `course_id` `course_id` INT(10) UNSIGNED NOT NULL AFTER `student_id`;
        //
        // ALTER TABLE `PurchaseList`
        // ADD PRIMARY KEY (`student_id`, `course_id`);
    }

    private static void executeSession() {
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
            executeQuery(session);
        }
        finally {
            sessionFactory.close();
        }
    }

    private static void executeQuery(Session session) {
        String hql = "UPDATE Purchase AS p " +
                     "SET " +
                     "id.student = (FROM Student WHERE name = p.studentName), " +
                     "id.course = (FROM Course WHERE name = p.courseName)";

        Transaction tx = session.beginTransaction();
        session.createQuery(hql).executeUpdate();
        tx.commit();
    }
}
