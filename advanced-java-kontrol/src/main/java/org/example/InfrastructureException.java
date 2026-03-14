package org.example;

public class InfrastructureException extends Exception {
    public InfrastructureException(String message, Exception e) {
        super(message);
    }
}
