<?php  

	$con = new PDO("mysql:host=".DB_HOST.";dbname=".DB_NAME, DB_USER, DB_PASS);

	$res = $con->query("TRUNCATE TABLE registro");

	echo "Se borró la base de datos.";
?>