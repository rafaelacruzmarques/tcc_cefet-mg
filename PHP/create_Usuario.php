<?php
	
	 //Pega a saida e guarda.

	//Debug
	include __DIR__. "/debuglib.php";
	session_start();	
	//show_vars();

	
    ob_start();
	
	
	$debug=$usuario=$senha=$nome=$sobrenome=$dataNascimento="";
	
	
	
	//Código para criar um login no banco de dados
	//array para a resposta JSON. (caso necessite visualizar os login inseridos)
	$response=array();
	
	//verifica se há campos de preenchimento obrigatório
	//isset=informa se variável foi iniciada
	if(isset($_POST['usuario'])&& isset($_POST['senha']))
	{
		
		$usuario=$_POST['usuario'];
		$senha=$_POST['senha'];
		$nome=$_POST['nome'];
		$sobrenome=$_POST['sobrenome'];
		$dataNascimento=$_POST['dataNascimento'];
					
		//inclui a classe db connect
		require_once __DIR__ . '/db_connect.php';
	
		
		//conecta o db
		$db=new DB_CONNECT();
		
		
		//insere uma nova linha no mysql
		$sql = "INSERT INTO usuario(usuario,senha,nome,sobrenome,dataNascimento) VALUES('$usuario','$senha','$nome','$sobrenome','$dataNascimento')";
				
		$result=$db->con->query($sql); 
	
		
		//verifica se a nova linha foi inserida
		if($result!=false)
		{
			//linha inserida com sucesso
			$response["success"]=1;
			$response["message"]= "Usuário cadastrado com sucesso!";
			
			//imprime a resposta JSON
			//echo json_encode($response);
		}
		else
		{
			//Falha ao inserir a nova linha no banco
			$response["success"]=0;
			$response["message"]="Falha no cadastro do usuário. <br>";
			$debug .= trim($sql."<br>Erro:".$db->error);			
			//imprime a resposta JSON
			//echo json_encode($response);
		}	
	}else
	{
		//campos obrigatórios não preenchidos
		$response["success"]=0;
		$response["message"]="Todos os campos devem ser preenchidos";
		
		//imprime a resposta JSON
		//echo json_encode($response);
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