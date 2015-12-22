$(document).ready(function() {
    $("#user").ready(function(){
        $.post("php/Cookie.php",
            {type:"Get"},
            function(data)
            {
                $("#user").text(data);
            });

    });

    $("#Logout").click(function(){
            $.post("php/Cookie.php",
                {type:"Del"});
        }
    );

    menuYloc();
    WaitingSlice();
    RunningSlice();
});
function menuYloc() {
    var name = "#floatMenu";
    var menuYloc = null;
    menuYloc = parseInt($(name).css("top").substring(0, $(name).css("top").indexOf("px")));
    $(window).scroll(function () {
        var offset = menuYloc + $(document).scrollTop() + "px";
        $(name).animate({top: offset}, {duration: 500, queue: false});
    });
}
function WaitingSlice()
{
    $.get("php/CheckWaitingSlice.php",
        function (data){
            $("#CheckWaitingTbody").html(data);

            $(".btnCheck").click(function(){
                var value=$(this).attr("name");
                $.post("php/deleteOrder.php",
                    {
                        OrderID:value
                    },function(data){
                        if(data == "Success")
                        {
                            alert("Delete success");
                            $("#"+value).remove();
                        }
                        else
                        {
                            alert("Delete failure");
                        }
                    });
            });
        });
}

function  RunningSlice()
{
    $.get("php/CheckRunningSlice.php",
        function(data)
        {
            $("#CheckRunningTbody").html(data);
            $('[data-toggle="tooltip"]').tooltip();
            $(".btnTopology").click(function(){
                $("#myModal").css("display","block");
                var value=$(this).attr("name");
                ShowTopology(value);

            });
        });
}
function ShowTopology(value)
{
    $.post("php/ShowTopology.php",
        {
            SliceID:value
        },function(data){
            $("#flowchart-demo").empty();
            var content =   eval('(' + data + ')');
            var topology = content.Topology;
            // alert(topology);
            var SwitchNum = content.SwitchNum;
            var VMNum = content.VMNum;
            for (var i = 0; i < SwitchNum; i++) {
                var Switch = "<div class='window switch' id='s" + i + "' ><strong>s" + i + "</strong></div>";
                $("#flowchart-demo").append(Switch);
                var space = (800-60*SwitchNum)/(SwitchNum+1);
                var sID = "#s"+i ;
                var left = 60*i+(i+1)*space;
                $(sID).css("left", left );
            }
            for (var j = 0; j < VMNum; j++) {
                var host = "<div class='window host' id='" + "h" + j + "'><strong>" + "v" + j + "</strong></div>";
                $("#flowchart-demo").append(host);
                space = (800-60*VMNum)/(VMNum+1);
                var hID = "#h"+j ;
                left = 60*j+(j+1)*space;
                $(hID).css("left", left );
            }
            jsPlumb.ready(function () {

                var instance = jsPlumb.getInstance({
                    DragOptions: {cursor: 'pointer', zIndex: 2000},
                    ConnectionOverlays: [],
                    Container: "flowchart-demo"
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
                    firstEndpoint = {
                        endpoint: "Dot",
                        paintStyle: {
                            strokeStyle: "#000000",  // endpoint color
                            fillStyle: "transparent",
                            radius: 4,
                            lineWidth: 2
                        },

                        connector: ["StateMachine", {curviness: 20}],
                        connectorStyle: {
                            strokeStyle: "#5c96bc",
                            lineWidth: 2,
                            outlineColor: "transparent",
                            outlineWidth: 4
                        },
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
                            radius: 4,
                            lineWidth: 2
                        },

                        connector: ["StateMachine", {curviness: 20}],
                        connectorStyle: {
                            strokeStyle: "#5c96bc",
                            lineWidth: 2,
                            outlineColor: "transparent",
                            outlineWidth: 4
                        },
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
                            radius: 4,
                            lineWidth: 2
                        },

                        connector: ["StateMachine", {curviness: 20}],
                        connectorStyle: {
                            strokeStyle: "#5c96bc",
                            lineWidth: 2,
                            outlineColor: "transparent",
                            outlineWidth: 4
                        },
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
                            radius: 4,
                            lineWidth: 2
                        },

                        connector: ["StateMachine", {curviness: 20}],
                        connectorStyle: {
                            strokeStyle: "#5c96bc",
                            lineWidth: 2,
                            outlineColor: "transparent",
                            outlineWidth: 4
                        },
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
                    var Endpoints = [firstEndpoint, secondEndpoint, thirdEndpoint, fourthEndpoint];
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


                instance.batch(function () {
                    for (var i = 0; i < SwitchNum; i++) {
                        var Switch = "s" + i;
                        addSwitchEndpoints(Switch, ["TopCenter", "LeftMiddle", "BottomCenter", "RightMiddle"])
                    }
                    for (var j = 0; j < VMNum; j++) {
                        var VM = "h" + j;
                        addVMEndpoints(VM, ["TopCenter"])
                    }
                    instance.draggable(jsPlumb.getSelector(".flowchart-demo .window"), {grid: [20, 20]});
                    instance.bind("connectionDrag", function (connection) {
                        console.log("connection " + connection.id + " is being dragged. suspendedElement is ", connection.suspendedElement, " of type ", connection.suspendedElementType);
                    });

                    instance.bind("connectionDragStop", function (connection) {
                        console.log("connection " + connection.id + " was dragged");
                    });

                    instance.bind("connectionMoved", function (params) {
                        console.log("connection " + params.connection.id + " was moved");
                    });
                    for  (  var strObjIndex in  topology)
                    {
                        instance.connect({uuids: [strObjIndex, topology[strObjIndex]], editable: false});
                    }
                });


            });
            $('#myModal').modal('toggle');
        });
}