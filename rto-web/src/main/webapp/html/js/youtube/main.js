// TODO:
// (1) SF
// (2) Remove agrs from checkUrl, detectChange
// (3) _this
(function ($) {
  var FIELD_YOUTUBE_CONVERT = '#youTube-convert';
  var FIELD_YOUTUBE_PLAYER = 'youTube-player';
  var FIELD_YOUTUBE_SLIDER = '#youTube-slider';

  var FIELD_YOUTUBE_URL = '#youTube-url';
  var FIELD_YOUTUBE_URL_CHECK_INTERVAL = 250;
  var FIELD_YOUTUBE_URL_ERRMSG = '#youTube-url-errmsg';

  var YOUTUBE_API = '<script src="http://www.youtube.com/iframe_api" />';
  var YOUTUBE_METADATA_URL = "service/video/get"

  var YOUTUBE_URL_FIRST = 'http://www.youtube.com/watch?v=FnquYPxm3eo'
  var YOUTUBE_URL_REGEX = /http.+youtube\.com\/watch\?v\=(\w+)/;

  var YouTube = function() {
    var _this = this;

    _this.youTubeConvert = $(FIELD_YOUTUBE_CONVERT);

    _this.youTubeSlider = _this.createSlider(0, 1);

    _this.youTubeUrl = $(FIELD_YOUTUBE_URL);
    _this.youTubeUrl.val(YOUTUBE_URL_FIRST);

    _this.youTubeUrlErrMsg = $(FIELD_YOUTUBE_URL_ERRMSG);
    _this.youTubeUrlErrMsg.hide();

    _this.createYouTubeAPI(null);
  };

  YouTube.prototype = {
    constructor: YouTube,

    checkUrl: function(input) {
      var youTubeId = getYouTubeId(url);

      if (youTubeId) {
        this.getMetaData(youTubeId);
      }
      else {
        if (console && console.log) {
          console.log(this);
        }

        this.disableControls();
        this.youTubeUrlErrMsg.show();
      }
    },

    createSlider: function(minValue, maxValue) {
      var _this = this;

      var youTubeSlider = $(FIELD_YOUTUBE_SLIDER).slider({
        formater: _this.sliderFormatter,
        min: minValue,
        max: maxValue,
        step: 1,
        value: [minValue, maxValue]
      });

      return youTubeSlider.data('slider');
    },

    createYouTubeAPI: function() {
      var _this = this;

      var youTubeId = _this.getYouTubeId(YOUTUBE_URL_FIRST);

      var youTubeEvents = {
        onReady: _this.onYouTubePlayerReady,
        onStateChange: _this.onYouTubePlayerStateChange
      };

      window.onYouTubeIframeAPIReady = function() {
        _this.youTubePlayer = new YT.Player(FIELD_YOUTUBE_PLAYER, {
          height: '390',
          width: '640',
          videoId: youTubeId,
          events: youTubeEvents
        });

        if (console && console.log) {
          console.log('YouTube API has just got initialized.');
        }
      };

      var firstScriptTag = $('script').first();

      firstScriptTag.before(YOUTUBE_API);
    },

    detectChange: function(input, handler) {
      var old = input.attr('data-old-value');
      var current = input.val();

      if (old !== current) { 
        if (typeof old != 'undefined') { 
          handler.call(this, input);
        }

        input.attr('data-old-value', current);
      }
    },

    disableControls: function() {
      this.youTubeConvert.children().prop('disabled', true);
      this.youTubePlayer.clearVideo();
      this.youTubeSlider.disable();
    },

    enableControls: function() {
      this.youTubeConvert.children().prop('disabled', false);
      this.youTubeSlider.enable();
    },

    getMetaData: function(youTubeId) {
      var _this = this;

      var url = YOUTUBE_METADATA_URL + '/' + youTubeId;

      $.get(url)
        .done(function(result) {
          _this.setMetaData(result);

          _this.enableControls();
          _this.youTubeUrlErrMsg.hide();
        })
        .fail(function(result) {
          if (console && console.log) {
            console.log(result);
          }

          _this.disableControls();
          _this.youTubeUrlErrMsg.show();
        });
    },

    getYouTubeId: function(url) {
      var match = url.match(YOUTUBE_URL_REGEX);

      if (match && match[1]) {
        return match[1];
      }

      return null;
    },

    onYouTubePlayerReady: function() {
      var _this = this;

      if (console && console.log) {
        console.log('onYouTubePlayerReady');
      }

      _this.checkUrl(_this.youTubeUrl);

      setInterval(function() {
        _this.detectChange(_this.youTubeUrl, _this.checkUrl);
      }, FIELD_YOUTUBE_URL_CHECK_INTERVAL);
    },

    onYouTubePlayerStateChange: function() {
      if (console && console.log) {
        console.log('onYouTubePlayerStateChange');
      }
    },

    setMetaData: function(metaData) {
      var maxValue = (metaData && metaData.length) ? metaData.length : 0;

      if (this.youTubeSlider) {
        this.youTubeSlider.destroy();
      }

      this.youTubeSlider = this.createSlider(0, maxValue);
    },

    sliderFormatter: function(value) {
      var hours = Math.floor(value / 3600);
      var minutes = Math.floor((value % 3600) / 60);
      var seconds = value - (hours * 3600) - (60 * minutes);

      var sb = []

      sb.push('(', hours, ':', minutes, ':', seconds, ')');

      return sb.join('');
    }
  };

  new YouTube();
})(jQuery);