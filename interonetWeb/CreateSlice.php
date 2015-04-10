
<?php
include(dirname(__FILE__) . "/jsonrpcphp/jsonRPCClient.php");

$client = new jsonRPCClient("http://202.117.15.80:8080/abc");
$authToken = $_COOKIE["authToken"];
$response = $client->__call("getSwitchesUsageStatus",array($authToken));
$response2 = $client->__call("getVMsUsageStatus",array($authToken));
setcookie("SwitchesUsageStatus",$response);
setcookie("VMsUsageStatus",$response2);
?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <title>CreateSlice</title>
    <link href="css/reset.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="css/styles.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="css/print.css" rel="stylesheet" type="text/css" media="print" />
    <link href="css/bootstrap.css" rel="stylesheet" media="screen"/>
    <script src="js/jquery-1.7.1.js" type="text/javascript"></script>
    <script src="js/custom-menu.js" type="text/javascript"></script>
    <script src="js/jquery.ui.core.js" type="text/javascript"></script>
    <script src="js/jquery.ui.widget.js" type="text/javascript"></script>
    <script src="js/jquery.ui.accordion.js" type="text/javascript"></script>
    <script type="text/javascript">
        jQuery(function() {
            jQuery( "#accordion" ).accordion({
                autoHeight: false,
                navigation: true
            });
        });
    </script>
    <script src="js/mailmask.js" type="text/javascript"></script>
    <script src="js/jquery.organicTabs.js" type="text/javascript"></script><script src="js/bootstrap.js"></script>
    <script type="text/javascript">
        $(function() {

            $("#tab-one").organicTabs();

        });
    </script>

    <script src="js/deleteCookie.js" type="text/javascript"></script>
</head>
<body>
<div id="wrapper">
    <!-- start header -->
    <div id="header">
        <div class="header-content">
            <div class="topnavbar clearfix">
                <span class="alignright">
                    <a class="padding-left" href="index.html" title="Logout" onclick="delCookie('authToken')">Logout</a>

                </span>
            </div>
            <ul class="topnav alignright">
                <li ><a href="index2.html" title="Home">Home</a>
                </li>
                <li class="clearfix"><a href="#" onclick="return false" title="CreateSlice" class="cursor">Create</a>
                    <div class="subnav subnav1">
                        <div class="clearfix" id="services">
                            <dl class="alignleft">
                                <dd><a href="CreateSlice.php" title="CreateSlice">CreateSlice</a></dd>
                            </dl>
                        </div>
                    </div>
                </li>
                <li><a onclick="return false" title="CheckSlice">Check</a>

                    <div class="subnav subnav2">
                        <div class="clearfix" id="portfolio">
                            <dl class="alignleft">
                                <dd><a href="CheckRunningSlice.php" title="RunningSlice">Running Slice</a></dd>
                                <dd><a href="CheckWaitingSlice.php" title="WaitingSlice">Waiting Slice</a></dd>
                            </dl>
                        </div>
                    </div>
                </li>
                <li><a onclick="return false" title="UpdateSlice">Update</a>

                    <div class="subnav subnav3">
                        <div class="clearfix" id="blog">
                            <dl class="alignleft">
                                <dd><a href="UpdateRunningSlice.php" title="RunningSlice">Running Slice</a></dd>
                            </dl>
                        </div>
                    </div>
                </li>
                <li><a onclick="return false" title="StopSlice">Stop</a>

                    <div class="subnav subnav3">
                        <div class="clearfix" id="company">
                            <dl class="alignleft">
                                <dd><a href="StopRunningSlice.php" title="RunningSlice">Running Slice</a></dd>
                                <dd><a href="StopWaitingSlice.php" title="WaitingSlice">Waiting Slice</a></dd>
                            </dl>
                        </div>
                    </div>
                </li>
            </ul>
            <!-- start logo -->
            <h1 id="logo" class="alignleft"><a href="index2.html" title="INTERONET">INTERONET</a></h1>
            <!-- end logo -->
        </div>
        <div id="header-bottom" class="clear"></div>

    </div>
    <!-- end header -->
    <!-- start content -->
    <div id="container" class="clearfix">
        <div class="content">
            <h2>Create Slice</h2>

<?php
          // echo $_COOKIE["VMsUsageStatus"];
//            echo "<br/>";
//            $SwitchesUsageStatus = json_decode($response,true);
//            var_dump($SwitchesUsageStatus);
//            var_dump($SwitchesUsageStatus[0][1]);
//            echo count($SwitchesUsageStatus);
//            echo count($SwitchesUsageStatus[0]);
//            $a = $SwitchesUsageStatus[0][0];
//            echo $a['start'];
            //echo $SwitchesUsageStatus[0][0] -> end;

            //echo $SwitchesUsageStatus[0][0];
            //print_r($SwitchesUsageStatus) ;


            ?>
            <br/>
            <span style="font-size: larger"><strong>sw0</strong></span>

            <canvas id="Cs0" width="720" height="20" style="border:1px solid #000000;background-color: #cccccc;" tabindex="1"></canvas>
            <br/>
            <span style="font-size: larger"><strong>sw1</strong></span>
            <canvas id="Cs1" width="720"  height="20" style="border:1px solid #000000;background-color: #cccccc" tabindex="1"></canvas>
            <br/>
            <span style="font-size: larger"><strong>sw2</strong></span>
            <canvas id="Cs2" width="720"  height="20" style="border:1px solid #000000;background-color: #cccccc;" tabindex="1"></canvas>
            <br/>
            <span style="font-size: larger"><strong>sw3</strong></span>
            <canvas id="Cs3" width="720"  height="20" style="border:1px solid #000000;background-color: #cccccc;" tabindex="1"></canvas>
<br/>
            <span style="font-size: larger"><strong>vm0</strong></span>

            <canvas id="Cv0" width="720" height="20" style="border:1px solid #000000;background-color: #cccccc;" tabindex="1"></canvas>
            <br/>
            <span style="font-size: larger"><strong>vm1</strong></span>
            <canvas id="Cv1" width="720"  height="20" style="border:1px solid #000000;background-color: #cccccc;" tabindex="1"></canvas>
            <br/>
            <span style="font-size: larger"><strong>vm2</strong></span>
            <canvas id="Cv2" width="720"  height="20" style="border:1px solid #000000;background-color: #cccccc;" tabindex="1"></canvas>
            <br/>
            <span style="font-size: larger"><strong>vm3</strong></span>
            <canvas id="Cv3" width="720"  height="20" style="border:1px solid #000000;background-color: #cccccc;" tabindex="1"></canvas>
            <br/>
            <span style="font-size: larger"><strong>vm4</strong></span>

            <canvas id="Cv4" width="720"  height="20" style="border:1px solid #000000;background-color: #cccccc;" tabindex="1"></canvas>
            <br/>
            <span style="font-size: larger"><strong>vm5</strong></span>
            <canvas id="Cv5" width="720"  height="20" style="border:1px solid #000000;background-color: #cccccc" tabindex="1"></canvas>
            <br/>
            <span style="font-size: larger"><strong>vm6</strong></span>
            <canvas id="Cv6" width="720"  height="20" style="border:1px solid #000000;background-color: #cccccc" tabindex="1"></canvas>
            <br/>
            <span style="font-size: larger"><strong>vm7</strong></span>
            <canvas id="Cv7" width="720"  height="20" style="border:1px solid #000000;background-color: #cccccc" tabindex="1"></canvas>
            <script type="text/javascript">

                var SwitchesUsageStatus = eval('(' + getCookie("SwitchesUsageStatus") + ')');
                var SwitchesUsageStatusLength = 0;
                for(var slength in SwitchesUsageStatus)
                {
                    SwitchesUsageStatusLength++;
                }
                //document.getElementById("Canvas0").setAttribute("title",n);
                for(var i=0;i<SwitchesUsageStatusLength;i++)
                {
                    var canvas = document.getElementById("Cs"+i);
                    var ctx=canvas.getContext("2d");
                    for(var k=1 ; k<6;k++)
                    {
                        ctx.beginPath();
                        ctx.lineWidth=0.1;
                        ctx.lineCap="butt";
                        ctx.moveTo(120*k,0);
                        ctx.lineTo(120*k,30);
                        ctx.stroke();
                    }

                   canvas.addEventListener("mouseover", doMouseDown, false);
                    for(var j=0; j < SwitchesUsageStatus[i].length ; j++)
                    {
                        var startTime =SwitchesUsageStatus[i][j].start;
                        var endTime =SwitchesUsageStatus[i][j].end;
//                        var startHour = parseInt(startTime.substr(0,2));
//                        var startMinute = parseInt(startTime.substr(3,5));
                        var startPoint = parseInt((parseInt(startTime.substr(0,2))*60+parseInt(startTime.substr(3,5)))/2);
//                        var endHour = parseInt(endTime.substr(0,2));
//                        var endMinute = parseInt(endTime.substr(3,5));
                        var endPoint = parseInt((parseInt(endTime.substr(0,2))*60+parseInt(endTime.substr(3,5)))/2);
                        var cxt=canvas.getContext("2d");


                        cxt.fillStyle="#666666";
                        cxt.fillRect(startPoint,0,endPoint-startPoint,20);


                    }
                }

                var VMsUsageStatus = eval('('+getCookie("VMsUsageStatus")+')');
                var VMsUsageStatusLength = 0;
                for( var vlength in VMsUsageStatus)
                {
                    VMsUsageStatusLength++;
                }
               // document.getElementById("Cv0").setAttribute("title",VMsUsageStatusLength);
                for( i=0;i<VMsUsageStatusLength;i++)
                {
                     canvas = document.getElementById("Cv"+i);
                    ctx=canvas.getContext("2d");
                    for( k=1 ; k<6;k++)
                    {
                        ctx.beginPath();
                        ctx.lineWidth=0.1;
                        ctx.lineCap="butt";
                        ctx.moveTo(120*k,0);
                        ctx.lineTo(120*k,30);
                        ctx.stroke();
                    }
                     canvas.addEventListener("mouseover", doMouseDown, false);
                    for(j=0; j < VMsUsageStatus[i].length ; j++)
                    {
                        startTime =VMsUsageStatus[i][j].start;
                         endTime =VMsUsageStatus[i][j].end;
//                        var startHour = parseInt(startTime.substr(0,2));
//                        var startMinute = parseInt(startTime.substr(3,5));
                         startPoint = parseInt((parseInt(startTime.substr(0,2))*60+parseInt(startTime.substr(3,5)))/2);
//                        var endHour = parseInt(endTime.substr(0,2));
//                        var endMinute = parseInt(endTime.substr(3,5));
                         endPoint = parseInt((parseInt(endTime.substr(0,2))*60+parseInt(endTime.substr(3,5)))/2);
                         cxt=canvas.getContext("2d");
                        cxt.fillStyle="#666666";
                        cxt.fillRect(startPoint,0,endPoint-startPoint,20);
                    }
                }


                    function getX(obj){
                        var ParentObj=obj;
                        var left=obj.offsetLeft;
                        while(ParentObj=ParentObj.offsetParent){
                            left+=ParentObj.offsetLeft;
                        }
                        return left;
                    }
                function doMouseDown(event)
                {
                    var x = event.pageX;
                    //var c=document.getElementById("Canvas0");
                    var a = parseInt(getX(this));
                    var hour = parseInt((x-a)/30);
                    var minute = parseInt((x-a)%30);
                    if(minute<10) minute = "0"+minute;
                    this.setAttribute("title",hour+":"+minute);
                   // document.getElementById("myCanvas").innerHTML=hour+":"+minute;
                }
               //rectangle(0);

            </script>

            <form class="form-horizontal" method="post" action="CreateSliceTwo.php" onsubmit="return validate(this);">
                <div class="col2-3 ">
                    <h3>Domain</h3>
                    <div id="tab-one">
                        <ul class="nav">
                            <li class="nav-li first"><a href="#first" class="current">Xi‘an</a></li>
                            <li class="nav-li"><a href="#second">Nanjing</a></li>
                            <li class="nav-li"><a href="#third">Wuhan</a></li>
                            <li class="nav-li"><a href="#fourth">Extend</a></li>
                        </ul>
                        <div class="list-wrap">

                            <div id="first">
                                <div class="control-group">
                                    <label class="control-label" for="inputXianSwitch">Switch</label>
                                    <div class="controls">
                                        <input id="inputXianSwitch" name="XianSwitch" class="input-medium" value="0" type="text" />
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label class="control-label"  for="inputXianVM">VM</label>
                                    <div class="controls">
                                        <input id="inputXianVM" name="XianVM" class="input-medium" type="text" value="0" />
                                    </div>
                                </div>
                            </div>
                            <div id="second" class="hide">
                                <div class="control-group">
                                    <label class="control-label" for="inputNanjingSwitch">Switch</label>
                                    <div class="controls">
                                        <input id="inputNanjingSwitch" name="NanjingSwitch" class="input-medium" type="text" value="0"/>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label class="control-label" for="inputNanjingVM">VM</label>
                                    <div class="controls">
                                        <input id="inputNanjingVM" name="NanjingVM" class="input-medium" type="text" value="0"/>
                                    </div>
                                </div>
                            </div>
                            <div id="third" class="hide">
                                <div class="control-group">
                                    <label class="control-label" for="inputWuhanSwitch">Switch</label>
                                    <div class="controls">
                                        <input id="inputWuhanSwitch" name="WuhanSwitch" class="input-medium" type="text" value="0"/>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label class="control-label" for="inputWuhanVM">VM</label>
                                    <div class="controls">
                                        <input id="inputWuhanVM" name="WuhanVM" class="input-medium" type="text" value="0"/>
                                    </div>
                                </div>
                            </div>
                            <div id="fourth" class="hide">
                                <p> Waiting to be extended </p>

                            </div>
                        </div>
                        <!-- END List Wrap -->
                    </div>
                </div>
            <div class="col1-3 last">

                <div id="accordion">

                        <fieldset>
                            <h3>Duration Time</h3>
                            <div class="control-group">
                                <label class="control-label" for="DurationStartTime">Start Time</label>
                                <div class="controls">
                                    <input id="DurationStartTime" name="StartTime"  class="input-small" type="text" value="00:00"/>
                                </div>
                                <label class="control-label" for="DurationEndTime">End Time</label>
                                <div class="controls">
                                    <input id="DurationEndTime" name="EndTime" class="input-small" type="text" value="00:00"/>
                                </div>
                            </div>
                        </fieldset>
                        <div class="control-group">

                                <button type="button" class="btn btn-success btn-large" disabled="disabled">Back</button>
                                <button type="submit" class="btn btn-success btn-large" >Next</button>
                            <script type="text/javascript">
                                function validate(form) {

                                    var returnValue =true;
                                    if(form.XianSwitch.value > 10 || form.NanjingSwitch.value >10 || form.WuhanSwitch.value >10)
                                    {
                                        returnValue = false;
                                        alert("The number of switch should be less than 10");

                                    }if(form.XianVM.value > 8 || form.NanjingVM.value >8 || form.WuhanVM.value >8)
                                    {
                                        returnValue = false;
                                        alert("The number of virtual machine should be less than 8");

                                    }

                                    return returnValue;
                                }
                            </script>

                        </div>


                </div>
            </div>

            </form>




        </div>
    </div>
    <!-- end content -->
</div>
<!-- start footer -->
<div id="footer">
    <div class="footer-content clearfix">
        <div class="col1-3">
            <h3>Contact Us</h3>

            <p>400<br/>
                Deng Shuoling<br/>
                xian<br/>
                Phone:  123 4567</p>
        </div>
        <div class="col1-4">
            <h3>About Us</h3>

            <p>400 </p>
        </div>

        <div class="col1-3 last">
            <h3>We are Social</h3>

            <p>Connect with us through the following social media platforms!</p>
            <ul class="social">
                <li class="twitter first"><a href="http://twitter.com/#!/envato/" title="twitter">Visit our twitter
                    Account</a></li>
                <li class="facebook"><a href="http://facebook.com/envato/" title="facebook">Visit our facebook
                    Account</a></li>
                <li class="dribble"><a href="http://dribble.com/" title="dribble">Visit our dribble Account</a></li>
                <li class="flickr"><a href="http://flickr.com/" title="flickr">Visit our flickr Account</a></li>
                <li class="vimeo"><a href="http://vimeo.com/" title="vimeo">Visit our vimeo Account</a></li>
            </ul>
            <div class="clear"></div>
        </div>
        <div id="footer-bottom" class="clear">


            <p class="alignright"><a href="#" onclick="return false" title="Link">Terms of Use</a> <span
                    class="padding">|</span> <a href="#" onclick="return false" title="Link">Privacy Policy</a></p>
        </div>
    </div>
</div>
<!-- end footer -->
<div id="background"></div>

</body>
</html>
