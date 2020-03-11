package net.labycore.base.task.property;

import net.labycore.structure.identifier.Identifier;
import net.labycore.structure.annotation.Transitive;
import net.labycore.structure.property.Property;
import net.labycore.base.task.Task;

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
