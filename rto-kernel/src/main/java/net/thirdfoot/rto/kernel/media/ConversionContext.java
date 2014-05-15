package net.thirdfoot.rto.kernel.media;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lcsontos
 */
public class ConversionContext {

  public ConversionContext() {
  }

  public <T> T get(ConversionAttribute attribute) {
    return (T)_attributes.get(attribute);
  }

  public void set(ConversionAttribute attribute, Object value) {
    if (attribute == null) {
      throw new IllegalArgumentException("Attribute cannot be null");
    }

    if (value == null) {
      _attributes.remove(attribute);
    }

    _attributes.put(attribute, value);
  }

  private Map<ConversionAttribute, Object> _attributes =
    new LinkedHashMap<ConversionAttribute, Object>();

}