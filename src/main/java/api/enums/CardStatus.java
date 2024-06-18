package api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum CardStatus {

    INATIVO( "INATIVO" ),
    ATIVO( "ATIVO" );

    private String value;

    public static CardStatus fromValue(String value ) {
        return Arrays.stream( values() )
                .filter( v -> v.getValue().equalsIgnoreCase( value ) )
                .findFirst()
                .orElse( null );
    }

}
