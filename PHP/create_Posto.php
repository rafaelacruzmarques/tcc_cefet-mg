<?php
	
	
	//Debug
	include __DIR__. "/debuglib.php";
	session_start();	
	//show_vars();
	
    ob_start();
	
	$bandeira=$nomePosto=$adicionais=$formaDePagamento="";
	$precoGasolina=$precoEtanol=$precoDiesel=0;
	//$coodLatid=$coodLongit=0;
	$horarioDeFuncionamento="";
	
	
	
	//Código para criar um login no banco de dados
	//array para a resposta JSON. (caso necessite visualizar os login inseridos)
	$response=array();
	
	//verifica se há campos de preenchimento obrigatório
	//isset=informa se variável foi iniciada
	//if(isset($_POST['coodLatid'])&& isset($_POST['coodLongit']))
	//{
		if(
		isset($_POST['nomePosto'])&& 
		isset($_POST['bandeira']) &&
		isset($_POST['precoGasolina'])&&
		isset($_POST['precoEtanol'])&&		
		isset($_POST['precoDiesel'])&& 
		isset($_POST['formaDePagamento'])&&
		isset($_POST['coordLatid'])&& 
		isset($_POST['horarioDeFuncionamento'])&&
		isset($_POST['coordLongit']))
		{
			$nomePosto=$_POST['nomePosto'];
			$bandeira=$_POST['bandeira'];
			$coordLatid=$_POST['coordLatid'];
			$coordLongit=$_POST['coordLongit'];
			$formaDePagamento=$_POST['formaDePagamento'];
			$adicionais=$_POST['adicionais'];
			$precoGasolina=$_POST['precoGasolina'];
			$precoEtanol=$_POST['precoEtanol'];
			$precoDiesel=$_POST['precoDiesel'];
			$horarioDeFuncionamento=$_POST['horarioDeFuncionamento'];
			//inclui a classe db connect
			require_once __DIR__ . '/db_connect.php';
			
			//conecta o db
			$db=new DB_CONNECT();
			//insere uma nova linha no mysql
			$sql = "INSERT INTO cadastroposto(nomePosto,bandeira,adicionais,formaDePagamento,precoDiesel,precoEtanol,precoGasolina,coordLatid,coordLongit,horarioDeFuncionamento) VALUES('$nomePosto','$bandeira','$adicionais','$formaDePagamento','$precoDiesel','$precoEtanol','$precoGasolina','$coordLatid','$coordLongit','$horarioDeFuncionamento')";
			$result=$db->con->query($sql); 
			
			//verifica se a nova linha foi inserida
			if($result)
			{
				//linha inserida com sucesso
				$response["success"]=1;
				$response["message"]= "Posto cadastrado com sucesso!";
				
				//imprime a resposta JSON
				//echo json_encode($response);
			}
			else
			{
				//Falha ao inserir a nova linha no banco
				$response["success"]=0;
				$response["message"]="Falha no cadastro do posto. <br>".$sql."<br>".$db->con->error;
				
				//imprime a resposta JSON
				//echo json_encode($response);
			}
		
				
	}else
	{
		//campos obrigatórios não preenchidos
		$response["success"]=0;
		$response["message"]="Todos os campos devem ser preenchidos";
		
		//imprime a resposta JSON
		echo json_encode($response);
	}
	
	//Pega tudo que foi enviado para saida e coloca como debug.
	$debug .= ob_get_contents();
	$response["debug"] = trim($debug);
	// Já podemos encerrar o buffer e limpar tudo que há nele
    ob_end_clean();

	//Prepara o json para apresentação
	$retorno = json_encode($response);
	
	//Mostra a saida.
	echo $retorno;

	
	
?>		