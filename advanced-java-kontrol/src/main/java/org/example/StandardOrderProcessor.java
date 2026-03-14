package org.example;
import java.math.BigDecimal;

public class StandardOrderProcessor extends OrderProcessorTemplate {

    @Override
    protected void validate(Order order) {
        boolean hasPhysical = false;
        for (OrderItem item : order.getItems()) {
            if (item.getType() == ProductType.PHYSICAL) {
                hasPhysical = true;
                break;
            }
        }
        if (!hasPhysical) {
            throw new AppException("Order must contain at least one PHYSICAL product (Rule R8)");
        }
    }

    @Override
    protected void calculateTotal(Order order) {
        BigDecimal sum = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            sum = sum.add(item.getPrice().getAmount());
        }

        if (order.isFirstOrder()) {
            BigDecimal discount = sum.multiply(new BigDecimal("0.06"));
            sum = sum.subtract(discount);
        }
        order.setTotalAmount(new Money(sum));
    }

    @Override
    protected void registerFirstOrderBonus(Order order) {
        if (order.getTotalAmount().getAmount().compareTo(BigDecimal.TEN) < 0) {
            throw new FirstOrderRuleException("Total too low for first order bonus");
        }
    }
}