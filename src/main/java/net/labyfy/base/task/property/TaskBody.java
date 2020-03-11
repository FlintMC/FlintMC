package net.labyfy.base.task.property;

import net.labyfy.structure.identifier.Identifier;
import net.labyfy.structure.annotation.Transitive;
import net.labyfy.structure.property.Property;
import net.labyfy.base.task.Task;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Identifier(
    parents = Task.class,
    requiredProperties = @Property(value = TaskBodyPriority.class))
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Transitive
public @interface TaskBody {}
