
$(function(){

    $.fn.tabs=function(control){
        var element=$(this);
        control=$(control);
        element.on('click','li, button',function(){
            var tabName=$(this).attr("data-id");
            element.trigger("change.tabs",tabName);
        });
        element.bind("change.tabs",function(e,tabName){
            element.find("li").removeClass("active");
            element.find(">[data-id='"+ tabName +"']").addClass("active");
        });
        element.bind("change.tabs",function(e,tabName){
            control.find(">[data-id]").css('display','none');
            control.find(">[data-id='"+ tabName +"']").css('display','block');
        });
        var firstName=element.find(":first").attr("data-id");
        element.trigger("change.tabs",firstName);
        return this;
    };
    $('#changeTab').tabs('#contentTab');
    $('#tabSwitch').tabs('#contentSwitch');
    $('#tabHost').tabs('#contentHost');

});
function updateState(rfb, state, oldstate, msg) {
            var level;
            switch (state) {
                case 'failed':
                    level = "error";
                    break;
                case 'fatal':
                    level = "error";
                    break;
                case 'normal':
                    level = "normal";
                    break;
                case 'disconnected':
                    level = "normal";
                    break;
                case 'loaded':
                    level = "normal";
                    break;
                default:
                    level = "warn";
                    break;
            }
            console.log(level);
            if (msg) {
                console.log(msg);
            }
        }
function connected() {
                console.log('connected');
            }
function disconnected() {
              console.log('disconnected')
            }