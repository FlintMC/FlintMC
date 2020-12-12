package net.flintmc.framework.inject.method;

public class MethodInjectorGenerationException extends RuntimeException {
    public MethodInjectorGenerationException(String message) {
        super(message);
    }

    public MethodInjectorGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
