package gr.aueb.cf.eshop_app.exception.custom;

public class AppObjectInvalidException extends RuntimeException {
    public AppObjectInvalidException(String message) {
        super(message);
    }
}
