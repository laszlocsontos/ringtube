package net.thirdfoot.rto.kernel.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.thirdfoot.rto.kernel.filter.BaseFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
public class ThreadLocalRegistry {

  public static void clearThreadLocals() {
    _threadLocals.clear();
  }

  public static <T> ThreadLocal<T> registerThreadLocal(
    String name, T initialValue) {

    ThreadLocal<T> threadLocal = (ThreadLocal<T>)_threadLocals.get(name);

    if (threadLocal == null) {
      threadLocal = new InitialThreadLocal<T>(name, initialValue);

      _threadLocals.putIfAbsent(name, threadLocal);
    }

    return threadLocal;
  }

  public static void resetThreadLocals() {
    for (ThreadLocal<?> threadLocal : _threadLocals.values()) {

      if (_log.isTraceEnabled()) {
        _log.trace("resetting thread local " + threadLocal);
      }

      threadLocal.remove();
    }
  }

  private static Logger _log = LoggerFactory.getLogger(BaseFilter.class);

  private static ConcurrentMap<String, ThreadLocal<?>> _threadLocals =
    new ConcurrentHashMap<String, ThreadLocal<?>>();

  private ThreadLocalRegistry() {
  }

}