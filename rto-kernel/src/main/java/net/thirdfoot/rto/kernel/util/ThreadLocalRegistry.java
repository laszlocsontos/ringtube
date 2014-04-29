package net.thirdfoot.rto.kernel.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
      threadLocal.remove();
    }
  }

  private static ConcurrentMap<String, ThreadLocal<?>> _threadLocals =
    new ConcurrentHashMap<String, ThreadLocal<?>>();

  private ThreadLocalRegistry() {
  }

}