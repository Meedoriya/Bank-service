package com.example.bankapp.service;

import com.example.bankapp.model.TransferBalance;
import com.example.bankapp.repository.BalanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class BankService {


    private final BalanceRepository balanceRepository;

    public BigDecimal getBalance(Long accountId) {
        BigDecimal balance = balanceRepository.getBalanceForId(accountId);
        if (balance == null) {
            throw new IllegalArgumentException();
        }
        return balance;

    }
    public BigDecimal addMoney(Long to, BigDecimal amount) {
        BigDecimal currentAmount = balanceRepository.getBalanceForId(to);
        if (currentAmount == null) {
            balanceRepository.save(to, amount);
            return amount;
        } else {
            BigDecimal updatedAmount = currentAmount.add(amount);
            balanceRepository.save(to, updatedAmount);
            return updatedAmount;
        }    }

    public void makeTransfer(TransferBalance transferBalance) {
        BigDecimal fromBalance = balanceRepository.getBalanceForId(transferBalance.getFrom());
        BigDecimal toBalance = balanceRepository.getBalanceForId(transferBalance.getTo());
        if (fromBalance == null || toBalance == null) throw new IllegalArgumentException("no ids");
        if (transferBalance.getAmount().compareTo(fromBalance) > 0) throw new IllegalArgumentException("no money");
        BigDecimal updatedFromBalance = fromBalance.subtract(transferBalance.getAmount());
        BigDecimal updatedToBalance = toBalance.add(transferBalance.getAmount());
        balanceRepository.save(transferBalance.getFrom(), updatedFromBalance);
        balanceRepository.save(transferBalance.getTo(), updatedToBalance);


    }
}
