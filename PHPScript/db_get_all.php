<?php


$response = array();

require 'db_connect.php';

$db = new DB_CONNECT();

$result = mysql_query("SELECT * FROM wp_inventory") or die(mysql_error());

if (mysql_num_rows($result) > 0) {
    $response["products"] = array();

    while ($row = mysql_fetch_array($result) ) {
    //  if($i<700){$i++;}else{break; }
        $product = array();
        $product["id"] = $row["id"];
        $product["number"] = $row["number"];
        $product["item"] = $row["item"];
        $product["name_wks"] = $row["name_wks"];
        $product["owner"] = $row["owner"];
        $product["location"] = $row["location"];
        $product["status_invent"] = $row["status_invent"];
      //  $product["status_sync"] = $row["status_sync"];
        $product["description"] = $row["description"];

      array_push($response["products"], $product);
    }

    $response["success"] = 1;

    echo (json_encode($response));
} else {
    $response["success"] = 0;
    $response["message"] = "No products found";

    echo  json_encode($response);
}
?>
