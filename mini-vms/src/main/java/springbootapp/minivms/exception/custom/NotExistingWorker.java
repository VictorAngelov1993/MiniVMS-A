package springbootapp.minivms.exception.custom;

public class NotExistingWorker extends RuntimeException{

    public NotExistingWorker(String message) {
        super(message);
    }
}
