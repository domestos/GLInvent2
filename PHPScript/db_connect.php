<?php



class DB_CONNECT {

    function __construct() {
        $this->connect();
    }

    function __destruct() {
        $this->close();
    }

    function connect() {
    	//echo " - run connect<br>";
        require 'db_credential.php';

        $con = mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysql_error());
      mysql_set_charset('UTF8',$con);
       //mysql_query("SET NAMES 'utf8'");

        if ($con) {
 			//echo " - connect is seccuess <br>";
 		}else{
 		//	echo " -  fail connect <br>";
 		}
        $db = mysql_select_db(DB_DATABASE) or die(mysql_error()) or die(mysql_error());
        return $con;
    }

    function close() {
        mysql_close();
    }

}

?>
