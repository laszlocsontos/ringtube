(function ($) {
  var FIELD_YOUTUBE_CONVERT = '#youtube-convert';
  var FIELD_YOUTUBE_SLIDER = '#youtube-slider';

  var FIELD_YOUTUBE_URL = '#youtube-url';
  var FIELD_YOUTUBE_URL_CHECK_INTERVAL = 250;
  var FIELD_YOUTUBE_URL_ERRMSG = '#youtube-url-errmsg';

  var YOUTUBE_METADATA_URL = "service/video/get"
  var YOUTUBE_URL_REGEX = /http.+youtube\.com\/watch\?v\=(\w+)/;

  var YouTube = function() {
    var _this = this;

    _this.youtubeConvert = $(FIELD_YOUTUBE_CONVERT);

    _this.youtubeSlider = _this.createSlider(0, 1);

    _this.youtubeUrl = $(FIELD_YOUTUBE_URL);

    _this.youtubeUrlErrMsg = $(FIELD_YOUTUBE_URL_ERRMSG);
    _this.youtubeUrlErrMsg.hide();

    setInterval(function() {
      _this.detectChange(_this.youtubeUrl, _this.checkUrl);
    }, FIELD_YOUTUBE_URL_CHECK_INTERVAL);
  };

  YouTube.prototype = {
    constructor: YouTube,

    checkUrl: function(input) {
      var url = input.val();

      var match = url.match(YOUTUBE_URL_REGEX);

      if (match && match[1]) {
        var youtubeId = match[1];

        this.getMetaData(youtubeId);
      }
      else {
        if (console && console.log) {
          console.log(this);
        }

        this.disableControls();
        this.youtubeUrlErrMsg.show();
      }
    },

    createSlider: function(minValue, maxValue) {
      var _this = this;

      var youtubeSlider = $(FIELD_YOUTUBE_SLIDER).slider({
        formater: _this.sliderFormatter,
        min: minValue,
        max: maxValue,
        step: 1,
        value: [minValue, maxValue]
      });

      return youtubeSlider.data('slider');
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
      this.youtubeConvert.children().prop('disabled', true);
      this.youtubeSlider.disable();
    },

    enableControls: function() {
      this.youtubeConvert.children().prop('disabled', false);
      this.youtubeSlider.enable();
    },

    getMetaData: function(youtubeId) {
      var _this = this;

      var url = YOUTUBE_METADATA_URL + '/' + youtubeId;

      $.get(url)
        .done(function(result) {
          _this.setMetaData(result);

          _this.enableControls();
          _this.youtubeUrlErrMsg.hide();
        })
        .fail(function(result) {
          if (console && console.log) {
            console.log(result);
          }

          _this.disableControls();
          _this.youtubeUrlErrMsg.show();
        });
    },

    setMetaData: function(metaData) {
      var maxValue = (metaData && metaData.length) ? metaData.length : 0;

      if (this.youtubeSlider) {
        this.youtubeSlider.destroy();
      }

      this.youtubeSlider = this.createSlider(0, maxValue);
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