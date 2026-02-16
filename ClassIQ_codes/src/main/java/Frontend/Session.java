package Frontend;

import Backend.model.entity.Student;

public class Session {
    public enum Role { STUDENT, TEACHER }

    private static Role currentRole;
    private static int currentUserId;
    private static Student currentStudent;

    public static void setRole(Role role) { currentRole = role; }
    public static Role getRole() { return currentRole; }

    public static void setUserId(int userId) { currentUserId = userId; }
    public static int getUserId() { return currentUserId; }

    public static void setCurrentStudent(Student s) { currentStudent = s; }
    public static Student getCurrentStudent() { return currentStudent; }

    public static boolean isStudentLoggedIn() {
        return currentRole == Role.STUDENT && currentStudent != null;
    }

    public static void clear() {
        currentRole = null;
        currentUserId = 0;
        currentStudent = null;
    }
}
