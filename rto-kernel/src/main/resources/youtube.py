import pafy

class youtube_metadata(object):
  """ Wrapper class for pafy """

  def __init__(self, url, stream_type = "audio"):
    video = pafy.new(url)

    self.author = video.author
    self.category = video.category
    self.length = video.length
    self.videoid = video.videoid
    self.published = video.published
    self.title = video.title

    self.streams = []

    streams = video.audiostreams if stream_type == "audio" else video.streams

    for stream in streams:
      self.streams.append(youtube_stream(stream))

class youtube_stream(object):
  """ Wrapper class for holding data of Youtube streams """

  def __init__(self, stream):
    self.stream_extension = stream.extension
    self.stream_mediatype = stream.mediatype
    self.stream_quality = stream.quality
    self.stream_resolution = stream.resolution
    self.stream_size = stream.size
    self.stream_url = stream.url