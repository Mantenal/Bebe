<?PHP
$hostname_localhost="localhost";
$database_localhost="simon_bebe";
$username_localhost="Mantenal_root";
$password_localhost="Mantenal_root";

$json=array();

	if(isset($_GET["id"])){
		$id=$_GET["id"];
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

		$consulta="select * from bebe where id= '{$id}'";
		$resultado=mysqli_query($conexion,$consulta);
			
		if($registro=mysqli_fetch_array($resultado)){
			$json['actual'][]=$registro;
		//	echo $registro['id'].' - '.$registro['nombre'].' - '.$registro['profesion'].'<br/>';
		}else{
			$resultar["id"]=0;
			$resultar["nombre"]='no registra';
			$resultar["profesion"]='no registra';
			$json['actual'][]=$resultar;
		}
		
		mysqli_close($conexion);
		echo json_encode($json);
	}
	else{
		$resultar["success"]=0;
		$resultar["message"]='Ws no Retorna';
		$json['actual'][]=$resultar;
		echo json_encode($json);
	}
?>