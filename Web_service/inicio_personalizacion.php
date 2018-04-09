<?PHP
$hostname_localhost="localhost";
$database_localhost="simon_bebe";
$username_localhost="Mantenal_root";
$password_localhost="Mantenal_root";

$json=array();

	if(isset($_GET["id"]) ){
		$id=$_GET['id'];
	
		
		$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
		
		$insert="INSERT INTO personalizacion(id,ritmo_c_max,ritmo_c_min,notificacion_s,notificacion_r) VALUES ('{$id}','0','0','0','0')";
		$resultado_insert=mysqli_query($conexion,$insert);
		
		if($resultado_insert){
			$consulta="SELECT * FROM personalizacion WHERE id = '{$id}'";
			$resultado=mysqli_query($conexion,$consulta);
			
			if($registro=mysqli_fetch_array($resultado)){
				$json['personalizacion'][]=$registro;
			}
			mysqli_close($conexion);
			echo json_encode($json);
		}
		else{
  echo($id);
			$resulta["id"]=0;
			$resulta["ritmo_c_max"]='No Registra';
            $resulta["ritmo_c_min"]='No Registra';
            $resulta["notificacion_s"]='No Registra';
            $resulta["notificacion_r"]='No Registra';
			$json['personalizacion'][]=$resulta;
			echo json_encode($json);
		}
		
	}
	else{
			$resulta["id"]=0;
			$resulta["ritmo_c_max"]='WS No retorna';
            $resulta["ritmo_c_min"]='WS No retorna';
            $resulta["notificacion_s"]='WS No retorna';
            $resulta["notificacion_r"]='WS No retorna';
			$json['personalizacion'][]=$resulta;
			echo json_encode($json);
		}

?>