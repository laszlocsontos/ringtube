var YOUTUBE_URL_REGEX = /http.+youtube\.com\/watch\?v\=\w+/;

function checkUrl(url) {
  if (url.match(YOUTUBE_URL_REGEX)) {
    $("#youtube-url-errmsg").hide();
    enableControls();
  }
  else {
    $("#youtube-url-errmsg").show();
    disableControls();
  }
}

function disableControls() {
  $("#youtube-convert").children().prop("disabled", true);
  $("#youtube-slider").children().prop("disabled", true);
}

function enableControls() {
  $("#youtube-convert").children().prop("disabled", false);
  $("#youtube-slider").children().prop("disabled", false);
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

$("#youtube-url").on("change, keydown, focusout", function() {
  var url = $(this).val();

  checkUrl(url);
});

$("#youtube-url-errmsg").hide();