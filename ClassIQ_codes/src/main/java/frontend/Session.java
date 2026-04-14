package frontend;

import backend.model.entity.Student;
import java.util.Locale;

public class Session {
    public enum Role { STUDENT, TEACHER }

    private static Role currentRole;
    private static int currentUserId;
    private static Student currentStudent;

    // language / locale
    private static Locale currentLocale = new Locale("en", "US");
    private static String languageCode = "en";

    // for teacher
    private static int currentTeacherId;
    private static String currentTeacherSubject;

    public static void setRole(Role role) {
        currentRole = role;
    }

    public static Role getRole() {
        return currentRole;
    }

    public static void setUserId(int userId) {
        currentUserId = userId;
    }

    public static int getUserId() {
        return currentUserId;
    }

    public static void setCurrentStudent(Student s) {
        currentStudent = s;
    }

    public static Student getCurrentStudent() {
        return currentStudent;
    }

    // teacher session
    public static void setTeacherId(int teacherId) {
        currentTeacherId = teacherId;
    }

    public static int getTeacherId() {
        return currentTeacherId;
    }

    public static void setTeacherSubject(String subject) {
        currentTeacherSubject = subject;
    }

    public static String getTeacherSubject() {
        return currentTeacherSubject;
    }

    // locale
    public static void setCurrentLocale(Locale locale) {
        currentLocale = locale;

        if (locale != null) {
            languageCode = locale.getLanguage();
        }
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    // language code
    public static void setLanguageCode(String code) {
        if (code == null || code.isBlank()) {
            languageCode = "en";
            currentLocale = new Locale("en", "US");
            return;
        }

        languageCode = code.toLowerCase();

        switch (languageCode) {
            case "si":
                currentLocale = new Locale("si", "LK");
                break;
            case "ar":
                currentLocale = new Locale("ar", "SA");
                break;
            default:
                languageCode = "en";
                currentLocale = new Locale("en", "US");
                break;
        }
    }

    public static String getLanguageCode() {
        return languageCode;
    }

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

        currentLocale = new Locale("en", "US");
        languageCode = "en";

        currentTeacherId = 0;
        currentTeacherSubject = null;
    }
}