package com.gasstations.gastation;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroUsuarioActivity extends Activity 
{
	private ProgressDialog pDialog;
	Button cadastro,cancelar;
	EditText editNome, editSobrenome,editUsuario,editSenha,editData;
	
	private String url_cadastro_usuario = "create_Usuario.php";
	private static final String TAG_SUCCESS = "success";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_cadastro_usuario);
		
		cadastro = (Button) findViewById(R.id.cadastrar);
		cancelar = (Button) findViewById(R.id.buttoncancelar);
		editUsuario = (EditText) findViewById(R.id.editUsuario);
		editSenha = (EditText) findViewById(R.id.editSenha);
		editNome = (EditText) findViewById(R.id.editNome);
		editSobrenome = (EditText) findViewById(R.id.editSobrenome);
		editData = (EditText) findViewById(R.id.editData);
		
		url_cadastro_usuario = Servers.getServersInstancia(this).getServerEmUso().urlString + url_cadastro_usuario;
				
		// verifica preenchimento do login
		cadastro.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				String usuario = editUsuario.getText().toString();
				String senha = editSenha.getText().toString();
				String nome = editNome.getText().toString();
				String sobrenome = editSobrenome.getText().toString();
				
				if ((nome.equals("")&&(sobrenome.equals(""))&&(usuario.equals("") && senha.equals(""))))
				{
					Toast.makeText(getApplicationContext(),
							"Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
				} else 
				{
					Toast.makeText(getApplicationContext(), " Seja Bem-Vindo",Toast.LENGTH_LONG).show();
					new NovoUsuario().execute();
				
				}
			}
		});
		cancelar.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				
				abrirTelaDoMapaNaoUsuario();
					


			}
		});
			

	}

	/**
	 * Background Async Task to Create new product
	 * */
	class NovoUsuario extends AsyncTask<String, String, String> 
	{

		int success = 0;

		
		 //Mostra dialogo
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(CadastroUsuarioActivity.this);
			pDialog.setMessage("Efetuando login ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			//Toast.makeText(getApplicationContext(), " OLAAA",Toast.LENGTH_LONG).show();
		}

		
		  //Criando Login
		 protected String doInBackground(String... args) 
		{
			
			String usuario = editUsuario.getText().toString();
			String senha = editSenha.getText().toString();
			String nome = editNome.getText().toString();
			String sobrenome = editSobrenome.getText().toString();
			String data=editData.getText().toString();

			// Parâmetros de construção
			Map<String,String> params = new HashMap<String, String>();
			params.put("usuario", usuario);
			params.put("senha", senha);
			params.put("nome", nome);
			params.put("sobrenome", sobrenome);
			params.put("Data de Nascimento", data);
    	    

			// getting JSON Object
			// aceita método POST
			JSONObject json = JSONParser.makeHttpRequest(url_cadastro_usuario,
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
			//Toast.makeText(getApplicationContext(), success,
				//	Toast.LENGTH_LONG).show();	

			if (success == 1)
			{
				Toast.makeText(getApplicationContext(), " Usuário cadastrado com sucesso!.",
				Toast.LENGTH_LONG).show();	
				Intent trocaTela = new Intent(CadastroUsuarioActivity.this, GPSMapaActivity.class);
				startActivity(trocaTela);
				finish();
				
			}
			else if( success == 0) 
			{
				// failed to create product
				Toast.makeText(CadastroUsuarioActivity.this, " Falha no cadastro do usuário.",
				Toast.LENGTH_LONG).show();
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						CadastroUsuarioActivity.this);
				alertDialog.setTitle("Novo Usuário");
				alertDialog. 
					setMessage ("Deseja tentar novamente?");
				alertDialog.setPositiveButton("Sim", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								Intent trocaTela = new Intent(CadastroUsuarioActivity.this, CadastroUsuarioActivity.class);
								startActivity(trocaTela);
								finish();
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
		
		Intent trocaTela = new Intent(CadastroUsuarioActivity.this, GPSMapaActivity.class);
		startActivity(trocaTela);
		finish();
	}
	protected void abrirTelaDoMapaNaoUsuario() 
	{
		//Toast.makeText(getApplicationContext(), " Mapa", Toast.LENGTH_LONG).show();
		
		Intent trocaTela = new Intent(CadastroUsuarioActivity.this, GPSMapaNaoUsuarioActivity.class);
		startActivity(trocaTela);
		finish();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cadastro_usuario, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}