<?php
	
	$idPosto=0;
	$bandeira=$nomePosto=$adicionais=$formaDePagamento="";
	$precoGasolina=$precoEtanol=$precoDiesel=0;
	$horarioDeFuncionamento="";
	
	
	//Código para criar um login no banco de dados
	//array para a resposta JSON. (caso necessite visualizar os login inseridos)
	$response=array();
	
	//verifica se há campos de preenchimento obrigatório
	//isset=informa se variável foi iniciada
	if(isset($_POST['idPosto'])&& isset($_POST['horarioDeFuncionamento']))
	{
		if(isset($_POST['nomePosto'])&& isset($_POST['bandeira']))
		{
			if(isset($_POST['precoGasolina'])&& isset($_POST['precoEtanol']))
			{
				if(isset($_POST['precoDiesel'])&& isset($_POST['formaDePagamento']))
				{
					$nomePosto=$_POST['nomePosto'];
					$bandeira=$_POST['bandeira'];
					$idPosto=$_POST['idPosto'];
					$formaDePagamento=$_POST['formaDePagamento'];
					$adicionais=$_POST['adicionais'];
					$precoGasolina=$_POST['precoGasolina'];
					$precoEtanol=$_POST['precoEtanol'];
					$precoDiesel=$_POST['precoDiesel'];
					$precoDiesel=$_POST['horarioDeFuncionamento'];
					//inclui a classe db connect
					require_once __DIR__ . '/db_connect.php';
					
					//conecta o db
					$db=new DB_CONNECT();
					//insere uma nova linha no mysql
					$sql = "UPDATE cadastroposto SET nomePosto='$nomePosto',bandeira='$bandeira',adicionais='$adicionais',formaDePagamento='$formaDePagamento',precoDiesel='$precoDiesel',precoEtanol='$precoEtanol',precoGasolina='$precoGasolina',horarioDeFuncionamento='$horarioDeFuncionamento' WHERE idPosto=$idPosto" ;
					$result=$db->con->query($sql) or die (mysql_error()); 
					
					//verifica se a nova linha foi inserida
					if($result)
					{
						//linha inserida com sucesso
						$response["success"]=1;
						$response["message"]= "Sucesso na atualização do posto!";
						
						//imprime a resposta JSON
						echo json_encode($response);
					}
					else
					{
						//Falha ao inserir a nova linha no banco
						$response["success"]=0;
						$response["message"]="Falha na atualização do posto. <br>".$sql."<br>".$db->error;
						
						//imprime a resposta JSON
						echo json_encode($response);
					}
					}
					}
					}	
	}
	else
	{
		//campos obrigatórios não preenchidos
		$response["success"]=0;
		$response["message"]="Todos os campos devem ser preenchidos";
		
		//imprime a resposta JSON
		echo json_encode($response);
	}
?>		