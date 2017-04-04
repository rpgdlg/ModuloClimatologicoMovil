<?php 
	if(	!empty($_GET['t']) and !empty($_GET['h']) and !empty($_GET['p']) and !empty($_GET['l'])){

		$temperatura = $_GET['t'];
		$humedad = $_GET['h'];
		$presion = $_GET['p'];
		$luminosidad = $_GET['l'];

		$con = new PDO("mysql:host=localhost;dbname=mcm", "root", "");

		$res = $con->query("INSERT INTO registro (Id, IdSensor, Fecha, Humedad, Presion, Temperatura, Luminosidad) VALUES (NULL, 1, NULL, '$humedad', '$presion', '$temperatura', '$luminosidad')");

		if ($res){
			echo json_encode(true);
		}else{
			echo json_encode(false);
		}
	}
?>