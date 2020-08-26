var margin = {top: 10, right: 30, bottom: 30, left: 60};
var width = 460 - margin.left - margin.right;
var height = 400 - margin.top - margin.bottom;
const riskScores = [];

var webSocket  = new WebSocket("ws://"+location.hostname+":"+location.port+"/socket");
//    webSocket.onopen = function (event){webSocket.send("StartReading~"+usertoken)};
    webSocket.onmessage = function (event) {riskScores.push(JSON.parse(event.data)); console.log(JSON.stringify(riskScores))};//this array will be used for displaying history
    webSocket.onclose = function () {
        alert("Thank you for using STEDI, your graph session has ended");
        window.location.href = "/timer.html";
    };

var svg = d3.select("#risk-over-time")
    .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
    .append("g")
        .attr("transform",
              "translate("+margin.left+",", + margin.top +")");
