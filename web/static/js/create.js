$(document).ready(function(){
    var state = 1, switchNum = 0, hostNum = 0,switchTemp = 0, hostTemp = 0;
    var instance;
    var myControl=  {
        create: function(tp_inst, obj, unit, val, min, max, step){
            $('<input class="ui-timepicker-input" value="'+val+'" style="width:50%">')
                .appendTo(obj)
                .spinner({
                    min: min,
                    max: max,
                    step: step,
                    change: function(e,ui){ // key events
                        // don't call if api was used and not key press
                        if(e.originalEvent !== undefined)
                            tp_inst._onTimeChange();
                        tp_inst._onSelectHandler();
                    },
                    spin: function(e,ui){ // spin events
                        tp_inst.control.value(tp_inst, obj, unit, ui.value);
                        tp_inst._onTimeChange();
                        tp_inst._onSelectHandler();
                    }
                });
            return obj;
        },
        options: function(tp_inst, obj, unit, opts, val){
            if(typeof(opts) == 'string' && val !== undefined)
                return obj.find('.ui-timepicker-input').spinner(opts, val);
            return obj.find('.ui-timepicker-input').spinner(opts);
        },
        value: function(tp_inst, obj, unit, val){
            if(val !== undefined)
                return obj.find('.ui-timepicker-input').spinner('value', val);
            return obj.find('.ui-timepicker-input').spinner('value');
        }
    };

    // set the time
    $('#startTime').datetimepicker({
        dateFormat:'yy-mm-dd',
        timeFormat:'HH:mm:ss z',
        controlType: myControl
    });
    $('#endTime').datetimepicker({
        dateFormat:'yy-mm-dd',
        timeFormat:'HH:mm:ss z',
        controlType: myControl
    });

    //init state
    $("#progress1").addClass("active");
    $("#content1").show();

    // check the use condition
    $("#occupation").on("click",function(){
        $("#model-occupation").show();
    });
    $("#occupation-close").on("click",function(){
        $("#model-occupation").hide();
    });


    $(".state-btn").on("click",function(){
        changeState(this.id);
    });
    var switchInfo = $("#switchInformation");
    var hostInfo = $("#hostInformation");

    $("#divSwitch").on("click",".switch",function(){
        if(!(switchInfo .hasClass("show")))
        {
            if($(this).hasClass("add"))
            {
                switchTemp = switchTemp + 1;
                var addDiv = '<div class="switch-add" id="switchAdd'+switchTemp+'"><div class="switch-type"><p>Switch Type</p><div class="switch-select ons active"></div><div class="switch-select ovs"></div> </div> <div class="add-btn"> <button type="button" class="delete" id="switchDel'+switchTemp+'">Delete</button> <button type="button" class="OK" id="switchOK'+switchTemp+'">OK</button></div><br/></div>';
                switchInfo.append(addDiv);
            }
            else
            {
                // switch + num
                $("#switchAdd"+this.id.slice(6)).show();
            }
            switchInfo .addClass("show");
        }
    })
        .on("click", ".delete",function() {
            // switchDel + num
            $("#switchAdd" + this.id.slice(9)).remove();
            $("#switch" + this.id.slice(9)).remove();
            switchInfo.removeClass("show");
        })
        .on("click",".OK",function() {
            //switchOK + num
            if($("#switch"+this.id.slice(8)).length == 0)
            {
                var addSwitch = '<div class="switch ons" data-protocol="OF1.3" id="switch'+switchTemp+'"></div>';
                $("#switchAddBtn").before(addSwitch);
                $("#switchAdd"+switchTemp).hide();
            }
            else
            {
                $("#switchAdd"+this.id.slice(8)).hide();
            }

            switchInfo.removeClass("show");

        });
    $("#divHost").on("click",".host",function(){
        if(!(hostInfo.hasClass("show")))
        {
            if($(this).hasClass("add"))
            {
                hostTemp = hostTemp + 1;
                var addDiv = ' <div class="host-add" id="hostAdd'+hostTemp+'"><div class="host-type"> <p>Host Type</p><div class="host-select ubuntu active"></div> <div class="host-select windows"></div> </div> <div class="add-btn"> <button type="button" class="delete" id="hostDel'+hostTemp+'">Delete</button> <button type="button" class="OK" id="hostOK'+hostTemp+'">OK</button> </div> <br/></div>';
                hostInfo .append(addDiv);
            }
            else
            {
                // host + num
                $("#hostAdd"+this.id.slice(4)).show();
            }
            hostInfo.addClass("show");
        }
    }).
        on("click", ".delete",function() {
            //hostDel + num
            $("#hostAdd" + this.id.slice(7)).remove();
            $("#host" + this.id.slice(7)).remove();
            hostInfo.removeClass("show");
        })
        .on("click",".OK",function() {
            //hostOK + num
            if($("#host"+this.id.slice(6)).length == 0)
            {
                var addHost= '<div class="host ubuntu" id="host'+hostTemp+'"></div>';
                $("#hostAddBtn").before(addHost);
                $("#hostAdd"+hostTemp).hide();
            }
            else
            {
                $("#hostAdd"+this.id.slice(6)).hide();
            }
            hostInfo.removeClass("show");

        });
    function changeState(data)
    {
        var now = data.slice(-1);
        state = now;
        $("#progress").children().removeClass("active");
        for(var i = 1; i <= state; i++)
        {
            $("#progress"+i).addClass("active");
        }
        $('.content').hide();
        $("#content"+ now).show();
        if(state == 4)
        {
           var s = $(".switch"), h = $(".host");
           if((switchNum != (s.length -1 )) || (hostNum != (h.length) - 1 ))
           {
               switchNum = s.length - 1 ;
               hostNum = h.length - 1;
               var divTopology = $("#divTopology");
               var topologyContainer = $("#topology");
               topologyContainer.empty();
               instance = Topology( {"switchNum":switchNum, "hostNum":hostNum, "width":divTopology.width(), "height":divTopology.height()});
           }
        }
        if(state == 6)
        {
            $("#startTimeShow").html($("#startTime").val());
            $("#endTimeShow").html($("#endTime").val());
            $("#switchNumShow").html(switchNum);
            $("#hostNumShow").html(hostNum);
            $("#IPShow").html($("#IP").val());
            $("#PortShow").html($("#Port").val());
            $("#switchNum").val(switchNum);
            $("#hostNum").val(hostNum);
            var switchSet  = $("#divSwitch").find("div.switch").not(".add");
            //var hostSet = $("#divHost").find("div.host").not(".add");
            var switchConfObj = {}, topologyObj={};
            for(var k = 0; k < switchSet.length; k++)
            {
                switchConfObj["s"+k] = $(switchSet[k]).attr("data-protocol");
            }
            $("#switchConf").val(JSON.stringify(switchConfObj));

            var connect = instance.getAllConnections();
            for (k = 0; k < connect.length; k++) {
                var line = connect[k].endpoints;
                var start, end;
                if (line[0].getUuid().substr(0, 1) == "h") {
                    start = line[0].getUuid();
                    end = line[1].getUuid();
                }
                else {
                    start = line[1].getUuid();
                    end = line[0].getUuid();
                }
                topologyObj[start] = end;
            }
            $("#topologyStr").val(JSON.stringify(topologyObj));
        }

    }
});

