<?php
include(dirname(__FILE__) . "/jsonrpcphp/jsonRPCClient.php");

$client = new jsonRPCClient("http://202.117.15.80:8080/abc");
$authToken = $_COOKIE["authToken"];
$response = $client->__call("getRunningSlices",array($authToken));
echo $response;
$runningList = json_decode($response);

?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="robots" content="follow"/>
    <meta http-equiv="Content-Language" content="de"/>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta name="publisher" content="ppandp"/>
    <meta name="Description" content="ppandp - Corporate Business xHTML Template"/>
    <title>CheckRunningSlice</title>
    <link href="css/reset.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="css/styles.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="css/print.css" rel="stylesheet" type="text/css" media="print" /><link href="css/bootstrap.css" rel="stylesheet" media="screen"/>
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
    <link href="css/reset.css" rel="stylesheet" type="text/css" media="screen"/>
    <link href="css/styles.css" rel="stylesheet" type="text/css" media="screen"/>
    <link href="css/print.css" rel="stylesheet" type="text/css" media="print"/>
    <link href="css/bootstrap.css" rel="stylesheet" media="screen"/>
    <script src="js/jquery-1.7.1.js" type="text/javascript"></script>
    <script src="js/custom-menu.js" type="text/javascript"></script>
    <script src="http://code.jquery.com/jquery.js"></script>
    <script src="js/bootstrap.js"></script>

    <script src="js/image-hover.js" type="text/javascript"></script>
    <script src="js/mailmask.js" type="text/javascript"></script>
    <script src="js/jquery.ui.core.js" type="text/javascript"></script>
    <script src="js/jquery.ui.widget.js" type="text/javascript"></script>
    <script src="js/jquery.ui.accordion.js" type="text/javascript"></script>
    <script type="text/javascript">
        jQuery(function () {
            jQuery("#accordion").accordion({
                autoHeight: false,
                navigation: true
            });
        });
    </script>
    <link href="css/onebyone.css" rel="stylesheet" type="text/css" media="screen"/>
    <script src="js/jquery.cycle.all.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(function () {
            $('#slider').cycle({
                fx: 'fade',
                fadeSpeed: 500,
                timeout: 4100,
                pager: '#cyclenav-full-width-image'
            });
        });
    </script>
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
                <li class=class="clearfix"><a href="index2.html" title="Home">Home</a>
                </li>
                <li><a href="#" onclick="return false" title="CreateSlice" class="cursor">Create</a>
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
    <div id="wrapper">
        <!-- start content -->
        <div id="container" class="clearfix">
            <div class="content">
                <h2>CHECK Running Slice</h2>
                <p class="large">On this webpage, you can check the slices that are running </p>


                <table class="table">
                    <thead>
                    <tr>
                        <th>
                            SliceName
                        </th>
                    </tr>
                    <?php

                    for($i=0;$i<count($runningList);$i++)
                    {
                        if($i%3==0)
                        {
                            echo "<tr class='alert-success'>";
                        }
                        elseif($i%3==1)
                        {
                            echo "<tr class='alert-error'>";
                        }
                        else
                        {
                            echo "<tr class='alert-block'>";
                        }

                        showSlice($runningList[$i]);
                        echo "</tr>";
                    }
                    function  showSlice($name)
                    {

                        echo "<td>";
                        echo $name;
                        echo "</td>";

                    }

                    ?>


                    </thead>
                    <tbody>

                    </tbody>
                </table>



            </div>
        </div>
    </div>
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

