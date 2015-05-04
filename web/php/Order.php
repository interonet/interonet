<?php
/**
 * Created by PhpStorm.
 * User: root
 * Date: 15-3-12
 * Time: 上午11:07
 */

$authToken = $_COOKIE["authToken"];
$user = base64_decode($authToken);
$judge ="";
$SwitchNum = $_POST["SwitchNum"];
$VMNum = $_POST["VMNum"];
$StartTime = $_POST["StartTimeHour"].":".$_POST["StartTimeMinute"];
$EndTime = $_POST["EndTimeHour"].":".$_POST["EndTimeMinute"];
$topology = json_decode($_COOKIE["Topology"]);
$ip = $_POST["IP"];
$port = $_POST["Port"];

$SwitchConf=array();
if($_SERVER["REQUEST_METHOD"] == "POST")
{
    for($i=0; $i < $SwitchNum; $i++)
    {
        if ($_FILES["file".$i]["name"] != "")
        {
            $path = $_FILES["file".$i]["tmp_name"];
            $fp = fopen($path, 'rb');  // 以二进制形式打开文件
            $file = fread($fp, 2); // 读取文件内容
            fclose($fp);
            $fileType = @unpack("C2chars", $file);
            $typeCode = intval($fileType['chars1'].$fileType['chars2']);
            if($typeCode == "25355")
            {

                $location = "upload/" . $user . "sw" . $i . time() . ".tar.xz";
                move_uploaded_file($_FILES["file" . $i]["tmp_name"],
                    $location);
                $value = "http://202.117.15.78/interonetWeb/" . $location;
                $array1 = array('s' . $i => $value);
            }
            else
            {
            echo "<script type='text/javascript'>location=history.go(-1);alert('The file type you upload is illegal');
            </script>";
                exit();

            }
        }
        else
        {
            $array1 = array('s'.$i =>$_POST["OF$i"]);
        }

        $SwitchConf =array_merge($SwitchConf,$array1);
    }
}

$order = array('num'=>array('switchesNum'=>$SwitchNum,'vmsNum'=>$VMNum),'time'=>array('begin'=>$StartTime,'end'=>$EndTime),'topology'=>$topology ,'switchConf'=>$SwitchConf,'controllerConf'=>array('ip'=>$ip,'port'=>$port));

$jsonOrder = json_encode($order);
//echo $jsonOrder;
//var_dump(json_decode($jsonOrder));



//$content2 = base64_encode($content1); // 将二进制信息编码成字符串
// echo $content2;
//

include(dirname(__FILE__) . "/../jsonrpcphp/jsonRPCClient.php");

$response = $client->__call("orderSlice",array($authToken,$jsonOrder));
//echo $response;
if ($response == "Success")
{
    echo "<script type='text/javascript'>location='../Check.html';alert('Success');
            </script>";
}
else
{
    echo "<script type='text/javascript'>location='../Create.html';alert('failure');
            </script>";
}
