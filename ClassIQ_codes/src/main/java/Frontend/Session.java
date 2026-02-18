package Frontend;

import Backend.model.entity.Student;

public class Session {
    public enum Role { STUDENT, TEACHER }

    private static Role currentRole;
    private static int currentUserId;
    private static Student currentStudent;

    // for teacher
    private static int currentTeacherId;
    private static String currentTeacherSubject;

    public static void setRole(Role role) { currentRole = role; }
    public static Role getRole() { return currentRole; }

    public static void setUserId(int userId) { currentUserId = userId; }
    public static int getUserId() { return currentUserId; }

    public static void setCurrentStudent(Student s) { currentStudent = s; }
    public static Student getCurrentStudent() { return currentStudent; }

    // getters/setters for teacher
    public static void setTeacherId(int teacherId) { currentTeacherId = teacherId; }
    public static int getTeacherId() { return currentTeacherId; }

    public static void setTeacherSubject(String subject) { currentTeacherSubject = subject; }
    public static String getTeacherSubject() { return currentTeacherSubject; }

    public static boolean isStudentLoggedIn() {
        return currentRole == Role.STUDENT && currentStudent != null;
    }

    public static boolean isTeacherLoggedIn() {
        return currentRole == Role.TEACHER && currentTeacherId != 0;
    }

    public static void clear() {
        currentRole = null;
        currentUserId = 0;
        currentStudent = null;

        currentTeacherId = 0;
        currentTeacherSubject = null;
    }
}
