package Frontend;

public class Session {
    public enum Role { STUDENT, TEACHER }

    private static Role currentRole;

    public static void setRole(Role role) {
        currentRole = role;
    }

    public static Role getRole() {
        return currentRole;
    }

    public static void clear() {
        currentRole = null;
    }
}
