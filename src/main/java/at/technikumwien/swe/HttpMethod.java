package at.technikumwien.swe;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    public final String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public static HttpMethod fromValue(String value) {
        value = value.toUpperCase();
        for (HttpMethod method : HttpMethod.values()) {
            if (method.value.equals(value)) return method;
        }
        return null;
    }
}
