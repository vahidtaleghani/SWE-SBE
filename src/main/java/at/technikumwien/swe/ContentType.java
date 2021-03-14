package at.technikumwien.swe;

public enum ContentType {
    TEXT("text/plain"),
    JSON("application/json"),
    HTML("text/html");

    public final String value;

    ContentType(String value) {
        this.value = value;
    }

    public static ContentType fromValue(String value) {
        for (ContentType method : ContentType.values()) {
            if (method.value.equals(value)) return method;
        }
        return null;
    }
}
