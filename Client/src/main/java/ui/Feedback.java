package ui;

public class Feedback {
    boolean succes;
    String message;
    Object involved;
    Exception exception;

    public Feedback(boolean succes, String message) {
        this.succes = succes;
        this.message = message;
    }

    public Feedback(boolean succes, String message, Exception exception) {
        this.succes = succes;
        this.message = message;
        this.exception = exception;
    }

    public Feedback(boolean succes, String message, Object involved) {
        this.succes = succes;
        this.message = message;
        this.involved = involved;
    }

    public Feedback(boolean succes, String message, Object involved, Exception exception) {
        this.succes = succes;
        this.message = message;
        this.involved = involved;
        this.exception = exception;
    }

    public boolean isSucces() {
        return succes;
    }

    public void setSucces(boolean succes) {
        this.succes = succes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getInvolved() {
        return involved;
    }

    public void setInvolved(Object involved) {
        this.involved = involved;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
