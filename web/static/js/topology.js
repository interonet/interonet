function Topology(data) {
    var switchNum = data.switchNum, hostNum = data.hostNum, topology = data.topology,
        width = data.width, height = data.height;
    var instance = jsPlumb.getInstance({
        DragOptions: {cursor: 'pointer', zIndex: 2000},
        ConnectionOverlays: [],
        Container: "topology"
    });
    var connectorHoverStyle = {
            lineWidth: 4,
            strokeStyle: "#216477",
            outlineWidth: 2,
            outlineColor: "white"
        },
        endpointHoverStyle = {
            fillStyle: "#216477",
            strokeStyle: "#216477"
        },
        endPoint = {
            endpoint: "Dot",
            paintStyle: {
                strokeStyle: "#000000",  // endpoint color
                fillStyle: "transparent",
                radius: 4,
                lineWidth: 2
            },
            isSource: true,
            isTarget: true,
            connector: ["StateMachine", {curviness: 20}],
            connectorStyle: {
                strokeStyle: "#5c96bc",
                lineWidth: 2,
                outlineColor: "transparent",
                outlineWidth: 4
            },
            hoverPaintStyle: endpointHoverStyle,
            connectorHoverStyle: connectorHoverStyle,
            dragOptions: {}
        },
        firstEndpoint = {
            overlays: [
                ["Label", {
                    location: [0.5, 1.5],
                    label: "eth0",
                    cssClass: "endpointSourceLabel"
                }]
            ]
        },
        secondEndpoint = {
            overlays: [
                ["Label", {
                    location: [0.5, 1.5],
                    label: "eth1",
                    cssClass: "endpointSourceLabel"
                }]
            ]
        },
        thirdEndpoint = {
            overlays: [
                ["Label", {
                    location: [0.5, 1.5],
                    label: "eth2",
                    cssClass: "endpointSourceLabel"
                }]
            ]
        },
        fourthEndpoint = {
            overlays: [
                ["Label", {
                    location: [0.5, 1.5],
                    label: "eth3",
                    cssClass: "endpointSourceLabel"
                }]
            ]
        };

    var addSwitchEndpoints = function (toId, Anthors) {
        var Endpoints = [firstEndpoint, secondEndpoint, thirdEndpoint, fourthEndpoint];
        for (var i = 0; i < Anthors.length; i++) {
            var uuid = toId + ":" + i;
            instance.addEndpoint(toId, jQuery.extend({},endPoint,Endpoints[i]), {
                anchor: Anthors[i], uuid: uuid
            });
        }
    };
    var addVMEndpoints = function (toId, Anthors) {
        for (var j = 0; j < Anthors.length; j++) {
            var uuid = toId + ":" + j;
            instance.addEndpoint(toId, jQuery.extend({},endPoint,firstEndpoint), {
                anchor: Anthors[j], uuid: uuid
            });
        }
    };
    instance.batch(function () {
        for (var i = 0; i < switchNum; i++) {
            var Switch = $("<div class='window switch-block' id='s" + i + "' ><strong>s" + i + "</strong></div>");
            var space = (width - 60 * switchNum) / (switchNum + 1);
            var left = 60 * i + (i + 1) * space;
            Switch.css("left", left);
            $("#topology").append(Switch);
            var s = "s" + i;
            addSwitchEndpoints(s, ["TopCenter", "LeftMiddle", "BottomCenter", "RightMiddle"])
        }
        for (var j = 0; j < hostNum; j++) {
            var Host = $("<div class='window host-block' id='" + "h" + j + "'><strong>" + "v" + j + "</strong></div>");
            space = (width - 60 * hostNum) / (hostNum + 1);
            left = 60 * j + (j + 1) * space;
            Host.css("left",left);
            $("#topology").append(Host);
            var h = "h" + j;
            addVMEndpoints(h, ["TopCenter"])
        }
        instance.draggable(jsPlumb.getSelector(".topology .window"), {grid: [20, 20]});
        instance.bind("click", function (c) {instance.detach(c);});
        if(topology)
        {
            for  (  var k in  topology)
            {
                instance.connect({uuids: [k, topology[k]], editable: false});
            }
        }

    });

    return instance;

}