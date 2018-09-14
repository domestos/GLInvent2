<?php
 
$response = array();
 
if (isset($_POST['id']) ) {
 
    $id = $_POST['id'];
 
    require 'db_connect.php';
 
    $db = new DB_CONNECT();
 
    $result = mysql_query("UPDATE wp_inventory SET status_invent = ''");
 
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Status Invent was reseted.";
        echo json_encode($response);
    } else {
  	$response["success"] = 0;
	$response["message"] = "Error with database";
	echo json_encode($response);
   }
} else {
   $response["success"] = 0;
   $response["message"] = "Opps, works security method from a fool. You did not push the button RESET. ";
   echo json_encode($response);
}
?>
