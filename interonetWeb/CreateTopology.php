<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>CreateSlice</title>
    <link href="css/bootstrap.css" rel="stylesheet" media="screen"/>
    <link href="css/index.css" rel="stylesheet"/>
    <link href="css/CreateSlice.css" rel="stylesheet"/>
    <link rel="stylesheet" href="css/demo.css"/>
</head>
<body>
<script src="http://code.jquery.com/jquery.js"></script>
<script src="js/bootstrap.js"></script>


<div class="container-fluid">
    <div class="row-fluid">


            <div class="span7" id="main">
                <!-- demo -->
                <div class="demo flowchart-demo" id="flowchart-demo">


                    <?php
                    $SwitchNum = $_COOKIE["SwitchNum"];
                    $VMNum = $_COOKIE["VMNum"];
                    for ($i = 0; $i < $SwitchNum; $i++) {
                        echo "<div class='window switch'  id='s$i' ><strong>s$i</strong></div>";
                    }
                    for ($j = 0; $j < $VMNum; $j++) {
                        echo "<div class='window host' id='h$j'><strong>h$j</strong></div>";
                    }
                    ?>


                </div>
                <!-- /demo -->

            </div>
            <div class="span2">



            </div>
        <div class="span3">
            <div class="control-group">
                <div class="controls">
                    <br/>
                    <br/>
                    <button id="button" type="button" class="btn btn-success btn-large" contenteditable="true">
                       OK
                    </button>
                </div>
            </div>
        </div>


        </div>
    </div>
</div>
<script src="lib/jsBezier-0.6.js"></script>
<!-- event adapter -->
<script src="lib/mottle-0.5.js"></script>
<!-- geometry functions -->
<script src="lib/biltong-0.2.js"></script>
<!-- drag -->
<script src="lib/katavorio-0.6.js"></script>
<!-- jsplumb util -->
<script src="src/util.js"></script>
<script src="src/browser-util.js"></script>
<!-- base DOM adapter -->
<script src="src/dom-adapter.js"></script>
<!-- main jsplumb engine -->
<script src="src/jsPlumb.js"></script>
<script src="src/overlay-component.js"></script>
<!-- endpoint -->
<script src="src/endpoint.js"></script>
<!-- connection -->
<script src="src/connection.js"></script>
<!-- anchors -->
<script src="src/anchors.js"></script>
<!-- connectors, endpoint and overlays  -->
<script src="src/defaults.js"></script>
<!-- bezier connectors -->
<script src="src/connectors-bezier.js"></script>
<!-- state machine connectors -->
<script src="src/connectors-statemachine.js"></script>
<!-- flowchart connectors -->
<script src="src/connectors-flowchart.js"></script>
<script src="src/connector-editors.js"></script>
<!-- SVG renderer -->
<script src="src/renderers-svg.js"></script>


<!-- vml renderer -->
<script src="src/renderers-vml.js"></script>

<!-- no library jsPlumb adapter -->
<script src="src/base-library-adapter.js"></script>
<script src="src/dom.jsPlumb.js"></script>
<!-- /JS -->

<!--  demo code -->

<script type="text/javascript">
    jsPlumb.ready(function () {

        var instance = jsPlumb.getInstance({

            DragOptions: {cursor: 'pointer', zIndex: 2000},
            ConnectionOverlays: [],
            Container: "flowchart-demo"
        });

        var basicType = {
            connector: "StateMachine",
            paintStyle: {strokeStyle: "red", lineWidth: 4},
            hoverPaintStyle: {strokeStyle: "blue"},
            overlays: [
                "Arrow"
            ]
        };
        instance.registerConnectionType("basic", basicType);


        var connectorPaintStyle = {
                lineWidth: 2, strokeStyle: "#61B7CF", joinstyle: "round",
                outlineColor: "white",
                outlineWidth: 2
            },

            connectorHoverStyle = {
                lineWidth: 4,
                strokeStyle: "#216477",
                outlineWidth: 2,
                outlineColor: "white"
            },
            endpointHoverStyle = {
                fillStyle: "#216477",
                strokeStyle: "#216477"
            },

            firstEndpoint = {
                endpoint: "Dot",
                paintStyle: {
                    strokeStyle: "#000000",  // endpoint color
                    fillStyle: "transparent",
                    radius: 5,
                    lineWidth: 2
                },
                isSource: true,
                isTarget: true,
                connector: ["StateMachine", {curviness: 20}],
                connectorStyle: {strokeStyle: "#5c96bc", lineWidth: 2, outlineColor: "transparent", outlineWidth: 4},
                hoverPaintStyle: endpointHoverStyle,
                connectorHoverStyle: connectorHoverStyle,
                dragOptions: {},
                overlays: [
                    ["Label", {
                        location: [0.5, 1.5],
                        label: "eth0",
                        cssClass: "endpointSourceLabel"
                    }]
                ]
            },
            secondEndpoint = {
                endpoint: "Dot",
                paintStyle: {
                    strokeStyle: "#000000",  // endpoint color
                    fillStyle: "transparent",
                    radius: 5,
                    lineWidth: 2
                },
                isSource: true,
                isTarget: true,
                connector: ["StateMachine", {curviness: 20}],
                connectorStyle:{strokeStyle: "#5c96bc", lineWidth: 2, outlineColor: "transparent", outlineWidth: 4},
                hoverPaintStyle: endpointHoverStyle,
                connectorHoverStyle: connectorHoverStyle,
                dragOptions: {},
                overlays: [
                    ["Label", {
                        location: [0.5, 1.5],
                        label: "eth1",
                        cssClass: "endpointSourceLabel"
                    }]
                ]
            },
            thirdEndpoint = {
                endpoint: "Dot",
                paintStyle: {
                    strokeStyle: "#000000",  // endpoint color
                    fillStyle: "transparent",
                    radius: 5,
                    lineWidth: 2
                },
                isSource: true,
                isTarget: true,
                connector: ["StateMachine", {curviness: 20}],
                connectorStyle: {strokeStyle: "#5c96bc", lineWidth: 2, outlineColor: "transparent", outlineWidth: 4},
                hoverPaintStyle: endpointHoverStyle,
                connectorHoverStyle: connectorHoverStyle,
                dragOptions: {},
                overlays: [
                    ["Label", {
                        location: [0.5, 1.5],
                        label: "eth2",
                        cssClass: "endpointSourceLabel"
                    }]
                ]
            },
            fourthEndpoint = {
                endpoint: "Dot",
                paintStyle: {
                    strokeStyle: "#000000",  // endpoint color
                    fillStyle: "transparent",
                    radius: 5,
                    lineWidth: 2
                },
                isSource: true,
                isTarget: true,
                connector: ["StateMachine", {curviness: 20}],
                connectorStyle: {strokeStyle: "#5c96bc", lineWidth: 2, outlineColor: "transparent", outlineWidth: 4},
                hoverPaintStyle: endpointHoverStyle,
                connectorHoverStyle: connectorHoverStyle,
                dragOptions: {},
                overlays: [
                    ["Label", {
                        location: [0.5, 1.5],
                        label: "eth3",
                        cssClass: "endpointSourceLabel"
                    }]
                ]
            },

            init = function (connection) {
                connection.getOverlay("label").setLabel(connection.sourceId.substring(15) + "-" + connection.targetId.substring(15));
            };
        var addSwitchEndpoints = function (toId, Anthors) {
            var Endpoints = new Array(firstEndpoint, secondEndpoint, thirdEndpoint, fourthEndpoint);
            for (var i = 0; i < Anthors.length; i++) {
                var uuid = toId + ":" + i;
                instance.addEndpoint(toId, Endpoints[i], {
                    anchor: Anthors[i], uuid: uuid
                });
            }
        };
        var addVMEndpoints = function (toId, Anthors) {
            for (var j = 0; j < Anthors.length; j++) {
                var uuid = toId + ":" + j;
                instance.addEndpoint(toId, firstEndpoint, {
                    anchor: Anthors[j], uuid: uuid
                });
            }
        };
        var SwitchNum =<?php echo $SwitchNum ?>;
        var VMNum = <?php echo  $VMNum ?>;


        instance.batch(function () {
            for (var i = 0; i < SwitchNum; i++) {
                var Switch = "s" + i;
                addSwitchEndpoints(Switch, ["TopCenter", "LeftMiddle", "BottomCenter", "RightMiddle"])
            }
            for (var j = 0; j < VMNum; j++) {
                var VM = "h" + j;
                addVMEndpoints(VM, ["TopCenter"])
            }

            instance.bind("connection", function (connInfo, originalEvent) {
                init(connInfo.connection);
            });


            instance.draggable(jsPlumb.getSelector(".flowchart-demo .window"), {grid: [20, 20]});


            instance.bind("click", function (c) {
                instance.detach(c);
            });


            instance.bind("connectionDrag", function (connection) {
                console.log("connection " + connection.id + " is being dragged. suspendedElement is ", connection.suspendedElement, " of type ", connection.suspendedElementType);
            });

            instance.bind("connectionDragStop", function (connection) {
                console.log("connection " + connection.id + " was dragged");
            });

            instance.bind("connectionMoved", function (params) {
                console.log("connection " + params.connection.id + " was moved");
            });
        });

        jsPlumb.fire("jsPlumbDemoLoaded", instance);
        document.getElementById('button').onclick = function () {
            var topology = "{";
            var connect = instance.getAllConnections();
            for (var k = 0; k < connect.length; k++) {
                var line = connect[k].endpoints;
                var start,end;
                if(line[0].getUuid().substr(0,1) == "h")
                {
                    start = line[0].getUuid();
                    end = line[1].getUuid();
                }
               else
                {
                    start = line[1].getUuid();
                    end = line[0].getUuid();
                }

                topology = topology + "\"" + start + "\":" + "\"" + end + "\"";
                if (k < connect.length - 1)
                    topology = topology + ",";
                else topology = topology + "}";
            }
            document.cookie = "Topology" + " = " + topology + ";";
            window.close();


        };


    });
</script>
</body>
</html>