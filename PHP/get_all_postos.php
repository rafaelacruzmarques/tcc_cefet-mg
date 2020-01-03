<?php

	//Pega a saida e guarda.
    ob_start();	

	$response=array();

	//inclui a classe db connect

	require_once __DIR__. '/db_connect.php';

	

	//conecta o db

	$db= new DB_CONNECT();
	
	if(isset($_POST['latN']) AND isset($_POST['logN']) AND	isset($_POST['latS']) AND isset($_POST['logS']))
	{
		$latN = ($_POST['latN']);
		$logN =($_POST['logN']) ;
		$latS = ($_POST['latS']);
		$logS = ($_POST['logS']);
		
		
		//obter todos os usuarios da tabela
		
		$sql = "SELECT idPosto,coordLatid,coordLongit,adicionais,precoEtanol,precoGasolina,precoDiesel,bandeira,formaDePagamento,nomePosto,horarioDeFuncionamento
				FROM `cadastroposto`
				where
				(coordLatid BETWEEN ". min($latN, $latS)." AND ". max($latN, $latS)." )  
				AND
				(coordLongit BETWEEN ". min($logN, $logS)." AND ". max($logN, $logS) .")
				ORDER BY `idPosto`;";
							
		//echo $sql."<BR>";
				
		$result= mysqli_query($db->con,$sql);
						

		if(mysqli_num_rows($result)>0)
		{
			//echo $sql."<BR>";
			//usuarios

			$response["Postos"]=array();

			//loop para exibir todos os resultados

			while($row=mysqli_fetch_array($result))

			{

				$posto=array();

				$posto["id"]=$row["idPosto"];	
				$posto["lat"]=$row["coordLatid"];	
				$posto["log"]=$row["coordLongit"];	
				$posto["adicionais"]=$row["adicionais"];				
				$posto["gasolina"]=$row["precoGasolina"];
				$posto["nome"]=$row["nomePosto"];
				$posto["etanol"]=$row["precoEtanol"];
				$posto["diesel"]=$row["precoDiesel"];	
				$posto["bandeira"]=$row["bandeira"];
				$posto["formaDePagamento"]=$row["formaDePagamento"];
				$posto["horarioDeFuncionamento"]=$row["horarioDeFuncionamento"];
				//coloca o único usuario no final do array

				array_push($response["Postos"],$posto);

			}

			$response["success"]=1;

			//echo json_encode($response);

		}else
		{	

			$response["success"]=0;

			$response["message"]="Postos não encontrados.<BR>";
			echo "SQL com erro <br>" . $sql;
					

			//echo json_encode($response);	

		}
	}else
		{	

			$response["success"]=0;

			$response["message"]="Coordenadas Noroeste e Sudeste n���o enviadas.";
					

			//echo json_encode($response);	

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

	/*
	include __DIR__. "/debuglib.php";
	session_start();	
	show_vars();
	*/

?>