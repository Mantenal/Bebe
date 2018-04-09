<?PHP
$hostname_localhost="localhost";
$database_localhost="simon_bebe";
$username_localhost="Mantenal_root";
$password_localhost="Mantenal_root";

$json=array();

	if(isset($_GET["id"])&& isset($_GET["n_nacimiento"]) && isset($_GET["peso"]) && isset($_GET["sexo"]) ){
        $id=$_GET['id'];
        $n_nacimiento=$_GET['n_nacimiento'];
        $peso=$_GET['peso'];
        $sexo=$_GET['sexo'];
	
		
	$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);	
    $insert="UPDATE bebe SET n_nacimiento='{$n_nacimiento}',peso='{$peso}',sexo='{$sexo}' WHERE id='{$id}'";
	$resultado_insert=mysqli_query($conexion,$insert);
		
		
		
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