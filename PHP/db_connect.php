<?php

	

	class DB_CONNECT	

	{

	

		//construtor para conectar com o Banco

		function __construct()

		{

			//conecta ao banco

			$this->connect();

		}

		//destrutor para desconectar do banco

		function __destruct()

		{

			//desconecta do banco

			$this->close();

		}

		//função que faz a conexão com o banco

		function connect()

		{

			//importa as variáveis de conexão com o banco de dados

			require_once 'db_config.php'; 

			

			//conecta o banco de dados mysql

			$this->con=mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD) or die (mysql_error());

			//seleciona o banco de dados

			$db=mysqli_select_db( $this->con,DB_DATABASE) or die(mysql_error());

			

			//retorna a conexão do cursor

			return $this->con; 

			

		}

		//Função que fecha a conexão com o banco

		function close()

		{

			mysqli_close($this->con);

		}

			

	}	

?>			