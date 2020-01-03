package com.gasstations.gastation;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class UsuarioActivity extends Activity
{
		// variável para exibir uma mensagem
		private ProgressDialog pDialog;

		//JSONParser jsonParser = new JSONParser();
		Button entrar, cadastrar;
		EditText editlogin, editsenha;
	
		private String urlVerificaLogin = "get_usuario_detalhes.php";
	
		
		// JSON Node names
		private static final String TAG_SUCCESS = "success";

		@Override
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_usuario);
			
			entrar = (Button) findViewById(R.id.buttonacessar);
			cadastrar = (Button) findViewById(R.id.buttoncadastrar);
			editlogin = (EditText) findViewById(R.id.editlogin);
			editsenha = (EditText) findViewById(R.id.editsenha);
			
			urlVerificaLogin = Servers.getServersInstancia(this).getServerEmUso() + urlVerificaLogin;
			
			// verifica preenchimento do login
			entrar.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View view) 
				{
					String login = editlogin.getText().toString();
					String senha = editsenha.getText().toString();
					if (login.equals("") && senha.equals("")) 
					{
						
						Toast.makeText(getApplicationContext(),
								"Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
					} else 
					{
						Toast.makeText(getApplicationContext(), " Seja Bem-Vindo",Toast.LENGTH_LONG).show();
						new VerificaLogin().execute();
					
					}
				}
			});
			cadastrar.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View view) 
				{
					abrirTelaDeCadastroDeUsuario();
				}
			});
		}

		/**
		 * Background Async Task to Create new product
		 * */
		class VerificaLogin extends AsyncTask<String, String, String> 
		{

			int success = 0;
			//FileOutputStream fileOut=openFileOutput("LoginSenha",MODE_WORLD_READABLE);
			
			 //Mostra dialogo
			
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();
				pDialog = new ProgressDialog(UsuarioActivity.this);
				pDialog.setMessage("Efetuando login ...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				//Toast.makeText(getApplicationContext(), " OLAAA",Toast.LENGTH_LONG).show();
			}

			
			  //Criando Login
			 protected String doInBackground(String... args) 
			{
				
				String usuario= editlogin.getText().toString();
				String senha = editsenha.getText().toString();

				// Parâmetros de construção
				Map<String,String> params = new HashMap<String, String>();
				params.put("usuario", usuario);
				params.put("senha", senha);
	    	    

				// getting JSON Object
				// aceita método POST
				JSONObject json = JSONParser.makeHttpRequest(urlVerificaLogin,
						"POST", params);				
				// verifica o log cat de resposta
				Log.d("Create Response", json.toString());

				// verifica a variável sucess
				try 
				{
					success = json.getInt(TAG_SUCCESS);
					
				} catch (JSONException e) 
				{
					e.printStackTrace();
				}

				return null;
			}

			/**
			 * apresenta resultado após executar os comandos acima
			 * **/
			protected void onPostExecute(String file_url) 
			{

				// dismiss the dialog once done
				pDialog.dismiss();

				if (success == 1) {
					Toast.makeText(getApplicationContext(), " Usuário autenticado.",
							Toast.LENGTH_LONG).show();
					
					abrirTelaDoMapa();
				} else {
					// failed to create product
					Toast.makeText(UsuarioActivity.this, " Usuário não reconhecido.",
					Toast.LENGTH_LONG).show();
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							UsuarioActivity.this);
					alertDialog.setTitle("Novo Usuário");
					alertDialog. 
						setMessage ("Deseja se tornar um usuário da GaStations?");
					alertDialog.setPositiveButton("Sim", 
							new DialogInterface.OnClickListener() 
							{
								public void onClick(DialogInterface dialog,int which) 
								{
									abrirTelaDeCadastroDeUsuario();
								}
							});
					// Setting Negative "NO" Button
					alertDialog.setNegativeButton("Não",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Write your code here to invoke NO event
									// Toast.makeText(getApplicationContext(),
									// "You clicked on NO",
									// Toast.LENGTH_SHORT).show();
									dialog.cancel();
									abrirTelaDoMapaNaoUsuario();
								}
							});

					// Showing Alert Message
					alertDialog.show();
				}

			}

		}
		protected void abrirTelaDoMapa() 
		{
			//Toast.makeText(getApplicationContext(), " Mapa", Toast.LENGTH_LONG).show();
			
			Intent trocaTela = new Intent(UsuarioActivity.this, GPSMapaActivity.class);
			startActivity(trocaTela);
			finish();
		}
		protected void abrirTelaDoMapaNaoUsuario() 
		{
			//Toast.makeText(getApplicationContext(), " Mapa", Toast.LENGTH_LONG).show();
			
			Intent trocaTela = new Intent(UsuarioActivity.this, GPSMapaNaoUsuarioActivity.class);
			startActivity(trocaTela);
			finish();
		}
		protected void abrirTelaDeCadastroDeUsuario() 
		{
			//Toast.makeText(getApplicationContext(), " Mapa", Toast.LENGTH_LONG).show();
			
			Intent trocaTela = new Intent(UsuarioActivity.this, CadastroUsuarioActivity.class);
			startActivity(trocaTela);
			finish();
		}
		
}