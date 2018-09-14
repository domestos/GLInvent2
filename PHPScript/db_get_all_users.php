<?php


$response = array();

require 'db_connect.php';

$db = new DB_CONNECT();

$result = mysql_query("SELECT * FROM wp_employees") or die(mysql_error());

if (mysql_num_rows($result) > 0) {
    $response["users"] = array();

    while ($row = mysql_fetch_array($result) ) {
   
        $user= $row["name"];

      array_push($response["users"], $user);
    }

    $response["success"] = 1;

    echo (json_encode($response));
} else {
    $response["success"] = 0;
    $response["message"] = "User not found";

    echo  json_encode($response);
}
?>
