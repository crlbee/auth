package com.auth.controller;

import com.auth.service.PaymentService;
import com.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    @Autowired
    public PaymentController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @PostMapping("/payment")
    public ResponseEntity<String> processPayment(@AuthenticationPrincipal UserDetails user) {
        paymentService.processPayment(userService.findByUsername(user.getUsername()).get());
        return ResponseEntity.ok("Платеж успешен");
    }
}
