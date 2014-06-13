(function ($) {
  var FIELD_YOUTUBE_CONVERT = "#youtube-convert";
  var FIELD_YOUTUBE_SLIDER = "#youtube-slider";

  var FIELD_YOUTUBE_URL = "#youtube-url";
  var FIELD_YOUTUBE_URL_CHECK_INTERVAL = 250;
  var FIELD_YOUTUBE_URL_ERRMSG = "#youtube-url-errmsg";

  var YOUTUBE_URL_REGEX = /http.+youtube\.com\/watch\?v\=\w+/;

  var YouTube = function() {
    var _this = this;

    _this.youtubeConvert = $(FIELD_YOUTUBE_CONVERT);

    _this.youtubeSlider = $(FIELD_YOUTUBE_SLIDER).slider({
      formater: _this.sliderFormatter,
      min: 0,
      max: 3600,
      step: 5,
      value: [0, 10]
    });

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

      if (url.match(YOUTUBE_URL_REGEX)) {
        this.enableControls();
        this.youtubeUrlErrMsg.hide();
      }
      else {
        if (console && console.log) {
          console.log(this);
        }

        this.disableControls();
        this.youtubeUrlErrMsg.show();
      }
    },

    detectChange: function(input, handler) {
      var old = input.attr("data-old-value");
      var current = input.val();

      if (old !== current) { 
        if (typeof old != 'undefined') { 
          handler.call(this, input);
        }

        input.attr("data-old-value", current);
      }
    },

    disableControls: function() {
      this.youtubeConvert.children().prop("disabled", true);
      // this.youtubeSlider.disable();
    },

    enableControls: function() {
      this.youtubeConvert.children().prop("disabled", false);
      // this.youtubeSlider.enable();
    },

    sliderFormatter: function(value) {
      var hours = Math.floor(value / 3600);
      var minutes = Math.floor((value % 3600) / 60);
      var seconds = value - (hours * 3600) - (60 * minutes);

      var sb = []

      sb.push("(", hours, ":", minutes, ":", seconds, ")");

      return sb.join("");
    }
  };

  new YouTube();
})(window.jQuery);