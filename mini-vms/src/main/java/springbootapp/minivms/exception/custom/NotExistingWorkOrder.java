package springbootapp.minivms.exception.custom;

public class NotExistingWorkOrder extends RuntimeException{

    public NotExistingWorkOrder(String message) {
        super(message);
    }
}
