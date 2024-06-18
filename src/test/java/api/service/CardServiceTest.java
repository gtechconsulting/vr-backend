package api.service;

import api.ApplicationTests;
import api.entity.CardEntity;
import api.entity.BalanceEntity;
import api.enums.CardStatus;
import api.model.CardModel;
import api.model.CardCreateModel;
import api.repository.CardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CardServiceTest extends ApplicationTests {

    @Autowired
    private CardService cardService;

    @MockBean
    private CardRepository cardRepository;

    private BalanceEntity balance = new BalanceEntity();

    private ModelMapper mapper = new ModelMapper();

    @Test
    @DisplayName("Cria o Cartão com sucesso")
    void testSaveCartao() {
        CardCreateModel mockCartaoModel = CardCreateModel.builder()
                .cardNumber("1111111111")
                .password(null)
                .build();

        CardEntity cardEntity = mapper.map(mockCartaoModel, CardEntity.class);

        when(cardRepository.save(any(CardEntity.class))).thenReturn( cardEntity);

        CardModel saveCardModel = cardService.save(mockCartaoModel);
        CardCreateModel cardCreateModel = mapper.map(saveCardModel, CardCreateModel.class);

        Assertions.assertNotNull( cardEntity);
        assertEquals(mockCartaoModel, cardCreateModel);
    }

    @Test
    @DisplayName("Localiza todos os Cartões por Status com sucesso")
    void testFindAllByStatus() {
        List<CardEntity> mockListCartoesEntities = Stream.of(
                        CardEntity.builder()
                                .id(1L)
                                .cardNumber("1111111111")
                                .balance(balance)
                                .status(CardStatus.ATIVO)
                                .build(),
                        CardEntity.builder()
                                .id(2L)
                                .cardNumber("2222222222")
                                .balance(balance)
                                .status(CardStatus.ATIVO)
                                .build())
                .collect(Collectors.toList());

        when(cardRepository.findAllByStatusOrderByCardNumberAsc(CardStatus.ATIVO))
                .thenReturn(mockListCartoesEntities);

        List<CardModel> cards = cardService.findAllByStatusOrderByCardNumberAsc(CardStatus.ATIVO);
        List< CardEntity > listaCartoes = cards.stream().map(entity -> mapper.map(entity, CardEntity.class)).collect(Collectors.toList());

        Assertions.assertNotNull(listaCartoes);
        assertEquals(mockListCartoesEntities, listaCartoes);
    }

    @Test
    @DisplayName("Localiza todos os Cartões por Status inválido")
    void testFindAllByStatusInvalid() {
        List<CardEntity> mockListCartoesEntities = Stream.of(
                        CardEntity.builder()
                                .id(1L)
                                .cardNumber("1111111111")
                                .balance(balance)
                                .status(CardStatus.ATIVO)
                                .build(),
                        CardEntity.builder()
                                .id(2L)
                                .cardNumber("2222222222")
                                .balance(balance)
                                .status(CardStatus.ATIVO)
                                .build())
                .collect(Collectors.toList());

        when(cardRepository.findAllByStatusOrderByCardNumberAsc(CardStatus.ATIVO))
                .thenReturn(mockListCartoesEntities);

        try {
            List<CardModel> cards = cardService.findAllByStatusOrderByCardNumberAsc(CardStatus.INATIVO);
            List< CardEntity > listaCartoes = cards.stream().map(entity -> mapper.map(entity, CardEntity.class)).collect(Collectors.toList());
            assertNotEquals(mockListCartoesEntities, listaCartoes);
        } catch (Exception e) {
            assertEquals("Card not found.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Localiza o Cartão por ID com sucesso")
    void testFindCartaoById() {
        CardEntity mockCartaoEntity = CardEntity.builder()
                .id(1L)
                .cardNumber("1111111111")
                .balance(balance)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.findById(1L)).thenReturn(Optional.of(mockCartaoEntity));

        CardModel findCartao = cardService.findCardById(1L);
        CardEntity cardEntity = mapper.map(findCartao, CardEntity.class);

        Assertions.assertNotNull( cardEntity);
        assertEquals(mockCartaoEntity, cardEntity);
    }

    @Test
    @DisplayName("Localiza o Cartão por ID inválido")
    void testFindCartaoByIdInvalid() {
        CardEntity mockCartaoEntity = CardEntity.builder()
                .id(1L)
                .cardNumber("1111111111")
                .balance(balance)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.findById(1L)).thenReturn(Optional.of(mockCartaoEntity));

        try {
            cardService.findCardById(2L);
        } catch (Exception e) {
            assertEquals("Card not found.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Localiza o Cartão por Número do Cartão com sucesso")
    void testFindCartaoByNumber() {
        CardEntity mockCartaoEntity = CardEntity.builder()
                .cardNumber("1111111111")
                .balance(balance)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.findByCardNumber("1111111111")).thenReturn(Optional.of(mockCartaoEntity));

        BigDecimal findCartao = cardService.findCardByNumber("1111111111");

        Assertions.assertNotNull(findCartao);
        assertEquals(mockCartaoEntity.getBalance().getValue(), findCartao);
    }

    @Test
    @DisplayName("Localiza o Cartão por Número do Cartão inválido")
    void testFindCartaoByNumberInvalid() {
        CardEntity mockCartaoEntity = CardEntity.builder()
                .cardNumber("1111111111")
                .balance(balance)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.findByCardNumber("1111111111")).thenReturn(Optional.of(mockCartaoEntity));

        try {
            cardService.findCardByNumber("555555555");
        } catch (Exception e) {
            assertEquals("", e.getMessage());
        }
    }

    @Test
    @DisplayName("Altera o Cartão por ID com sucesso")
    void testUpdateCartao() {
        CardEntity mockCartaoEntity = CardEntity.builder()
                .cardNumber("1111111111")
                .password("xxxxxxxx")
                .balance(balance)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.existsById(1L)).thenReturn(true);

        mockCartaoEntity.setCardNumber("3333333333");
        when(cardRepository.save(any(CardEntity.class))).thenReturn(mockCartaoEntity);

        CardModel updateCardModel = cardService.update(1L, mockCartaoEntity);
        CardEntity cardEntity = mapper.map(updateCardModel, CardEntity.class);
        cardEntity.setBalance(mockCartaoEntity.getBalance());
        cardEntity.setPassword(mockCartaoEntity.getPassword());

        Assertions.assertNotNull( cardEntity);
        assertEquals(mockCartaoEntity, cardEntity);
    }

    @Test
    @DisplayName("Altera o Cartão por ID inválido")
    void testUpdateCartaoIdInvalid() {
        CardEntity mockCartaoEntity = CardEntity.builder()
                .cardNumber("1111111111")
                .password("2222222222")
                .balance(balance)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.existsById(1L)).thenReturn(true);

        mockCartaoEntity.setCardNumber("44444444444");
        when(cardRepository.save(any(CardEntity.class))).thenReturn(mockCartaoEntity);

        try {
            cardService.update(2L, mockCartaoEntity);
        } catch (Exception e) {
            assertEquals("Card not found.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Exclui o Cartão por ID com sucesso")
    void testDeleteCartaoById() {
        CardEntity mockCartaoEntity = CardEntity.builder()
                .cardNumber("1111111111")
                .password("2222222222")
                .balance(balance)
                .status(CardStatus.ATIVO)
                .build();

        when(cardRepository.existsById(1L)).thenReturn(true);

        String deletedCard = cardService.deleteCardById(1L);

        Assertions.assertNotNull(deletedCard);
        assertEquals("Cartão excluído com sucesso.", deletedCard);
    }

}
