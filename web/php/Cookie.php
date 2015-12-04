<?php
/**
 * Created by PhpStorm.
 * User: Lenovo
 * Date: 2015/6/2
 * Time: 16:27
 */

$type = $_POST["type"];
if($type ="Get")
{
    $CookieName = $_COOKIE["username"];
    echo $CookieName;
}
else if($type = "Del")
{
    setcookie("authToken", "", time()-3600);
    setcookie("username", "", time()-3600);
}
