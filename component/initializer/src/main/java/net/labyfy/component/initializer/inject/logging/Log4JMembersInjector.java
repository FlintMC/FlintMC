package net.labyfy.component.initializer.inject.logging;

import com.google.inject.MembersInjector;
import net.labyfy.component.inject.logging.LoggingProvider;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class Log4JMembersInjector<T> implements MembersInjector<T> {

    private final Field field;
    private final Logger logger;

    Log4JMembersInjector(Field field, LoggingProvider provider) {
        this.field = field;
        this.logger = provider.getLogger(field.getDeclaringClass());
    }

    @Override
    public void injectMembers(T instance) {
        try {
            field.setAccessible(true);
            field.set(instance, logger);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
