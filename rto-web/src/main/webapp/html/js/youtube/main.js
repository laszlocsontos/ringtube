// TODO:
// (1) SF
// (2) Remove agrs from checkUrl, detectChange
// (3) _this
(function ($) {
  var FIELD_YOUTUBE_CONVERT = '#youTube-convert';
  var FIELD_YOUTUBE_PLAYER = 'youTube-player';
  var FIELD_YOUTUBE_SLIDER = '#youTube-slider';
  var FIELD_YOUTUBE_TITLE = '#youTube-title';

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
    _this.youTubeConvertForm = $('youTube-convert-fm');

    _this.youTubeConvertButton = $('youTube-convert-btn');

    _this.youTubeConvertButton.on('click', function(event) {
      _this.youTubeConvertForm.submit();
    });

    _this.youTubeSlider = _this.createSlider(0, 1, false);

    _this.youTubeTitle = $(FIELD_YOUTUBE_TITLE);

    _this.youTubeUrl = $(FIELD_YOUTUBE_URL);
    _this.youTubeUrl.val(YOUTUBE_URL_FIRST);

    _this.youTubeUrlErrMsg = $(FIELD_YOUTUBE_URL_ERRMSG);
    _this.youTubeUrlErrMsg.hide();

    _this.createYouTubeAPI(null);
  };

  YouTube.prototype = {
    constructor: YouTube,

    checkUrl: function(url) {
      var youTubeId = this.getYouTubeId(url);

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

    createSlider: function(minValue, maxValue, showTooltip) {
      var _this = this;

      var youTubeSliderElement = $(FIELD_YOUTUBE_SLIDER).slider({
        formater: _this.sliderFormatter,
        min: minValue,
        max: maxValue,
        step: 1,
        tooltip: (showTooltip ? 'always' : 'hide'),
        tooltip_split: true,
        value: [minValue, maxValue]
      })

      var youTubeSlider = youTubeSliderElement.data('slider');

      youTubeSliderElement.on('slideStop', function(event) {
        var old = youTubeSliderElement.attr('data-old-value');
        var value = youTubeSlider.getValue();

        if (Array.isArray(value)) {
          if (value.length > 1) {
            value = Math.min(value[0], value[1]);
          }
          else {
            value = value[0];
          }
        }

        if (old != value) { 
          if (typeof old != 'undefined') { 
            _this.onSliderChange.call(_this, value);
          }

          youTubeSliderElement.attr('data-old-value', value);
        }

      });

      return youTubeSlider;
    },

    createYouTubeAPI: function() {
      var _this = this;

      var youTubeId = _this.getYouTubeId(YOUTUBE_URL_FIRST);

      var youTubeEvents = {
        onReady: $.proxy(_this.onYouTubePlayerReady, _this),
        onStateChange: $.proxy(_this.onYouTubePlayerStateChange, _this)
      };

      window.onYouTubeIframeAPIReady = function() {
        _this.youTubePlayer = new YT.Player(FIELD_YOUTUBE_PLAYER, {
          /*height: '390',
          width: '640',*/
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
          handler.call(this, current);
        }

        input.attr('data-old-value', current);
      }
    },

    disableControls: function() {
      this.youTubeConvert.children().prop('disabled', true);
      this.youTubeSlider.disable();

      $('#' + FIELD_YOUTUBE_PLAYER).hide();
    },

    enableControls: function() {
      this.youTubeConvert.children().prop('disabled', false);
      this.youTubeSlider.enable();

      $('#' + FIELD_YOUTUBE_PLAYER).show();
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

    onConvert: function() {
      
    },

    onSliderChange: function(value) {
      if (console && console.log) {
        console.log(value);
      }

      this.youTubePlayer.seekTo(value, true);
      this.youTubePlayer.playVideo();
    },

    onYouTubePlayerReady: function() {
      var _this = this;

      if (console && console.log) {
        console.log('onYouTubePlayerReady');
      }

      _this.checkUrl(_this.youTubeUrl.val());

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
      var length = (metaData && metaData.length) ? metaData.length : 0;
      var title = (metaData && metaData.title) ? metaData.title : '&nbsp;'

      if (this.youTubeSlider) {
        this.youTubeSlider.destroy();
      }

      this.youTubeSlider = this.createSlider(0, length, true);

      this.youTubeTitle.text(title)

      this.youTubePlayer.loadVideoById({
        videoId: metaData.youTubeId,
        startSeconds: 0,
        endSeconds: length,
        suggestedQuality: 'small'
      });
    },

    sliderFormatter: function(value) {
      var hours = Math.floor(value / 3600);
      var minutes = Math.floor((value % 3600) / 60);
      var seconds = value - (hours * 3600) - (60 * minutes);

      seconds = ("00" + seconds).slice(-2);

      var sb = []

      if (hours > 0) {
        minutes = ("00" + minutes).slice(-2);

        sb.push('(', hours, ':', minutes, ':', seconds, ')');
      }
      else {
        sb.push('(', minutes, ':', seconds, ')');
      }

      return sb.join('');
    }
  };

  new YouTube();
})(jQuery);