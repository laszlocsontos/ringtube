package net.thirdfoot.rto.kernel.jython;

import jodd.datetime.JStopWatch;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
public class PyObjectFactory {

  public PyObject create(PyObject... args) {
    JStopWatch stopWatch = null;

    if (_log.isDebugEnabled()) {
      stopWatch = new JStopWatch();
    }

    PyObject pyObject = _pyClass.__call__(args);

    if (stopWatch != null) {
      _log.debug("Object created under " + stopWatch.elapsed() + " ms");
    }

    return pyObject;
  }


  PyObjectFactory(
    PySystemState pySystemState, String moduleName, String className) {

    PyObject pyImporter = pySystemState.getBuiltins().__getitem__(
      new PyString("__import__"));
  
    PyObject pyModule = pyImporter.__call__(
      new PyObject[] {
        new PyString(moduleName), new PyString(className)},
      new String[] {"name", "fromlist"});
  
    _pyClass = pyModule.__getattr__(className);
  }

  private static Logger _log = LoggerFactory.getLogger(PyObjectFactory.class);

  private PyObject _pyClass;

}