package net.thirdfoot.rto.kernel.media;

import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

/**
 * @author lcsontos
 */
public class YoutubeStreamerFactory {

  public static YoutubeStreamer create(String url) {
    try {
      YoutubeStreamerFactory factory = _getInstance();

      PyObject youtubeStreamer = factory._youtubeStreamerClass.__call__(
        new PyString(url));

      return (YoutubeStreamer)youtubeStreamer.__tojava__(
        YoutubeStreamer.class);
    } catch (PyException pye) {
      throw new YoutubeStreamerException(pye);
    }
  }

  private YoutubeStreamerFactory() {
    _pySystemState.path.append(
      new PyString("/home/lcsontos/devtools/jython-2.7b1/Lib"));

    _pyInterpreter = new PythonInterpreter(null, _pySystemState);
    _pyInterpreter.exec("from youtube import youtube_streamer");

    _youtubeStreamerClass = _pyInterpreter.get("youtube_streamer");
  }

  private static YoutubeStreamerFactory _getInstance() {
    if (_instance == null) {
      _instance = new YoutubeStreamerFactory();
    }

    return _instance;
  }

  private static YoutubeStreamerFactory _instance =
    new YoutubeStreamerFactory();

  private PythonInterpreter _pyInterpreter;
  private PySystemState _pySystemState = Py.getSystemState();
  private PyObject _youtubeStreamerClass;

}