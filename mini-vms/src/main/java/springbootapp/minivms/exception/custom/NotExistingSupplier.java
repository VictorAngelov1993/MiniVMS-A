package springbootapp.minivms.exception.custom;

public class NotExistingSupplier extends RuntimeException{

    public NotExistingSupplier(String message) {
        super(message);
    }
}
