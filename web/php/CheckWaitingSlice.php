<?php
include(dirname(__FILE__) . "/../jsonrpcphp/jsonRPCClient.php");


$authToken = $_COOKIE["authToken"];
$response = $client->__call("getOrdersList",array($authToken));
$orderList = json_decode($response);

    for($i=0;$i<count($orderList);$i++)
    {
        $response2 = $client->__call("getOrderInfoByID",array($authToken,$orderList[$i]));
        $slice = json_decode($response2);
        if($i%3==0)
        {
            echo "<tr id='$slice->OrderID' class='info'>";
        }
        elseif($i%3==1)
        {
            echo "<tr id='$slice->OrderID'  class='warning'>";
        }
        else
        {
            echo "<tr id='$slice->OrderID'  class='success'>";
        }

        showSlice($slice->OrderID);
        showSlice($slice->BeginTime);
        showSlice($slice->EndTime);
        showSlice($slice->SwitchNumber);
        showSlice($slice->VMNumber);
        showSlice($slice->ControllerIP);
        showSlice($slice->ControllerPort);
        echo "<td><a name='$slice->OrderID' href='#'>more</a>></td>";
        echo "<td><button name='$slice->OrderID' class='btn btn-danger btn-xs btnCheck' type='button'>Delete</button></td>";

        echo "</tr>";

      }

function  showSlice($name)
{

    echo "<td>";
    echo $name;
    echo "</td>";

}
