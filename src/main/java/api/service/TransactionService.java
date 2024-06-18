package api.service;

import api.entity.BalanceEntity;
import api.entity.CardEntity;
import api.entity.TransactionEntity;
import api.enums.CardStatus;
import api.exception.ModelException;
import api.model.TransactionCreateModel;
import api.model.TransactionModel;
import api.model.errors.CardErrors;
import api.model.errors.CommonErrors;
import api.model.errors.TransactionErrors;
import api.repository.BalanceRepository;
import api.repository.CardRepository;
import api.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final BalanceRepository balanceRepository;
    private final ModelMapper mapper = new ModelMapper();
    private static final String DEBIT_TRANSACTION_TYPE = "debito";
    private static final String CREDIT_TRANSACTION_TYPE = "credito";

    public TransactionService(TransactionRepository transactionRepository, CardRepository cardRepository, BalanceRepository balanceRepository) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.balanceRepository = balanceRepository;
    }

    public TransactionModel findById(Long id ) {
        TransactionEntity transaction = transactionRepository.findById( id ).orElseThrow(() -> new ModelException(TransactionErrors.NOT_FOUND));
        return mapper.map(transaction, TransactionModel.class);
    }

    public List<TransactionModel> findAll() {
        List<TransactionEntity> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            throw new ModelException(TransactionErrors.NOT_FOUND);
        }
        return transactions.stream().map(entity -> mapper.map(entity, TransactionModel.class)).collect(Collectors.toList());
    }

    public String save(TransactionCreateModel transactionCreateModel) {
        Optional<CardEntity> card = cardRepository.findByCardNumber(transactionCreateModel.getCardNumber());
        if(card.isPresent()) {
            if(card.get().getStatus().equals(CardStatus.ATIVO)) {
                if(card.get().getPassword().equals(transactionCreateModel.getPassword())) {
                    if(card.get().getBalance().getValue().compareTo(transactionCreateModel.getValue()) >= 0) {
                        updateBalance(card.get(), transactionCreateModel.getValue(), DEBIT_TRANSACTION_TYPE);
                        TransactionEntity transactionEntity = mapper.map(transactionCreateModel, TransactionEntity.class);
                        transactionEntity.setCard(card.get());
                        transactionRepository.save(transactionEntity);
                        return "OK";
                    }
                    throw new ModelException(TransactionErrors.INSUFFICIENT_BALANCE);
                }
                throw new ModelException(CommonErrors.INVALID_PASSWORD);
            }
            throw new ModelException(CardErrors.INACTIVE_CARD);
        }
        throw new ModelException(CardErrors.INVALID_NUMBER_CARD);
    }

    public void updateBalance(CardEntity card, BigDecimal transactionValue, String transactionType) {
        BalanceEntity balanceEntity = balanceRepository.findById(card.getBalance().getId()).orElseThrow(() -> new ModelException(TransactionErrors.NOT_FOUND));
        BigDecimal newValue = (transactionType.equals(DEBIT_TRANSACTION_TYPE)) ? balanceEntity.getValue().subtract(transactionValue) : balanceEntity.getValue().add(transactionValue);
        balanceEntity.setValue(newValue);
        balanceRepository.save(balanceEntity);
    }

    public String deleteById(Long id ) {
        TransactionEntity transactionEntity = transactionRepository.findById(id).orElseThrow(() -> new ModelException(TransactionErrors.NOT_FOUND));;
        Optional<CardEntity> card = cardRepository.findByCardNumber(transactionEntity.getCard().getCardNumber());

        if (card.isEmpty()) {
            throw new ModelException(TransactionErrors.NOT_FOUND);
        }

        transactionRepository.deleteById( id );
        updateBalance(card.get(), transactionEntity.getValue(), CREDIT_TRANSACTION_TYPE);
        return "Extorno realizado com sucesso.";
    }

}