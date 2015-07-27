var express = require('express');
var app = express();

var fs = require("fs");

var fromFile = function(fileName) {
  return fs.readFileSync(__dirname + fileName, 'utf-8');
};

var jsonParse = function(file) {
  return JSON.parse(file);
};

var simulateHighLatency = function(response, json) {
  setTimeout(function() { response.json(json) }, 5000);
};

app.get('/api/user/johndoe/latest_episodes', function(req, res) {
  var json = jsonParse(fromFile('/latest_episodes.json'));

  simulateHighLatency(res, json);
});

app.get('/api/user/johndoe/feeds', function(req, res) {
  var json = jsonParse(fromFile('/user_feeds.json'));

  simulateHighLatency(res, json);
});

app.get('/api/feeds/:id', function(req, res) {
  var json = jsonParse(fromFile('/feed.json'));

  simulateHighLatency(res, json);
});

var server = app.listen(3000, function () {

  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port);
});
