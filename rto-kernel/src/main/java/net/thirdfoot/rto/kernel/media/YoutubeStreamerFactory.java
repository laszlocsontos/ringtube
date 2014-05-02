package net.thirdfoot.rto.kernel.media;

import jodd.datetime.JStopWatch;

import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lcsontos
 */
public class YoutubeStreamerFactory {

  public static YoutubeStreamer create(String url) {
    try {
      JStopWatch stopWatch = null;

      if (_log.isDebugEnabled()) {
        stopWatch = new JStopWatch();
      }

      PyObject youtubeStreamer = _getInstance()._youtubeStreamerClass.__call__(
        new PyString(url));

      if (stopWatch != null) {
        _log.debug("Object created under " + stopWatch.elapsed() + " ms");
      }

      return (YoutubeStreamer)youtubeStreamer.__tojava__(
        YoutubeStreamer.class);
    } catch (PyException pye) {
      throw new YoutubeStreamerException(pye);
    }
  }

  private YoutubeStreamerFactory() {
    PySystemState pySystemState = Py.getSystemState();

    pySystemState.path.append(
      new PyString("/home/lcsontos/devtools/jython-2.7b1/Lib"));

    PyObject pyImporter = pySystemState.getBuiltins().__getitem__(
      new PyString("__import__"));

    PyObject pyModule = pyImporter.__call__(
      new PyObject[] {
        new PyString("youtube"), new PyString("youtube_streamer")},
      new String[] {"name", "fromlist"});

    _youtubeStreamerClass = pyModule.__getattr__("youtube_streamer");
  }

  private static YoutubeStreamerFactory _getInstance() {
    if (_instance == null) {
      _instance = new YoutubeStreamerFactory();
    }

    return _instance;
  }

  private static YoutubeStreamerFactory _instance =
    new YoutubeStreamerFactory();

  private static Logger _log = LoggerFactory.getLogger(
    YoutubeStreamerFactory.class);

  private PyObject _youtubeStreamerClass;

}