var margin = {top: 10, right: 30, bottom: 30, left: 60};
var width = 460 - margin.left - margin.right;
var height = 400 - margin.top - margin.bottom;
const riskScores = [];

//var webSocket  = new WebSocket("ws://"+location.hostname+":"+location.port+"/socket");
//    webSocket.onmessage = event => riskScores.push(JSON.parse(event.data));//this array will be used for displaying history
//    webSocket.onclose =  () => {
//        alert("Thank you for using STEDI, your graph session has ended");
//        window.location.href = "/timer.html";
//    };

var ctx = document.getElementById('risk-over-time').getContext('2d');

var myChart= new Chart(ctx, {
    type: 'line',
    data: {
        datasets: [{
            label: 'Person A',
            data: [{
                x: 1,
                y: 30,
                id: 'PersonATest1'
            },
            {
                x: 2,
                y: 35,
                id: 'PersonATest2'
            },
            ]
        },
        {
            label: 'Person B',
            data: [{
                x: 1,
                y: 37,
                id: 'PersonBTest1'
            },
            {
                x: 4,
                y: 34,
                id: 'PersonBTest2'
            },
            ]
        }
        ]
    }

});