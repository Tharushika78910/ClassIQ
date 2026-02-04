package Backend;

import Backend.model.dao.StudentDao;
import Backend.model.dao.impl.StudentDaoImpl;
import Backend.model.entity.Student;

public class StudentCrudDemoMain {
    public static void main(String[] args) throws Exception {
        StudentDao dao = new StudentDaoImpl();

        // Read existing student
        Student s201 = dao.findById(201);
        System.out.println("READ (Before) student 201 email: " + s201.getEmail());

        // Update existing student
        s201.setEmail("updated201@metropolia.fi");
        dao.update(s201);

        Student s201After = dao.findById(201);
        System.out.println("READ (After) student 201 email: " + s201After.getEmail());

        // Create new student (UserId should exist in app_user)
        Student newStudent = new Student();
        newStudent.setStudentId(999);
        newStudent.setFirstName("Demo");
        newStudent.setLastName("Student");
        newStudent.setStudentNumber("S9999");
        newStudent.setEmail("demo999@metropolia.fi");
        newStudent.setUserId(5); // must exist in app_user

        // If 999 already exists from previous, delete first to avoid to avoid duplicate key error we can delete it first
        if (dao.existsById(999)) {
            dao.delete(999);
        }

        dao.create(newStudent);
        Student created = dao.findById(999);
        System.out.println("CREATE + READ new student 999: " + created.getFirstName() + " " + created.getLastName());

        // Delete student 999
        dao.delete(999);
        Student deleted = dao.findById(999);
        System.out.println("DELETE student 999, now findById returns: " + deleted);
    }
}
