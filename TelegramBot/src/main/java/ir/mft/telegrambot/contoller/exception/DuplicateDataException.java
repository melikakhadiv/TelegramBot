package ir.mft.telegrambot.contoller.exception;

public class DuplicateDataException extends Exception {
    private String message;

    public DuplicateDataException() {
        message = "Duplicate Data";
    }

    public DuplicateDataException(String message) {
        super(message);
        this.message = message;
    }
}
