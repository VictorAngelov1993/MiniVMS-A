package springbootapp.minivms.exception.custom;

public class BudgetNotFoundException extends RuntimeException
{
    public BudgetNotFoundException(String message) {
        super(message);
    }
}
