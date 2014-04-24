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
        self.extension = extension
        self.resolution = resolution
        self.url = url

    def getExtension(self):
        return self.extension

    def getResolution(self):
        return self.resolution

    def getUrl(self):
        return self.url