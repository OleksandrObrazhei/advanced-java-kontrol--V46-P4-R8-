package org.example;
import java.util.Optional;

public class OrderService {
    private final OrderProcessorTemplate processor;

    public OrderService(OrderProcessorTemplate processor) {
        this.processor = processor;
    }

    public Optional<Order> findById(String id) {
        if ("123".equals(id)) {
            return Optional.of(new Order(id, "test@test.com", new OrderItem[0]));
        }
        return Optional.empty();
    }

    public void checkout(Order order, PaymentMethod payment) throws InfrastructureException {
        processor.process(order, payment);
    }
}