package api.service;

import api.ApplicationTests;
import api.entity.CardEntity;
import api.entity.BalanceEntity;
import api.entity.TransactionEntity;
import api.enums.CardStatus;
import api.model.TransactionCreateModel;
import api.model.TransactionModel;
import api.repository.CardRepository;
import api.repository.BalanceRepository;
import api.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionServiceTest extends ApplicationTests {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private BalanceRepository balanceRepository;

    private final BalanceEntity mockBalanceEntity = BalanceEntity.builder()
            .id(1L)
            .value(BigDecimal.valueOf(500))
            .build();

    @BeforeEach
    public void setUp() {
        when(balanceRepository.findById(1L)).thenReturn(Optional.of(mockBalanceEntity));
    }

    private final ModelMapper mapper = new ModelMapper();

    @Test
    @DisplayName("Create a successful card transaction")
    void testCreateSuccessfulCardTransaction() {
        CardEntity mockCardEntity = CardEntity.builder()
                .id(1L)
                .cardNumber("123456789087654")
                .password("1234@abc#12")
                .balance(mockBalanceEntity)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.findByCardNumber("123456789087654")).thenReturn(Optional.of(mockCardEntity));

        TransactionCreateModel mockTransactionModel = TransactionCreateModel.builder()
                .cardNumber(mockCardEntity.getCardNumber())
                .password(mockCardEntity.getPassword())
                .value(BigDecimal.valueOf(10.50))
                .build();

        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(mapper.map(mockTransactionModel, TransactionEntity.class));

        String salvaTransacao = transactionService.save(mockTransactionModel);

        Assertions.assertNotNull(salvaTransacao);
        assertEquals("OK", salvaTransacao);
    }

    @Test
    @DisplayName("Find all successful transactions")
    void testFindAll() {
        BalanceEntity balance = new BalanceEntity();
        CardEntity mockCardEntity = CardEntity.builder()
                .id(1L)
                .cardNumber("220220220220220")
                .balance(balance)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.findById(1L)).thenReturn(Optional.of(mockCardEntity));

        List<TransactionEntity> mockTransactionEntities = Stream.of(
                    TransactionEntity.builder()
                        .id(1L)
                        .card(mockCardEntity)
                        .value(BigDecimal.valueOf(10.50))
                        .build(),
                    TransactionEntity.builder()
                        .id(2L)
                        .card(mockCardEntity)
                        .value(BigDecimal.valueOf(11.55))
                        .build())
                .collect(Collectors.toList());

        when(transactionRepository.findAll()).thenReturn(mockTransactionEntities);

        List<TransactionModel> transactions = transactionService.findAll();
        List< TransactionEntity > transctionsList = transactions.stream().map(entity -> mapper.map(entity, TransactionEntity.class)).collect(Collectors.toList());

        Assertions.assertNotNull(transctionsList);
        assertEquals(mockTransactionEntities, transctionsList);
    }

    @Test
    @DisplayName("Find all invalid transaction")
    void testFindAllInvalid() {
        BalanceEntity balance = new BalanceEntity();
        CardEntity mockCardEntity = CardEntity.builder()
                .id(1L)
                .cardNumber("220220220220220")
                .balance(balance)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.findById(1L)).thenReturn(Optional.of(mockCardEntity));

        List<TransactionEntity> mockTransactionEntities = Stream.of(
                        TransactionEntity.builder()
                                .id(1L)
                                .card(mockCardEntity)
                                .value(BigDecimal.valueOf(10.50))
                                .build(),
                        TransactionEntity.builder()
                                .id(2L)
                                .card(mockCardEntity)
                                .value(BigDecimal.valueOf(11.55))
                                .build())
                .collect(Collectors.toList());

        when(transactionRepository.findAll()).thenReturn(new ArrayList<>());

        try {
            List<TransactionModel> transactions = transactionService.findAll();
            List< TransactionEntity > transactionList = transactions.stream().map(entity -> mapper.map(entity, TransactionEntity.class)).collect(Collectors.toList());

            Assertions.assertNotNull(transactionList);
            assertEquals(mockTransactionEntities, transactionList);
        } catch (Exception e) {
            assertEquals("Transaction not found.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Find successful transaction by ID")
    void testFindTransactionById() {
        CardEntity mockCardEntity = CardEntity.builder()
                .id(1L)
                .cardNumber("220220220220220")
                .balance(mockBalanceEntity)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.findById(1L)).thenReturn(Optional.of(mockCardEntity));

        TransactionEntity mockTransactionEntity = TransactionEntity.builder()
                .id(1L)
                .card(mockCardEntity)
                .value(BigDecimal.valueOf(10.50))
                .build();

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransactionEntity));

        TransactionModel transaction = transactionService.findById(1L);
        TransactionEntity transactionEntity = mapper.map(transaction, TransactionEntity.class);
        transactionEntity.setCard(mockCardEntity);

        Assertions.assertNotNull(transactionEntity);
        assertEquals(mockTransactionEntity, transactionEntity);
    }

    @Test
    @DisplayName("Not find transaction with invalid ID")
    void testNotFindTransactionByIdInvalid() {
        CardEntity mockCardEntity = CardEntity.builder()
                .id(1L)
                .cardNumber("330330330330330")
                .password("cccccccccc")
                .balance(mockBalanceEntity)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.findById(1L)).thenReturn(Optional.of(mockCardEntity));

        TransactionEntity mockTransactionEntity = TransactionEntity.builder()
                .id(1L)
                .card(mockCardEntity)
                .value(BigDecimal.valueOf(10.50))
                .build();

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransactionEntity));

        try {
            transactionService.findById(2L);
        } catch (Exception e) {
            assertEquals("Transaction not found.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Delete transaction by ID")
    void testDeleteTransactionById() {
        CardEntity mockCardEntity = CardEntity.builder()
                .id(1L)
                .cardNumber("440440440440440")
                .password("ddddddddddd")
                .balance(mockBalanceEntity)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.findByCardNumber("440440440440440")).thenReturn(Optional.of(mockCardEntity));

        TransactionEntity mockTransactionEntity = TransactionEntity.builder()
                .id(1L)
                .card(mockCardEntity)
                .value(BigDecimal.valueOf(10.50))
                .build();

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransactionEntity));

        String transactionDeleted = transactionService.deleteById(1L);

        Assertions.assertNotNull(transactionDeleted);
        assertEquals("Transação excluída com sucesso.", transactionDeleted);
    }

    @Test
    @DisplayName("Delete transaction with invalid ID")
    void testTryDeleteTransactionByIdInvalid() {
        CardEntity mockCardEntity = CardEntity.builder()
                .id(1L)
                .cardNumber("550550550550550")
                .password("eeeeeeeeeeee")
                .balance(mockBalanceEntity)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.findById(1L)).thenReturn(Optional.of(mockCardEntity));

        when(transactionRepository.existsById(1L)).thenReturn(true);

        try {
            transactionService.deleteById(2L);
        } catch (Exception e) {
            assertEquals("Transaction not found.", e.getMessage());
        }
    }

}
