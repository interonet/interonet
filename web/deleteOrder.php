<?php

$authToken = $_COOKIE["authToken"];
$value = $_COOKIE["value"];

include(dirname(__FILE__) . "/jsonrpcphp/jsonRPCClient.php");
//$client = new jsonRPCClient("http://202.117.15.80:8080/abc");
$client = new jsonRPCClient("http://127.0.0.1:8080/");
$response = $client->__call("deleteOrderByID",array($authToken,$value));
//echo $response;
setcookie("value","",time()-3600);
if($response=="Success")
{
    echo"<script type='text/javascript'>location='Check.php';alert('Stop Success');
            </script>";
}
else
{
    echo"<script type='text/javascript'>location='Check.php';alert('Stop Failure');
            </script>";
}