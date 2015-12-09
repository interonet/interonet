<?php

$authToken = $_COOKIE["authToken"];
$user = base64_decode($authToken);
$SwitchNum = $_POST["SwitchNum"];
$VMNum = $_POST["VMNum"];
$StartTime = $_POST["StartTimeHour"] . ":" . $_POST["StartTimeMinute"];
$EndTime = $_POST["EndTimeHour"] . ":" . $_POST["EndTimeMinute"];
$topology = json_decode($_POST["Topology"]);
$ip = $_POST["IP"];
$port = $_POST["Port"];
$SwitchConf = array();
$customSwitchConf = array();
for ($i = 0; $i < $SwitchNum; $i++) {
    $SwitchConf["s" . $i] = $_POST["OF$i"];
    if ($SwitchConf["s" . $i] == "custom") {
        $rootPath = $_FILES["root" . $i]["tmp_name"];  //25355
        $rootResult = checkRoot($rootPath, $user, $i);

        $systemPath = $_FILES["system" . $i]["tmp_name"];  //9
        $systemResult = checkSystem($systemPath, $user, $i);

        $uImagePath = $_FILES["uImage" . $i]["tmp_name"];  //395
        $uImageResult = checkUImage($uImagePath, $user, $i);

        $devicePath = $_FILES["device" . $i]["tmp_name"];  //20813
        $deviceResult = checkDevice($devicePath, $user, $i);

        if ($rootResult && $systemResult && $uImageResult && $deviceResult) {
            $customSwitchConf["s".$i] = array("root-fs"=>$rootResult,"system-bit"=>$systemResult,"uImage"=>$uImageResult,"device-tree"=>$deviceResult);
        }
        else {
            echo "< script type='text/javascript'>location=history.go(-1);alert('The file type you upload is illegal');
             </script>";
            exit();

        }

    }
}

$order = array('num' => array('switchesNum' => $SwitchNum, 'vmsNum' => $VMNum), 'time' => array('begin' => $StartTime, 'end' => $EndTime), 'topology' => $topology, 'switchConf' => $SwitchConf, 'controllerConf' => array('ip' => $ip, 'port' => $port),'customSwitchConf'=>$customSwitchConf);

$jsonOrder = json_encode($order);

include(dirname(__FILE__) . "/../jsonrpcphp/jsonRPCClient.php");

$response = $client->__call("orderSlice", array($authToken, $jsonOrder));
//echo $response;
if ($response == "Success") {
    echo "<script type='text/javascript'>location='../Check.html';alert('Success');
            </script>";
} else {
    echo "<script type='text/javascript'>location='../Create.html';alert('failure');
            </script>";
}




//检测root-bin文件类型
function checkRoot($path, $user, $i)
{
    $typeCode = getTypeCode($path);
    if ($typeCode == "25355") {
        $location = "upload/" . $user . "sw" . $i . time() . "root_bin" . ".tar.xz";
        move_uploaded_file($path, $location);
        $value = "http://202.117.15.78/interonetWeb/" . $location;
        return $value;
    } else {
        return false;
    }

}
//检测system-bit文件类性
function checkSystem($path, $user, $i)
{
    $typeCode = getTypeCode($path);
    if ($typeCode == "9") {
        $location = "upload/" . $user . "sw" . $i . time() . "system.bit";
        move_uploaded_file($path, $location);
        $value = "http://202.117.15.78/interonetWeb/" . $location;
        return $value;
    } else {
        return false;
    }
}
//检测uImage文件类型
function checkUImage($path, $user, $i)
{
    $typeCode = getTypeCode($path);
    if ($typeCode == "395") {
        $location = "upload/" . $user . "sw" . $i . time() . "uImage";
        move_uploaded_file($path, $location);
        $value = "http://202.117.15.78/interonetWeb/" . $location;
        return $value;
    } else {
        return false;
    }
}
//检测device-tree文件类型
function checkDevice($path, $user, $i)
{
    $typeCode = getTypeCode($path);
    if ($typeCode == "20813") {
        $location = "upload/" . $user . "sw" . $i . time() . "device_tree.dtb";
        move_uploaded_file($path, $location);
        $value = "http://202.117.15.78/interonetWeb/" . $location;
        return $value;
    } else {
        return false;
    }
}
//获取文件类型码
function getTypeCode($path)
{
    $fp = fopen($path, 'rb');  // 以二进制形式打开文件
    $file = fread($fp, 2); // 读取文件内容
    fclose($fp);
    $fileType = @unpack("C2chars", $file);
    $typeCode = intval($fileType['chars1'] . $fileType['chars2']);
    return $typeCode;
}

// if($_SERVER["REQUEST_METHOD"] == "POST")
// {
//     for($i=0; $i < $SwitchNum; $i++)
//     {
//         if ($_FILES["file".$i]["name"] != "")
//         {
//             $path = $_FILES["file".$i]["tmp_name"];
//             $fp = fopen($path, 'rb');  // 以二进制形式打开文件
//             $file = fread($fp, 2); // 读取文件内容
//             fclose($fp);
//             $fileType = @unpack("C2chars", $file);
//             $typeCode = intval($fileType['chars1'].$fileType['chars2']);
//             if($typeCode == "25355")
//             {

//                 $location = "upload/" . $user . "sw" . $i . time() . ".tar.xz";
//                 move_uploaded_file($_FILES["file" . $i]["tmp_name"], $location);
//                 $value = "http://202.117.15.78/interonetWeb/" . $location;
//                 $array1 = array('s' . $i => $value);
//             }
//             else
//             {
//             echo "< script type='text/javascript'>location=history.go(-1);alert('The file type you upload is illegal');
//             </script>";
//                 exit();

//             }
//         }
//         else
//         {
//             $array1 = array('s'.$i =>$_POST["OF$i"]);
//         }

//         $SwitchConf =array_merge($SwitchConf,$array1);
//     }
// }

//echo $jsonOrder;
//var_dump(json_decode($jsonOrder));


//$content2 = base64_encode($content1); // 将二进制信息编码成字符串
// echo $content2;
//

