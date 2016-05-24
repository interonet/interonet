<?php
include(dirname(__FILE__) . "/../jsonrpcphp/jsonRPCClient.php");
$authToken = $_COOKIE["authToken"];
$type = $_POST["type"];
if($type == "switch")
{
    $response = $client->__call("getSwitchesUsageStatus",array($authToken));
}
else
{
    $response = $client->__call("getVMsUsageStatus",array($authToken));
}
echo $response;
