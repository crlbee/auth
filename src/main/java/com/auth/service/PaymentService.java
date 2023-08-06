package com.auth.service;

import com.auth.entity.Payment;
import com.auth.entity.User;
import com.auth.exception.BalanceAmountException;
import com.auth.repository.PaymentRepository;
import com.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(UserRepository userRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void processPayment(User user) {
        BigDecimal paymentAmount = BigDecimal.valueOf(1.1);
        BigDecimal currentBalance = user.getBalance();

        if (currentBalance.compareTo(paymentAmount) >= 0) {
            user.setBalance(currentBalance.subtract(paymentAmount));
            userRepository.save(user);
            Payment payment = new Payment();
            payment.setUser(user);
            payment.setAmount(paymentAmount);
            paymentRepository.save(payment);
        } else {
            throw new BalanceAmountException("Недостаточно денег на счете");
        }
    }
}
