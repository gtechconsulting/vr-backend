package api.controller;

import api.entity.CardEntity;
import api.enums.CardStatus;
import api.model.CardModel;
import api.model.CardCreateModel;
import api.service.CardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping( "/cartoes" )
@Tag( name = "Cartões", description = "Cadastro de Cartões" )
@Validated
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping( value = "/{cardNumber}" )
    public ResponseEntity< BigDecimal > getCardByNumber(@PathVariable String cardNumber) {
        return new ResponseEntity< >( cardService.findCardByNumber(cardNumber), HttpStatus.OK);
    }

    @GetMapping( value = "/id/{id}" )
    public ResponseEntity<CardModel> getCardByNumber(@PathVariable Long id) {
        return new ResponseEntity< >( cardService.findCardById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity< List<CardModel> > listCards() {
        return new ResponseEntity< >( cardService.findAllByOrderByCardNumberAsc(), HttpStatus.OK );
    }

    @GetMapping( value = "/status/{status}" )
    public ResponseEntity< List<CardModel> > listCardsByStatus(@PathVariable( "status" ) String status ) {
        return new ResponseEntity< >( cardService.findAllByStatusOrderByCardNumberAsc( CardStatus.fromValue( status ) ), HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity<CardModel> createCard(@Valid @RequestBody CardCreateModel cardCreateModel) {
        return new ResponseEntity< >( cardService.save(cardCreateModel), HttpStatus.CREATED );
    }

    @PutMapping( value = "/{id}" )
    public ResponseEntity<CardModel> updateCard(@PathVariable( "id" ) Long id, @Valid @RequestBody CardEntity cardEntity) {
        return new ResponseEntity< >( cardService.update( id, cardEntity), HttpStatus.OK );
    }

    @DeleteMapping( value = "/{id}" )
    public ResponseEntity< String > deleteCard( @PathVariable( "id" ) Long id ) {
        return new ResponseEntity< >( cardService.deleteCardById( id ), HttpStatus.OK );
    }

}