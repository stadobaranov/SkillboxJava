package online.school;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StudentStorage {
    private static final String DB_NAME = "online-school";
    private static final String COLLECTION_NAME = "students";

    private final MongoCollection<Document> collection;

    public StudentStorage(MongoClient client) {
        collection = client.getDatabase(DB_NAME).getCollection(COLLECTION_NAME);
    }

    public long getCount() {
        try {
            return collection.countDocuments();
        }
        catch(MongoException exception) {
            throw new StudentStorageException("Не удалось получить количество студентов из базы данных", exception);
        }
    }

    public long getCountWhereAgeIsGreaterThen(int age) {
        try {
            BasicDBObject query = new BasicDBObject("age", new BasicDBObject("$gt", age));
            return collection.countDocuments(query);
        }
        catch(MongoException exception) {
            throw new StudentStorageException("Не удалось получить количество студентов из базы данных", exception);
        }
    }

    public Student getYoungest() {
        return getYoungestOrOldest(1);
    }

    public Student getOldest() {
        return getYoungestOrOldest(-1);
    }

    private Student getYoungestOrOldest(int direction) {
        try {
            FindIterable<Document> result = collection.find().sort(new BasicDBObject("age", direction)).limit(1);

            for(Document document: result) {
                Student student = new Student();
                student.setName(document.get("name", String.class));
                student.setAge(document.get("age", Integer.class));
                student.setCourseNames(new HashSet<>(document.get("courseNames", List.class)));
                return student;
            }
        }
        catch(MongoException exception) {
            throw new StudentStorageException("Не удалось получить студента из базы данных", exception);
        }

        return null;
    }

    public void add(List<Student> students) {
        List<Document> documents = new ArrayList<>(students.size());

        for(Student student: students) {
            documents.add(
                new Document()
                    .append("name", student.getName())
                    .append("age", student.getAge())
                    .append("courseNames", student.getCourseNames())
            );
        }

        try {
            collection.insertMany(documents);
        }
        catch(MongoException exception) {
            throw new StudentStorageException("Не удалось добавить студентов в базу данных", exception);
        }
    }
}
