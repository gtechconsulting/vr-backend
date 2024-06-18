package api.service;

import api.entity.CardEntity;
import api.entity.BalanceEntity;
import api.enums.CardStatus;
import api.exception.ModelException;
import api.model.CardModel;
import api.model.CardCreateModel;
import api.model.errors.CardErrors;
import api.repository.CardRepository;
import api.repository.BalanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CardService {
    private final CardRepository cardRepository;
    private final BalanceRepository balanceRepository;
    private final ModelMapper mapper = new ModelMapper();

    public CardService(CardRepository cardRepository, BalanceRepository balanceRepository) {
        this.cardRepository = cardRepository;
        this.balanceRepository = balanceRepository;
    }

    public BigDecimal findCardByNumber(String cardNumber ) {
        CardEntity card = cardRepository.findByCardNumber(cardNumber).orElseThrow(() -> new ModelException(CardErrors.INVALID_NUMBER_CARD));
        return card.getBalance().getValue();
    }

    public CardModel findCardById(Long id ) {
        CardEntity card = cardRepository.findById( id ).orElseThrow(() -> new ModelException(CardErrors.NOT_FOUND));
        return mapper.map(card, CardModel.class);
    }

    public List<CardModel> findAllByOrderByCardNumberAsc() {
        List<CardEntity> cards = cardRepository.findAllByOrderByCardNumberAsc();
        if ( cards.isEmpty() ) {
            throw new ModelException(CardErrors.NOT_FOUND);
        }
        return cards.stream().map(entity -> mapper.map(entity, CardModel.class)).collect(Collectors.toList());
    }

    public List<CardModel> findAllByStatusOrderByCardNumberAsc(CardStatus status ) {
        List<CardEntity> cards = cardRepository.findAllByStatusOrderByCardNumberAsc( status );
        if (cards.isEmpty()) {
            throw new ModelException(CardErrors.NOT_FOUND);
        }
        return cards.stream().map(entity -> mapper.map(entity, CardModel.class)).collect(Collectors.toList());
    }

    public CardModel save(CardCreateModel cardCreateModel) {
        CardEntity cardEntity = mapper.map(cardCreateModel, CardEntity.class);
        if(existCard(cardEntity)) {
            throw new ModelException(CardErrors.CARD_EXISTS);
        }
        try {
            BalanceEntity balanceEntity = new BalanceEntity();
            balanceRepository.save(balanceEntity);
            cardEntity.setBalance(balanceEntity);
            cardEntity.setStatus(CardStatus.ATIVO);
            cardEntity = cardRepository.save(cardEntity);
            return mapper.map( cardEntity, CardModel.class);
        } catch (Exception e) {
            throw new ModelException(CardErrors.ERROR_CREATING);
        }
    }

    public CardModel update(Long id, CardEntity cardEntity) {
        CardEntity card = cardRepository.findById( id ).orElse( new CardEntity() );
        if( cardRepository.existsById( id ) ) {
            cardEntity.setId(id);
            BalanceEntity balanceEntity = (card.getBalance() != null) ? balanceRepository.findById( card.getBalance().getId() ).orElse( new BalanceEntity() ) : new BalanceEntity();
            cardEntity.setBalance(balanceEntity);
            cardEntity.setCardNumber( cardEntity.getCardNumber() );
            cardEntity.setPassword( cardEntity.getPassword() );
            cardEntity.setStatus( ( cardEntity.getStatus() != null) ? cardEntity.getStatus() : CardStatus.ATIVO );
            cardEntity.setCreatedAt(card.getCreatedAt());
            cardEntity = cardRepository.save( cardEntity);
            return mapper.map( cardEntity, CardModel.class);
        }
        throw new ModelException(CardErrors.NOT_FOUND);
    }

    public String deleteCardById(Long id ) {
        if(cardRepository.existsById( id )) {
            cardRepository.deleteById( id );
            return "Card deleted.";
        }
        throw new ModelException(CardErrors.NOT_FOUND);
    }

    public boolean existCard(CardEntity cardEntity) {
        return cardRepository.findByCardNumber( cardEntity.getCardNumber() ).isPresent();
    }

}