package me.sieric;

public class InjectionCycleException extends Exception {
    public InjectionCycleException(String message) {
        super(message);
    }
}
