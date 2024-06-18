package api.controller;

import api.model.TransactionCreateModel;
import api.model.TransactionModel;
import api.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping( "/transacoes" )
@Tag( name = "Transações", description = "Cadastro de Transações" )
@Validated
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping( value = "/{id}" )
    public ResponseEntity<TransactionModel> getTransactionById(@PathVariable Long id) {
        return new ResponseEntity< >(transactionService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity< List<TransactionModel> > listTransactions() {
        return new ResponseEntity< >( transactionService.findAll(), HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity< String > createTransaction(@Valid @RequestBody TransactionCreateModel transactionCreateModel) {
        return new ResponseEntity< >(transactionService.save(transactionCreateModel), HttpStatus.CREATED );
    }

    @DeleteMapping( value = "/{id}" )
    public ResponseEntity< String > deleteTransaction( @PathVariable( "id" ) Long id ) {
        return new ResponseEntity< >( transactionService.deleteById( id ), HttpStatus.OK );
    }

}