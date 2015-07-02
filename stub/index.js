var express = require('express');
var app = express();

var fs = require("fs");

app.get('/api/user/johndoe/latest_episodes', function(req, res) {
  var file = fs.readFileSync(__dirname + '/latest_episodes.json', 'utf-8');
  var json = JSON.parse(file);

  var start = Date.now();

  setTimeout(function() {
    res.json(json)
  }, 5000);
});

var server = app.listen(3000, function () {

  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port);
});
