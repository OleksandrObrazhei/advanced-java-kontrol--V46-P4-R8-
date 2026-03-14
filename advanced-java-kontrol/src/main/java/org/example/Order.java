package org.example;
import java.util.Arrays;

public class Order extends BaseEntity {
    private final String customerEmail;
    private final OrderItem[] items;
    private final boolean isFirstOrder;
    private OrderStatus status;
    private Money totalAmount;

    public Order(String id, String customerEmail, OrderItem[] items) {
        this(id, customerEmail, items, false);
    }

    public Order(String id, String customerEmail, OrderItem[] items, boolean isFirstOrder) {
        super(id);
        this.customerEmail = customerEmail;
        this.items = items != null ? Arrays.copyOf(items, items.length) : new OrderItem[0];
        this.isFirstOrder = isFirstOrder;
        this.status = OrderStatus.NEW;
    }

    public OrderItem[] getItems() {
        return Arrays.copyOf(items, items.length);
    }

    public boolean isFirstOrder() { return isFirstOrder; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public Money getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Money totalAmount) { this.totalAmount = totalAmount; }

    public void markAsDelivered() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Can only deliver shipped orders.");
        }
        this.status = OrderStatus.DELIVERED;
    }
}