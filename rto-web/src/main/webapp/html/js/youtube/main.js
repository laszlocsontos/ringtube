var FIELD_YOUTUBE_URL = "#youtube-url";
var FIELD_YOUTUBE_URL_CHECK_INTERVAL = 250;
var FIELD_YOUTUBE_URL_ERRMSG = "#youtube-url-errmsg";

var FIELD_YOUTUBE_CONVERT = "#youtube-convert";
var FIELD_YOUTUBE_SLIDER = "#youtube-slider";

var YOUTUBE_URL_REGEX = /http.+youtube\.com\/watch\?v\=\w+/;

function checkUrl(url) {
  if (url.match(YOUTUBE_URL_REGEX)) {
    $(FIELD_YOUTUBE_URL_ERRMSG).hide();
    enableControls();
  }
  else {
    $(FIELD_YOUTUBE_URL_ERRMSG).show();
    disableControls();
  }
}

function disableControls() {
  $(FIELD_YOUTUBE_CONVERT).children().prop("disabled", true);
  $(FIELD_YOUTUBE_SLIDER).children().prop("disabled", true);
}

function enableControls() {
  $(FIELD_YOUTUBE_CONVERT).children().prop("disabled", false);
  $(FIELD_YOUTUBE_SLIDER).children().prop("disabled", false);
}

function formatter(value) {
  var hours = Math.floor(value / 3600);
  var minutes = Math.floor((value % 3600) / 60);
  var seconds = value - (hours * 3600) - (60 * minutes);

  var sb = []

  sb.push("(", hours, ":", minutes, ":", seconds, ")");

  return sb.join("");
}

$("#slider").slider({
  formater: formatter,
  min: 0,
  max: 3600,
  step: 5,
  value: [0, 10]
});

setInterval(function() { 
  var input = $(FIELD_YOUTUBE_URL);

  var old = input.attr("data-old-value");
  var current = input.val();

  if (old !== current) { 
    if (typeof old != 'undefined') { 
      checkUrl(current);
    }

    input.attr("data-old-value", current);
  }
}, FIELD_YOUTUBE_URL_CHECK_INTERVAL);

$(FIELD_YOUTUBE_URL_ERRMSG).hide();