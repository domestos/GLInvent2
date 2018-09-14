<?php
$response = array();
require 'db_connect.php';
$db = new DB_CONNECT();
if (isset($_GET["number"])){
    $number = $_GET['number'];

    $result = mysql_query("SELECT *FROM 'wp_inventory' WHERE 'number' LIKE '%$number%'");

    if (!empty($result)) {
        if (mysql_num_rows($result) > 0) {

            $result = mysql_fetch_array($result);
            $product = array();

             while ($row = mysql_fetch_array($result) ) {

            $product["id"] = $result["id"];
            $product["number"] = $result["number"];
            $product["item"] = $result["item"];
            $product["name_wks"] = $result["name_wks"];
            $product["lic"] = $result["lic"];
            $product["key_lic"] = $result["key_lic"];
            $product["owner"] = $result["owner"];
            $product["location"] = $result["location"];
            $product["description"] = $result["description"];
            $product["invoice"] = $result["invoice"];
            $response["success"] =1;
            $response["product"] = array();

            array_push($response["product"],$product);
            }
            
            echo json_encode($response);
        } else {
            $response["success"] =0;
            $response["message"] = "No product found";

            echo json_encode($response);
        }
    } else {
        $response["success"] =0;
        $response["message"] = "No product found";

        echo json_encode($response);
    }
} else {
    $response["success"] =0;
    $response["message"] = "Required field(s) is missing";

    echo json_encode($response);
}
?>