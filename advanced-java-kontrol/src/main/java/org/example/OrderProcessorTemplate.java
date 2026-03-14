package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OrderProcessorTemplate {
    private static final Logger log = LoggerFactory.getLogger(OrderProcessorTemplate.class);

    public final void process(Order order, PaymentMethod paymentMethod) throws InfrastructureException {
        try {
            log.info("Starting processing for order: {}", order.getId());

            validate(order);
            calculateTotal(order);

            if (order.isFirstOrder()) {
                registerFirstOrderBonus(order);
            }

            pay(order, paymentMethod);
            finishProcessing(order);

            log.info("Order {} processed successfully.", order.getId());
        } catch (AppException e) {
            log.warn("Business validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during processing", e);
            throw new InfrastructureException("Infrastructure failed", e);
        }
    }

    protected abstract void validate(Order order);
    protected abstract void calculateTotal(Order order);
    protected abstract void registerFirstOrderBonus(Order order);

    private void pay(Order order, PaymentMethod paymentMethod) {
        paymentMethod.pay(order.getTotalAmount());
        order.setStatus(OrderStatus.PAID);
    }

    private void finishProcessing(Order order) {
        order.setStatus(OrderStatus.SHIPPED);
    }
}