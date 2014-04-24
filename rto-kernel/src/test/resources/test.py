#!/usr/bin/env python -tt

import youtube

str = youtube.youtube_streamer("https://www.youtube.com/watch?v=8ptfyhBjXj8")

print str.getVideoAuthor()
print str.getVideoLength()
print str.getVideoRating()
print str.getVideoTitle()

for s in str.getVideoStreams():
    print s.getResolution()
