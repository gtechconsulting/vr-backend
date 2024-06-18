package api.model.errors;

import org.springframework.http.HttpStatus;

public class CommonErrors {

    public static final ErrorModel INVALID_PARAMETERS = new ErrorModel(HttpStatus.BAD_REQUEST.value(), "400", "Invalid parameter");
    public static final ErrorModel INVALID_PASSWORD = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422", "Wrong password.");
    public static final ErrorModel UNEXPECTED_ERROR = new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), "500", "Unexpected error");

}
