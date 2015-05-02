<?php
include(dirname(__FILE__) . "/jsonrpcphp/jsonRPCClient.php");

$client = new jsonRPCClient("http://127.0.0.1:8080/");
//$client = new jsonRPCClient("http:/202.117.15.80:8080/");
$authToken = $_COOKIE["authToken"];
$response = $client->__call("getOrdersList",array($authToken));
$response2 = $client->__call("getRunningSlices",array($authToken));
echo $response2;
$orderList = json_decode($response);
$runList =  json_decode($response2);
?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">

<head>
    <title>CheckWaitingSlice</title>

    <link href="css/styles.css" rel="stylesheet" type="text/css" media="screen"/>
    <link href="css/print.css" rel="stylesheet" type="text/css" media="print"/>
    <link href="css/bootstrap.css" rel="stylesheet" media="screen"/>
    <link href="css/floatMenu.css" rel="stylesheet"/>
    <script src="js/jquery-1.7.1.js" type="text/javascript"></script>
    <script src="js/bootstrap.js"></script>

    <script src="js/deleteCookie.js" type="text/javascript"></script>
    <script type="text/javascript">

        var name = "#floatMenu";
        var menuYloc = null;
        $(document).ready(function() {
            menuYloc = parseInt($(name).css("top").substring(0, $(name).css("top").indexOf("px")))
            $(window).scroll(function() {
                offset = menuYloc + $(document).scrollTop() + "px";
                $(name).animate({ top: offset }, { duration: 500, queue: false });
            });
            $(".btn").click(function(){

                var value=$(".btn").attr("name");
                document.cookie = "value" + " = " + value + ";";
                location = 'deleteOrder.php' ;

            });
        });

    </script>
</head>
<body>
<div id="wrapper">
    <!-- start header -->
    <div id="header">
        <div class="header-content">
            <div class="topnavbar clearfix">
                <span class="alignright">
                    <a class="padding-left" href="index.html" title="Logout" ><?php echo $_COOKIE["username"];?></a>
                    <a class="padding-left" href="index.html" title="Logout" onclick="delCookie('authToken')">Logout</a>
                </span>
            </div>
        </div>


    </div>
        <!-- start content -->
        <div id="container2" class="clearfix">
            <div class="content">
                <div class="col1-4">

                    <div id="floatMenu">

                        <ul class="nav nav-pills nav-stacked text-center">

                            <li >
                                <a class="first"  href="Create.php">CREATE</a>
                            </li>
                            <li class="active"><a>CHECK</a>
                            <ul class="nav nav-pills nav-stacked ">
                                <li><a  class="second"  href="#WaitingSlice">Waiting Slice</a></li>
                                <li><a  class="second"  href="#RunningSlice">Running Slice</a></li>
                            </ul>
                            </li>
                            <li><a class="first" href="#">UPDATE</a></li>
                        </ul>
                    </div>
                </div>


                <div class="col3-4 last">
                    <div class="context">
                        <br/>
                        <div id="WaitingSlice">

                              <h2>Waiting Slice</h2>
                            <table class="table table-hover">

                                <thead>
                                <tr >
                                    <th>
                                        SliceID
                                    </th>
                                    <th>
                                        StartTime
                                    </th>
                                    <th>
                                        EndTime
                                    </th>
                                    <th>
                                        # of Switch
                                    </th>
                                    <th>
                                        # of VM
                                    </th>
                                    <th>
                                        IP
                                    </th>
                                    <th>
                                        Port
                                    </th>
                                    <th>
                                        More
                                    </th>
                                    <th>
                                        Delete
                                    </th>
                                </tr>




                                </thead>
                                <tbody>
                                <?php

                                for($i=0;$i<count($orderList);$i++)
                                {
                                    $response2 = $client->__call("getOrderInfoByID",array($authToken,$orderList[$i]));
                                    $slice = json_decode($response2);
                                    if($i%3==0)
                                    {
                                        echo "<tr class='info'>";
                                    }
                                    elseif($i%3==1)
                                    {
                                        echo "<tr class='warning'>";
                                    }
                                    else
                                    {
                                        echo "<tr class='success'>";
                                    }

                                    showSlice($slice->OrderID);
                                    showSlice($slice->BeginTime);
                                    showSlice($slice->EndTime);
                                    showSlice($slice->SwitchNumber);
                                    showSlice($slice->VMNumber);
                                    showSlice($slice->ControllerIP);
                                    showSlice($slice->ControllerPort);
                                    echo "<td><a name='$slice->OrderID' href='#'>more</a>></td>";
                                    echo "<td><button name='$slice->OrderID' class='btn btn-danger btn-xs' type='button'>Delete</button></td>";
                                    echo "</tr>";
                                }
                                function  showSlice($name)
                                {

                                    echo "<td>";
                                    echo $name;
                                    echo "</td>";

                                }

                                ?>
                                </tbody>
                            </table>
                        </div>


                    </div>




                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br
                        /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br
                        /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br
                        /><br />

                </div>



            </div>
        </div>
    </div>




</body>
</html>