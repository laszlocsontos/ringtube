package net.thirdfoot.rto.kernel.jython;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.python.core.Py;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
public class PyObjectFactoryUtil {

  public static PyObjectFactory getFactory(
    String moduleName, String className) {

    FactoryKey factoryKey = new FactoryKey(moduleName, className);

    PyObjectFactory pyObjectFactory = _pyObjectFactories.get(factoryKey);

    if (pyObjectFactory != null) {
      return pyObjectFactory;
    }

    pyObjectFactory = new PyObjectFactory(
      _pySystemState, moduleName, className);

    _pyObjectFactories.putIfAbsent(factoryKey, pyObjectFactory);

    return pyObjectFactory;
  }

  private static Logger _log = LoggerFactory.getLogger(
    PyObjectFactoryUtil.class);

  private static ConcurrentMap<FactoryKey, PyObjectFactory> _pyObjectFactories =
    new ConcurrentHashMap<FactoryKey, PyObjectFactory>();

  private static PySystemState _pySystemState = Py.getSystemState();

  static {
    try {
      _pySystemState.path.append(
        new PyString("/home/lcsontos/devtools/jython-2.7b1/Lib"));
    }
    catch (Exception e) {
      _log.error(e.getMessage(), e);
    }
  }

  private PyObjectFactoryUtil() {
  }

  private static class FactoryKey {

    FactoryKey(String moduleName, String className) {
      _moduleName = moduleName;
      _className = className;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      FactoryKey other = (FactoryKey) obj;
      if (_className == null) {
        if (other._className != null)
          return false;
      } else if (!_className.equals(other._className))
        return false;
      if (_moduleName == null) {
        if (other._moduleName != null)
          return false;
      } else if (!_moduleName.equals(other._moduleName))
        return false;
      return true;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((_className == null) ? 0 : _className.hashCode());
      result = prime * result
          + ((_moduleName == null) ? 0 : _moduleName.hashCode());
      return result;
    }

    String _moduleName;
    String _className;

  }

}