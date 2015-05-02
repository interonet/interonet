<?php
/**
 * Created by PhpStorm.
 * User: Lenovo
 * Date: 2015/4/30
 * Time: 21:24
 */
include(dirname(__FILE__) . "/jsonrpcphp/jsonRPCClient.php");

$client = new jsonRPCClient("http://202.117.15.80:8080/abc");
$authToken = $_COOKIE["authToken"];
$response = $client->__call("getOrdersList",array($authToken));
$response2 = $client->__call("getRunningSlices",array($authToken));
//echo $response;
echo "<p>".$response2."</p><p>abc</p>";