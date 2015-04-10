<?php
/**
 * Created by PhpStorm.
 * User: root
 * Date: 15-3-12
 * Time: 上午11:07
 */

$SwitchNum = $_COOKIE["SwitchNum"];
$VMNum = $_COOKIE["VMNum"];
$StartTime = $_COOKIE["StartTime"];
$EndTime = $_COOKIE["EndTime"];

$topology  = $_COOKIE["Topology"];
$obj  = json_decode($topology);


$ip = "";
$port = "";
$SwitchConf=array();
if($_SERVER["REQUEST_METHOD"] == "POST")
{
    $ip = $_POST["IP"];
    $port = $_POST["Port"];
    for($i=0; $i < $SwitchNum; $i++)
    {      $array1 = array('s'.$i =>$_POST["OF$i"]);
        $SwitchConf =array_merge($SwitchConf,$array1);
    }
}




$order = array('num'=>array('switchesNum'=>$SwitchNum,'vmsNum'=>$VMNum),'time'=>array('begin'=>$StartTime,'end'=>$EndTime),'topology'=>$obj,'switchConf'=>$SwitchConf,'controllerConf'=>array('ip'=>$ip,'port'=>$port));

$jsonOrder = json_encode($order);
//echo $jsonOrder;

include(dirname(__FILE__) . "/jsonrpcphp/jsonRPCClient.php");

$client = new jsonRPCClient("http://202.117.15.80:8080/abc");
$authToken = $_COOKIE["authToken"];
$response = $client->__call("orderSlice",array($authToken,$jsonOrder));
//echo $response;
if ($response == "Success")
{
    echo"<script type='text/javascript'>location='index2.html';alert('Success');
            </script>";
}
else
{
    echo"<script type='text/javascript'>location='CreateSlice.php';alert('failure');
            </script>";
}
