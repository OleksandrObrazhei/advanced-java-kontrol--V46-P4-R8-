package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderProcessorTest {

    private StandardOrderProcessor processor;
    private OrderItem physicalItem;
    private OrderItem digitalItem;

    @BeforeEach
    void setUp() {
        processor = new StandardOrderProcessor();
        physicalItem = new OrderItem("Book", ProductType.PHYSICAL, new Money(new BigDecimal("1000")));
        digitalItem = new OrderItem("E-Book", ProductType.DIGITAL, new Money(new BigDecimal("500")));
    }

    @Test
    void testSuccessfulProcessing() throws InfrastructureException {
        Order order = new Order("1", "test@test.com", new OrderItem[]{physicalItem});
        processor.process(order, new BankTransferPayment());
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"100", "5000", "35000"})
    void testCardPaymentValidAmounts(String amount) {
        CardPayment card = new CardPayment();
        assertDoesNotThrow(() -> card.pay(new Money(new BigDecimal(amount))));
    }

    @Test
    void testValidationFailsWithoutPhysicalProduct() {
        Order order = new Order("2", "test@test.com", new OrderItem[]{digitalItem});
        Exception e = assertThrows(AppException.class, () -> processor.process(order, new PayPalPayment()));
        assertTrue(e.getMessage().contains("PHYSICAL"));
    }

    @Test
    void testFirstOrderBonusException() {
        OrderItem cheapItem = new OrderItem("Pen", ProductType.PHYSICAL, new Money(new BigDecimal("5")));
        Order order = new Order("3", "test@test.com", new OrderItem[]{cheapItem}, true);
        assertThrows(FirstOrderRuleException.class, () -> processor.process(order, new PayPalPayment()));
    }

    @Test
    void testCardPaymentLimitExceeded() {
        CardPayment card = new CardPayment();
        assertThrows(AppException.class, () -> card.pay(new Money(new BigDecimal("35001"))));
    }

    @Test
    void testPayPalMinimumAmount() {
        PayPalPayment paypal = new PayPalPayment();
        assertThrows(AppException.class, () -> paypal.pay(new Money(new BigDecimal("399"))));
    }

    @Test
    void testMarkAsDeliveredInvalidState() {
        Order order = new Order("4", "test@test.com", new OrderItem[]{physicalItem});
        assertThrows(IllegalStateException.class, order::markAsDelivered);
    }

    @Test
    void testFirstOrderDiscountCalculation() throws InfrastructureException {
        Order order = new Order("5", "test@test.com", new OrderItem[]{physicalItem}, true);
        processor.process(order, new BankTransferPayment());
        assertEquals(new BigDecimal("940.00"), order.getTotalAmount().getAmount().setScale(2));
    }

    @Test
    void testOrderStatusTransitions() throws InfrastructureException {
        Order order = new Order("6", "test@test.com", new OrderItem[]{physicalItem});
        processor.process(order, new PayPalPayment());
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
        order.markAsDelivered();
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    void testDefensiveCopy() {
        OrderItem[] items = {physicalItem};
        Order order = new Order("7", "test@test.com", items);
        items[0] = digitalItem; // Змінюємо оригінальний масив
        assertEquals(ProductType.PHYSICAL, order.getItems()[0].getType()); // В Order має залишитись PHYSICAL
    }
}