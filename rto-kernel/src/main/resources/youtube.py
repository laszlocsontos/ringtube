from net.thirdfoot.rto.kernel.youtube import YoutubeStream
from net.thirdfoot.rto.kernel.youtube import YoutubeStreamer

import pafy

class youtube_streamer(YoutubeStreamer):
    """ Wrapper class for pafy """

    def __init__(self, url):
        video = pafy.new(url)

        self.video_author = video.author
        self.video_length = video.length
        self.video_rating = video.rating
        self.video_title = video.title

        self.video_streams = []

        for s in video.streams:
            ys = youtube_stream(s.extension, s.resolution, s.url)
            self.video_streams.append(ys)

    def getVideoAuthor(self):
        return self.video_author

    def getVideoLength(self):
        return self.video_length

    def getVideoRating(self):
        return self.video_rating

    def getVideoStreams(self):
        return self.video_streams
        
    def getVideoTitle(self):
        return self.video_title

class youtube_stream(YoutubeStream):
    """ Wrapper class for holding data of Youtube streams """

    def __init__(self, extension, resolution, url):
        self.stream_extension = extension
        self.stream_resolution = resolution
        self.stream_url = url

    def getExtension(self):
        return self.stream_extension

    def getResolution(self):
        return self.stream_resolution

    def getUrl(self):
        return self.stream_url