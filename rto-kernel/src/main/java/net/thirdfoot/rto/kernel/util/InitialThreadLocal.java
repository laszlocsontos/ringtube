package net.thirdfoot.rto.kernel.util;

import jodd.util.StringUtil;

/**
 * @author lcsontos
 */
public class InitialThreadLocal<T> extends ThreadLocal<T> {

  @Override
  public String toString() {
    if (StringUtil.isBlank(_name)) {
      return super.toString();
    }

    return _name;
  }

  protected InitialThreadLocal(String name, T initialValue) {
    _name = name;
    _initialValue = initialValue;
  }

  @Override
  protected T initialValue() {
    return _initialValue;
  }

  private String _name;
  private T _initialValue;

}