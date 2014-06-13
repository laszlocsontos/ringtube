<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en"><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">
  <link rel="shortcut icon" href="http://getbootstrap.com/assets/ico/favicon.ico">

  <title>ringtube.org | All the ringtones you&apos;ve every wanted</title>

  <!-- Bootstrap core CSS -->
  <link href="html/css/bootstrap.min.css" rel="stylesheet">

  <!-- Slider CSS -->

  <link href="html/css/bootstrap-slider.min.css" rel="stylesheet">
  <style TYPE="text/css">
    .slider-selection {background: #BABABA;}
  </style>
  </head>

  <body>

  <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
      <span class="sr-only">Toggle navigation</span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">RingTube</a>
    </div>
    <div class="navbar-collapse collapse">
      <form class="navbar-form navbar-right" role="form">
      <div class="form-group">
        <input placeholder="Email" class="form-control" type="text">
      </div>
      <div class="form-group">
        <input placeholder="Password" class="form-control" type="password">
      </div>
      <button type="submit" class="btn btn-success">Sign in</button>
      </form>
    </div><!--/.navbar-collapse -->
    </div>
  </div>

  <!-- Main jumbotron for a primary marketing message or call to action -->
  <div class="jumbotron">
    <h2>Convert your favorite videos into ringtones easily for free of charge</h2>
    <p class="lead">Just enter the link, pick the desired time frame, the target file format and your&apos;re ready to go!</p>

    
    <div class="col-md-8 input-group lead">
      <span class="input-group-addon glyphicon glyphicon-music"></span>
      <input id="youtube-url" type="text" class="form-control" placeholder="YouTube URL">
    </div>

    <div id="youtube-url-errmsg" class="col-md-8 alert alert-danger">This isn't a YouTube URL</div>

    <div class="col-md-8 input-group lead">
      <span class="input-group-addon glyphicon glyphicon-resize-horizontal"></span>
      <input id="youtube-slider" type="text" class="form-control">
    </div>

    <div id="youtube-convert" class="btn-group">
      <button type="button" class="btn btn-lg btn-success">Convert</button>
      <button type="button" class="btn btn-lg btn-success dropdown-toggle" data-toggle="dropdown">
        <span class="caret"></span>
        <span class="sr-only">Toggle Dropdown</span>
      </button>
      <ul class="dropdown-menu" role="menu">
        <li><a href="#">MP3</a></li>
        <li><a href="#">AAC</a></li>
        <li><a href="#">OGG</a></li>
      </ul>
    </div>
  </div>

  <div class="container">
    <!-- Example row of columns -->
    <div class="row">
    <div class="col-md-4">
      <h2>Why register?</h2>
      <p>First of all, it doesn&apos;t cost a thing, basically there no reason why not to register.
      Furthermore, if you register you become a premium member. Premium membership has the following benefits.</p>
      <p><a class="btn btn-default" href="#" role="button">View details »</a></p>
    </div>
    <div class="col-md-4">
      <h2>Supported formats</h2>
      <p>You can choose from the following file formats.</p>
      <li>MP3</li>
      <li>AAC</li>
      <li>OGG</li>
      <p>Flac is coming soon...</p>
      <p><a class="btn btn-default" href="#" role="button">View details »</a></p>
     </div>
    <div class="col-md-4">
      <h2>Top Videos</h2>
      <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis 
in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. 
Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh,
 ut fermentum massa justo sit amet risus.</p>
      <p><a class="btn btn-default" href="#" role="button">View details »</a></p>
    </div>
    </div>

    <hr>

    <footer>
    <p>© ringtube.org 2014</p>
    </footer>
  </div> <!-- /container -->

  <!-- Placed at the end of the document so the pages load faster -->
  <script src="html/js/jquery/jquery.min.js"></script>
  <script src="html/js/bootstrap/bootstrap.min.js"></script>
  <script src="html/js/bootstrap-slider/bootstrap-slider.min.js"></script>
  <script src="html/js/youtube/main.js"></script>
</body>
</html>