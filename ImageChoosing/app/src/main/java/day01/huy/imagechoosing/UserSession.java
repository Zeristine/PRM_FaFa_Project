package day01.huy.imagechoosing;

public class UserSession {
    private static int userId = -1;

    public UserSession() {
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        UserSession.userId = userId;
    }
}
