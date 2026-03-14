package org.example;
public class OrderItem {
    private final String name;
    private final ProductType type;
    private final Money price;

    public OrderItem(String name, ProductType type, Money price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }
    public ProductType getType() {
        return type;
    }
    public Money getPrice() {
        return price;
    }
}