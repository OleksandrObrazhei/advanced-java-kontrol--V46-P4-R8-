package org.example;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Starting Order Processing System ---");

        OrderItem book = new OrderItem("Advanced Java Textbook", ProductType.PHYSICAL, new Money(new BigDecimal("1500")));
        OrderItem ebook = new OrderItem("Java Concurrency PDF", ProductType.DIGITAL, new Money(new BigDecimal("800")));

        OrderItem[] items = {book, ebook};
        Order order = new Order("ORD-001", "student@example.com", items, true);

        OrderProcessorTemplate processor = new StandardOrderProcessor();
        OrderService orderService = new OrderService(processor);

        PaymentMethod payment = new BankTransferPayment();

        try {
            System.out.println("Initiating checkout for Order: " + order.getId());
            orderService.checkout(order, payment);

            System.out.println("Order processed successfully!");
            System.out.println("Current status: " + order.getStatus());

            System.out.println("Total amount paid: " + order.getTotalAmount().getAmount());

            order.markAsDelivered();
            System.out.println("Order has been delivered. Final status: " + order.getStatus());

        } catch (AppException e) {
            System.err.println("Business Validation Error: " + e.getMessage());
        } catch (InfrastructureException e) {
            System.err.println("System Error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("--- Processing Finished ---");
    }
}