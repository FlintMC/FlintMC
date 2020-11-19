package net.flintmc.util.property;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Defines properties that can be set to a {@link PropertyContextAware}.
 *
 * @param <T_PropertyValue> the type of the properties value
 * @param <T_PropertyMeta>  the type of the properties metadata. If not required, use {@link Void}.
 */
public interface Property<T_PropertyValue, T_PropertyMeta> {
  static Builder<Object, Void> builder() {
    return new Builder<>();
  }

  /**
   * @param propertyValue the value to check
   * @return validation if the provided value is suitable for this property
   */
  boolean validateValue(T_PropertyValue propertyValue);

  /**
   * @param propertyMeta the meta to check
   * @return validation if the provided meta is suitable for this property
   */
  boolean validateMeta(T_PropertyMeta propertyMeta);

  /**
   * @return the default value of this property
   */
  T_PropertyValue getDefaultValue();

  /**
   * @return the default meta of this property
   */
  T_PropertyMeta getDefaultMeta();

  @SuppressWarnings("unchecked")
  class Builder<T_PropertyValue, T_PropertyMeta> {
    private final Predicate<T_PropertyValue> defaultValuePredicate = Objects::nonNull;
    private final Predicate<T_PropertyMeta> defaultMetaPredicate = Objects::nonNull;
    private final Supplier<T_PropertyValue> defaultDefaultValue = () -> null;
    private final Supplier<T_PropertyMeta> defaultDefaultMeta = () -> null;

    private Predicate<T_PropertyValue> valuePredicate = defaultValuePredicate;
    private Predicate<T_PropertyMeta> metaPredicate = defaultMetaPredicate;
    private Supplier<T_PropertyValue> defaultValue = defaultDefaultValue;
    private Supplier<T_PropertyMeta> defaultMeta = defaultDefaultMeta;

    private Builder() {
    }

    public <T_NewPropertyMeta> Builder<T_PropertyValue, T_NewPropertyMeta> withMeta(
        Class<T_NewPropertyMeta> clazz) {
      return this.withMeta();
    }

    public <T_NewPropertyMeta> Builder<T_PropertyValue, T_NewPropertyMeta> withMeta() {
      if (!this.metaPredicate.equals(defaultMetaPredicate)) {
        throw new IllegalStateException("Cannot change meta type after meta validator was set.");
      }
      if (!this.defaultMeta.equals(defaultDefaultMeta)) {
        throw new IllegalStateException("Cannot change meta type after meta default was set.");
      }
      return (Builder<T_PropertyValue, T_NewPropertyMeta>) this;
    }

    public <T_NewPropertyValue> Builder<T_NewPropertyValue, T_PropertyMeta> withValue(
        Class<T_NewPropertyValue> clazz) {
      return this.withValue();
    }

    public <T_NewPropertyValue> Builder<T_NewPropertyValue, T_PropertyMeta> withValue() {
      if (!this.valuePredicate.equals(defaultValuePredicate)) {
        throw new IllegalStateException("Cannot change value type after value validator was set.");
      }
      if (!this.defaultValue.equals(defaultDefaultValue)) {
        throw new IllegalStateException("Cannot change value type after value default was set.");
      }
      return (Builder<T_NewPropertyValue, T_PropertyMeta>) this;
    }

    public Builder<T_PropertyValue, T_PropertyMeta> withValueValidator(
        Predicate<T_PropertyValue> valuePredicate) {
      this.valuePredicate = valuePredicate;
      return this;
    }

    public Builder<T_PropertyValue, T_PropertyMeta> withMetaValidator(
        Predicate<T_PropertyMeta> metaPredicate) {
      this.metaPredicate = metaPredicate;
      return this;
    }

    public Builder<T_PropertyValue, T_PropertyMeta> withDefaultValue(T_PropertyValue value) {
      return this.withDefaultValue(() -> value);
    }

    public Builder<T_PropertyValue, T_PropertyMeta> withDefaultValue(
        Supplier<T_PropertyValue> defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    public Builder<T_PropertyValue, T_PropertyMeta> withDefaultMeta(T_PropertyMeta meta) {
      return this.withDefaultMeta(() -> meta);
    }

    public Builder<T_PropertyValue, T_PropertyMeta> withDefaultMeta(
        Supplier<T_PropertyMeta> defaultMeta) {
      this.defaultMeta = defaultMeta;
      return this;
    }

    public Property<T_PropertyValue, T_PropertyMeta> build() {
      return new Property<T_PropertyValue, T_PropertyMeta>() {
        public boolean validateValue(T_PropertyValue t_propertyValue) {
          return valuePredicate.test(t_propertyValue);
        }

        public boolean validateMeta(T_PropertyMeta t_propertyMeta) {
          return metaPredicate.test(t_propertyMeta);
        }

        public T_PropertyValue getDefaultValue() {
          return defaultValue.get();
        }

        public T_PropertyMeta getDefaultMeta() {
          return defaultMeta.get();
        }
      };
    }
  }
}
