<?php

$authToken = $_COOKIE["authToken"];
//$value = $_COOKIE["value"];
$OrderID = $_POST["OrderID"];
include(dirname(__FILE__) . "/../jsonrpcphp/jsonRPCClient.php");

$response = $client->__call("deleteOrderByID",array($authToken,$OrderID));

echo $response;

