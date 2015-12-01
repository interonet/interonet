<?php

$username = $password = "";

    $username = test_input($_POST["username"]);
    $password = test_input($_POST["password"]);

function test_input($data)
{
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    return $data;
}

include(dirname(__FILE__) . "/../jsonrpcphp/jsonRPCClient.php");


$response = $client->__call("authenticateUser",array($username,$password));
setcookie("username",$username);
setcookie("authToken", $response);

//echo $response;


if($response == null || $response == "Failed")
{

    echo "<script type='text/javascript'>location='../Login.html';alert('username or password is wrong, please check');
            </script>";
}
else
{
    header("location:../Create.html");
}

