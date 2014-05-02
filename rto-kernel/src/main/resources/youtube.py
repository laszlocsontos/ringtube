from net.thirdfoot.rto.kernel.media import YoutubeStream
from net.thirdfoot.rto.kernel.media import YoutubeStreamer

import pafy

class youtube_streamer(YoutubeStreamer):
  """ Wrapper class for pafy """

  def __init__(self, url):
    video = pafy.new(url)

    self.video_author = video.author
    self.video_length = video.length
    self.video_rating = video.rating
    self.video_title = video.title

    self.all_streams = []

    for s in video.allstreams:
      ys = youtube_stream(
        s.extension, s.mediatype, s.quality, s.resolution,
        s.get_filesize(), s.url);
      self.all_streams.append(ys)

  def getAllStreams(self):
    return self.all_streams

  def getVideoAuthor(self):
    return self.video_author

  def getVideoLength(self):
    return self.video_length

  def getVideoRating(self):
    return self.video_rating

  def getVideoTitle(self):
    return self.video_title

class youtube_stream(YoutubeStream):
  """ Wrapper class for holding data of Youtube streams """

  def __init__(self, extension, mediatype, quality, resolution, size, url):
    self.stream_extension = extension
    self.stream_mediatype = mediatype
    self.stream_quality = quality
    self.stream_resolution = resolution
    self.stream_size = size
    self.stream_url = url

  def getExtension(self):
    return self.stream_extension

  def getMediaType(self):
    return self.stream_mediatype

  def getResolution(self):
    return self.stream_resolution

  def getQuality(self):
    return self.stream_quality

  def getSize(self):
    return self.stream_size

  def getUrl(self):
    return self.stream_url