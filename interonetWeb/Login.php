<?php

$username = $password = "";
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = test_input($_POST["username"]);
    $password = test_input($_POST["password"]);

}
function test_input($data)
{
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    return $data;
}

include(dirname(__FILE__) . "/jsonrpcphp/jsonRPCClient.php");

$client = new jsonRPCClient("http://202.117.15.80:8080/abc");
$response = $client->__call("authenticateUser",array($username,$password));

setcookie("authToken", $response);

//echo $response;


if($response == null || $response == "Failed")
{

    echo"<script type='text/javascript'>location='Login.html';alert('username or password is wrong, please check');
            </script>";
}
else
{
    header("location:index2.html");
}
//echo "<br/>";
//$response1 = $client->__call("getVMsUsageStatus",array($_COOKIE["authToken"]));
//echo $response1;
//echo "<br/>";
//$response2 = $client->__call("getOrdersList",array($_COOKIE["authToken"]));
//echo $response2;

//header("location:test.php");

?>
