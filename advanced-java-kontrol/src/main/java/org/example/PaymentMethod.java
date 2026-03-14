package org.example;
import java.math.BigDecimal;

public interface PaymentMethod {
    void pay(Money amount) throws AppException;
}

class CardPayment implements PaymentMethod {
    @Override
    public void pay(Money amount) {
        if (amount.getAmount().compareTo(new BigDecimal("35000")) > 0) {
            throw new AppException("Card payment limit exceeded (max 35000)");
        }
    }
}

class PayPalPayment implements PaymentMethod {
    @Override
    public void pay(Money amount) {
        if (amount.getAmount().compareTo(new BigDecimal("400")) < 0) {
            throw new AppException("PayPal minimum amount is 400");
        }
    }
}

class BankTransferPayment implements PaymentMethod {
    @Override
    public void pay(Money amount) {
    }
}