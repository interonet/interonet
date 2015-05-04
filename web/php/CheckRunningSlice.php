<?php
include(dirname(__FILE__) . "/../jsonrpcphp/jsonRPCClient.php");


$authToken = $_COOKIE["authToken"];
$response = $client->__call("getRunningSlices",array($authToken));
$orderList = json_decode($response);
for($i=0;$i<count($orderList);$i++)
{
    $response2 = $client->__call("getRunningSliceInfoById",array($authToken,$orderList[$i]));
    $slice = json_decode($response2);
    $slice2 = json_decode($response2,true);
    $SwitchNum = count($slice2["userSW2domSWMapping"]);
    $VMNum = count($slice2["userVM2domVMMapping"]);

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
    showSlice($slice->SliceID);
    showSlice($slice->BeginTime);
    showSlice($slice->EndTime);
    showSwitchMap($slice->userSW2domSWMapping,$SwitchNum,$i);
    showVMMap($slice->userVM2domVMMapping,$VMNum,$i);


    showSlice($slice->ControllerIP);
    showSlice($slice->ControllerPort);

   echo "<td><button name='$slice->SliceID'   class='btn btn-primary btn-xs btnTopology' type='button'>check</button></td>";
     echo "<td><button name='$slice->SliceID' class='btn btn-danger btn-xs btnStop' type='button'>Stop</button></td>";


}
function  showSlice($name)
{

    echo "<td>";
    echo $name;
    echo "</td>";

}
function showSwitchMap(&$arr,$SwitchNum,$i)
{
    echo "<td>";
    echo "<table class='table table-hover'>";
    for($j=0 ; $j<$SwitchNum; $j++)
    {
        if($i%3==0)
        {
            echo "<tr  class='info'>";
        }
        elseif($i%3==1)
        {
            echo "<tr   class='warning'>";
        }
        else
        {
            echo "<tr  class='success'>";
        }
        $index = "s".$j;
        $map = $arr->$index;
        echo "<td>"."s".$j.":".$map."</td>";
        echo "</tr>";

    }
    echo "</table>";
    echo "</td>";

}
function showVMMap(&$arr,$VMNum,$i)
{
    echo "<td>";
    echo "<table class='table table-hover'>";
    for($j=0 ; $j< $VMNum; $j++)
    {
        if($i%3==0)
        {
            echo "<tr  class='info'>";
        }
        elseif($i%3==1)
        {
            echo "<tr   class='warning'>";
        }
        else
        {
            echo "<tr  class='success'>";
        }
        $index = "h".$j;
        $map = $arr->$index;
        echo "<td>"."h".$j.":".$map."</td>";
        echo "</tr>";

    }
    echo "</table>";
    echo "</td>";
}