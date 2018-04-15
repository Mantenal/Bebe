<?PHP
$hostname_localhost="localhost";
$database_localhost="simon_bebe";
$username_localhost="Mantenal_root";
$password_localhost="Mantenal_root";

$json=array();

	if(isset($_GET["id"]) ){
        $id=$_GET['id'];
      
	
		
	$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
		
    $insert="UPDATE bebe SET n_nacimiento='0',peso='0',sexo='0',config='0' WHERE id='{$id}'";
	$resultado_insert=mysqli_query($conexion,$insert);
	
	$insert1="UPDATE actual SET id='{$id}',temperatura='0',posicion='0',e_dormir='0',ritmo_c='0',ruido='0' WHERE id='{$id}' ";
        mysqli_query($conexion,$insert1);
        
      $insert2="UPDATE personalizacion SET id='{$id}',ritmo_c_max='0',ritmo_c_min='0',notificacion_s='0',notificacion_r='0' WHERE id='{$id}'";
      mysqli_query($conexion,$insert2);
	
	
	$insert3="UPDATE dia(id='{$id}',ritmo_c='0',temperatura='0',alarmas='0',actual='{$id}',bebe='{$id} WHERE id='{$id}'')";
		mysqli_query($conexion,$insert3);
		
		
	

      
		
		if($resultado_insert){
			$consulta="SELECT * FROM bebe WHERE id = '{$id}'";
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
            $resulta["notificacion_s"]='No Registra';
            $resulta["notificacion_r"]='No Registra';
			$json['personalizacion'][]=$resulta;
			echo json_encode($json);
		}
		
	}
	else{
			$resulta["id"]=0;
			$resulta["ritmo_c_max"]='WS No retorna';
            $resulta["notificacion_s"]='WS No retorna';
            $resulta["notificacion_r"]='WS No retorna';
			$json['personalizacion'][]=$resulta;
			echo json_encode($json);
		}

?>