package online.school;

import online.school.parsing.StudentParser;
import java.io.File;
import java.util.List;

public class Application {
    public static void main(String... args) throws Exception {
        StudentParser sp = new StudentParser();
        List<Student> students = sp.parse(new File("src/main/resources/mongo.csv"));

        try(StudentStorageFactory ssf = new StudentStorageFactory("192.168.99.100", 27017)) {
            StudentStorage ss = ssf.create();
            ss.add(students);

            System.out.printf("Общее количество студентов: %d%n", ss.getCount());
            System.out.printf("Количество студентов старше 40 лет: %d%n", ss.getCountWhereAgeIsGreaterThen(40));
            System.out.printf("Имя самого молодого студента: %s%n", ss.getYoungest().getName());
            System.out.printf("Курсы самого старого студента: %s%n", ss.getOldest().getCourseNames());
        }
    }
}
