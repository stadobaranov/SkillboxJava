package online.school.parsing;

import online.school.Student;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentParser {
    private static final Pattern linePattern = Pattern.compile("^(?<name>[^,]+),(?<age>\\d+),\"(?<courses>.+)\"$");

    public List<Student> parse(File file) {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return parse(reader);
        }
        catch(IOException exception) {
            throw new StudentParserException("Не удалось разобрать файл со студентами", exception);
        }
    }

    private List<Student> parse(BufferedReader reader) throws IOException {
        String line;
        List<Student> students = new ArrayList<>();

        while((line = reader.readLine()) != null) {
            Matcher matcher = linePattern.matcher(line);

            if(!matcher.find()) {
                System.out.printf("Не удалось разобрать строку с данными студента: %s%n", line);
                continue;
            }

            int age;

            try {
                age = Integer.parseInt(matcher.group("age"));
            }
            catch(NumberFormatException exception) {
                System.out.printf("Не удалось разобрать строку с данными студента: %s%n", line);
                continue;
            }

            Set<String> courseNames = new HashSet<>();

            for(String courseName: matcher.group("courses").split(",")) {
                courseNames.add(courseName);
            }

            Student student = new Student();
            student.setName(matcher.group("name"));
            student.setAge(age);
            student.setCourseNames(courseNames);

            students.add(student);
        }

        return students;
    }
}
