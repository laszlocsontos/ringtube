package net.thirdfoot.rto.kernel.media;

import com.xuggle.xuggler.IAudioResampler;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Juan González
 * @author Sergio González
 * @author Brian Wing Shun Chan
 * @author Alexander Chow
 *
 * Adopted for RTO
 * @author lcsontos
 */
public class AudioConverter extends Converter {

  public AudioConverter(
    String inputURL, String outputURL, ConversionContext conversionContext) {

    super(conversionContext);

    _inputURL = inputURL;
    _outputURL = outputURL;

    initAudioBitRate();
    initAudioSampleRate();
  }

  @Override
  public void convert() throws Exception {
    try {
      doConvert();
    }
    finally {
      if (_inputIContainer.isOpened()) {
        _inputIContainer.close();
      }

      if (_outputIContainer.isOpened()) {
        _outputIContainer.close();
      }
    }
  }

  protected void doConvert() throws Exception {

    // Prepare

    _inputIContainer = IContainer.make();
    _outputIContainer = IContainer.make();

    openContainer(_inputIContainer, _inputURL, false);
    openContainer(_outputIContainer, _outputURL, true);

    int inputStreamsCount = _inputIContainer.getNumStreams();

    if (inputStreamsCount < 0) {
      throw new RuntimeException("Input URL does not have any streams");
    }

    IAudioResampler[] iAudioResamplers =
      new IAudioResampler[inputStreamsCount];

    IAudioSamples[] inputIAudioSamples =
      new IAudioSamples[inputStreamsCount];
    IAudioSamples[] outputIAudioSamples =
      new IAudioSamples[inputStreamsCount];

    IStream[] outputIStreams = new IStream[inputStreamsCount];

    IStreamCoder[] inputIStreamCoders = new IStreamCoder[inputStreamsCount];
    IStreamCoder[] outputIStreamCoders = new IStreamCoder[inputStreamsCount];

    int firstAudioIndex = -1;

    for (int index = 0; index < inputStreamsCount; index++) {
      IStream inputIStream = _inputIContainer.getStream(index);

      IStreamCoder inputIStreamCoder = inputIStream.getStreamCoder();

      inputIStreamCoders[index] = inputIStreamCoder;

      ICodec.Type inputICodecType = inputIStreamCoder.getCodecType();

      if (inputICodecType == ICodec.Type.CODEC_TYPE_AUDIO) {
        prepareAudio(
          iAudioResamplers, inputIAudioSamples, outputIAudioSamples,
          inputIStreamCoder, outputIStreamCoders, _outputIContainer,
          outputIStreams, inputICodecType, _outputURL, index);

        if (firstAudioIndex < 0) {
          firstAudioIndex = index;
        }
      }

      openStreamCoder(inputIStreamCoders[index]);
      openStreamCoder(outputIStreamCoders[index]);
    }

    if (_outputIContainer.writeHeader() < 0) {
      throw new RuntimeException("Unable to write container header");
    }

    IPacket inputIPacket = IPacket.make();
    IPacket outputIPacket = IPacket.make();

    // Seek

    IStream firstAudioStream = _inputIContainer.getStream(firstAudioIndex);

    int denominator = firstAudioStream.getTimeBase().getDenominator();

    int startTimeStamp = getAttribute(ConversionAttribute.START_TIMESTAMP);
    boolean seekFailed = false;

    int seekResult = _inputIContainer.seekKeyFrame(
      firstAudioIndex, startTimeStamp * denominator, IContainer.SEEK_FLAG_ANY);

    if (seekResult < 0) {
      if (_log.isWarnEnabled()) {
        _log.warn("Unable to seek audio stream " + firstAudioStream);
      }

      seekFailed = true;
    }

    // Read

    _inputIContainer.readNextPacket(inputIPacket);

    // TODO Wouldn't IRational be a better alternative?
    double duration = 0;

    int endTimeStamp = getAttribute(ConversionAttribute.END_TIMESTAMP);

    int previousPacketSize = -1;

    while (_inputIContainer.readNextPacket(inputIPacket) == 0) {
      if (_log.isTraceEnabled()) {
        _log.trace("Current packet size " + inputIPacket.getDuration());
      }

      int streamIndex = inputIPacket.getStreamIndex();

      IStreamCoder inputIStreamCoder = inputIStreamCoders[streamIndex];
      IStreamCoder outputIStreamCoder = outputIStreamCoders[streamIndex];

      if (outputIStreamCoder == null) {
        continue;
      }

      if (inputIStreamCoder.getCodecType() ==
          ICodec.Type.CODEC_TYPE_AUDIO) {

        duration += ((double)inputIPacket.getDuration()) / denominator;

        if (seekFailed && duration < startTimeStamp) {
          continue;
        }

        if (duration > (endTimeStamp - startTimeStamp)) {
          break;
        }

        IStream iStream = _inputIContainer.getStream(streamIndex);

        long timeStampOffset = getStreamTimeStampOffset(iStream);

        decodeAudio(
          iAudioResamplers[streamIndex],
          inputIAudioSamples[streamIndex],
          outputIAudioSamples[streamIndex], inputIPacket,
          outputIPacket, inputIStreamCoder, outputIStreamCoder,
          _outputIContainer, inputIPacket.getSize(),
          previousPacketSize, streamIndex, timeStampOffset);
      }

      previousPacketSize = inputIPacket.getSize();
    }

    // Cleanup

    flush(outputIStreamCoders, _outputIContainer);

    if (_outputIContainer.writeTrailer() < 0) {
      throw new RuntimeException(
        "Unable to write trailer to output file");
    }

    cleanUp(iAudioResamplers, null);
    cleanUp(inputIAudioSamples, outputIAudioSamples);
    cleanUp(inputIStreamCoders, outputIStreamCoders);
    cleanUp(inputIPacket, outputIPacket);
  }

  @Override
  protected int getAudioBitRate(ICodec outputICodec, int originalBitRate) {
    return super.getAudioBitRate(outputICodec, _audioBitRate);
  }

  @Override
  protected int getAudioSampleRate() {
    return _audioSampleRate;
  }

  @Override
  protected IContainer getInputIContainer() {
    return _inputIContainer;
  }

  protected void initAudioBitRate() {
    _audioBitRate = getProperty(
      "converter.audio.bit.rate", AUDIO_BIT_RATE_DEFAULT, AUDIO_BIT_RATE_MAX);
  }

  protected void initAudioSampleRate() {
    _audioSampleRate = getProperty(
      "audio.sample.rate", AUDIO_SAMPLE_RATE_DEFAULT, AUDIO_SAMPLE_RATE_MAX);
  }

  private static Logger _log = LoggerFactory.getLogger(AudioConverter.class);

  private int _audioBitRate;
  private int _audioSampleRate;
  private IContainer _inputIContainer;
  private String _inputURL;
  private IContainer _outputIContainer;
  private String _outputURL;

}