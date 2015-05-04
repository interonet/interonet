<?php
include(dirname(__FILE__) . "/../jsonrpcphp/jsonRPCClient.php");
$authToken = $_COOKIE["authToken"];
$SliceID = $_POST["SliceID"];
//echo $SliceID;
$response = $client->__call("getRunningSliceInfoById",array($authToken,$SliceID));
$slice = json_decode($response,true);
$SwitchNum= count($slice["userSW2domSWMapping"]);
$VMNum = count($slice["userVM2domVMMapping"]);
$response2 = Array('SwitchNum'=>$SwitchNum,'VMNum'=>$VMNum,'Topology'=>$slice["Topology"]);
$topology = json_encode($response2);
//echo $SwitchNum;
//echo $VMNum;
echo $topology;