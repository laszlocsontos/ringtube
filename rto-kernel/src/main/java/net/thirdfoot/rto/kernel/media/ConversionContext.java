package net.thirdfoot.rto.kernel.media;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lcsontos
 */
public class ConversionContext {

  public ConversionContext() {
  }

  @SuppressWarnings("unchecked")
  public <T> T get(ConversionAttribute attribute) {
    return (T)_attributes.get(attribute);
  }

  public ConversionContext set(ConversionAttribute attribute, Object value) {
    if (attribute == null) {
      throw new IllegalArgumentException("Attribute cannot be null");
    }

    if (value == null) {
      _attributes.remove(attribute);
    }

    _attributes.put(attribute, value);

    return this;
  }

  private Map<ConversionAttribute, Object> _attributes =
    new LinkedHashMap<ConversionAttribute, Object>();

}