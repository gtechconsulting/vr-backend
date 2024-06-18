package api.model.errors;

import org.springframework.http.HttpStatus;

public class TransactionErrors {

    public static final ErrorModel NOT_FOUND = new ErrorModel(HttpStatus.NOT_FOUND.value(), "404", "Transaction not found.");
    public static final ErrorModel INSUFFICIENT_BALANCE = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422", "Insufficient balance to carry out the transaction.");

}