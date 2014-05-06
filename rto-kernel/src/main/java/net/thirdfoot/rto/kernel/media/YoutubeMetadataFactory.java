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
public class YoutubeMetadataFactory {

  public static YoutubeMetadata create(String url) {
    try {
      JStopWatch stopWatch = null;

      if (_log.isDebugEnabled()) {
        stopWatch = new JStopWatch();
      }

      PyObject pyYoutubeMetadata = _getInstance()._youtubeStreamerClass.__call__(
        new PyString(url));

      if (stopWatch != null) {
        _log.debug("Object created under " + stopWatch.elapsed() + " ms");
      }

      return new YoutubeMetadata(pyYoutubeMetadata);
    } catch (PyException pye) {
      throw new YoutubeException(pye);
    }
  }

  private YoutubeMetadataFactory() {
    PySystemState pySystemState = Py.getSystemState();

    pySystemState.path.append(
      new PyString("/home/lcsontos/devtools/jython-2.7b1/Lib"));

    PyObject pyImporter = pySystemState.getBuiltins().__getitem__(
      new PyString("__import__"));

    PyObject pyModule = pyImporter.__call__(
      new PyObject[] {
        new PyString("youtube"), new PyString("youtube_metadata")},
      new String[] {"name", "fromlist"});

    _youtubeStreamerClass = pyModule.__getattr__("youtube_metadata");
  }

  private static YoutubeMetadataFactory _getInstance() {
    if (_instance == null) {
      _instance = new YoutubeMetadataFactory();
    }

    return _instance;
  }

  private static YoutubeMetadataFactory _instance =
    new YoutubeMetadataFactory();

  private static Logger _log = LoggerFactory.getLogger(
    YoutubeMetadataFactory.class);

  private PyObject _youtubeStreamerClass;

}