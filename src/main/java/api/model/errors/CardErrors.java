package api.model.errors;

import org.springframework.http.HttpStatus;

public class CardErrors {

    public static final ErrorModel NOT_FOUND = new ErrorModel(HttpStatus.NOT_FOUND.value(), "404", "Card not found.");
    public static final ErrorModel INVALID_NUMBER_CARD = new ErrorModel(HttpStatus.NOT_FOUND.value(), "404", "Invalid card number");
    public static final ErrorModel ERROR_CREATING = new ErrorModel(HttpStatus.BAD_REQUEST.value(), "400", "Error creating card.");
    public static final ErrorModel CARD_EXISTS = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422", "Exist card with informed number.");
    public static final ErrorModel INACTIVE_CARD = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422", "Inactive card.");

}