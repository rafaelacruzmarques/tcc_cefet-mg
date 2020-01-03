<?php
   //Pega a saida e guarda.
    ob_start();
				
	$response=array();

	//inclui a classe db connect

	
	
	
	require_once __DIR__. "/db_connect.php";

	//conecta o db

	
	$db= new DB_CONNECT();

	
	if(isset($_POST['usuario'])&& isset($_POST['senha']))
	{
		$usuario=$_POST['usuario'];
		$senha=$_POST['senha'];

		//obter os usuarios da tabela

		$result=mysqli_query($db->con,"SELECT *FROM usuario WHERE usuario= '$usuario' and senha= '$senha' ");

		

		//se a variável result não for vazia

		if(!empty($result))

		{

			if(mysqli_num_rows($result)>0)

			{

				$result=mysqli_fetch_array($result);

				$usuario=array();

				$usuario["idUsuario"]=$result["idUsuario"];

				$usuario["usuario"]=$result["usuario"];

				$usuario["senha"]=$result["senha"];

				

				//sucesso

				$response["success"]=1;

				

				$response["usuario"]=array();

				array_push($response["usuario"],$usuario);

				

				//echo json_encode($response);

			}

			else

			{

				$response["success"]=0;

				$response["message"]="Usuario não cadastrado.";

				

				//echo json_encode($response);

			}		

		}else

		{

			$response["success"]=0;

			$response["message"]="Usuario não cadastrado.";

				

			//echo json_encode($response);	

		}

	}else
	{

		

		$response["success"]=0;

		$response["message"]="Todos os campos devem ser preenchidos.";

	}
	//Pega tudo que foi enviado para saida e coloca como debug.
	$debug = ob_get_contents();
	$response["debug"] = trim($debug);
	// Já podemos encerrar o buffer e limpar tudo que há nele
    ob_end_clean();

	//Prepara o json para apresentação
	$retorno = json_encode($response);
	
	//Mostra a saida.
	echo $retorno;

	/* //Debug
	include __DIR__. "/debuglib.php";
	session_start();	
	show_vars();
	*/
	
?>