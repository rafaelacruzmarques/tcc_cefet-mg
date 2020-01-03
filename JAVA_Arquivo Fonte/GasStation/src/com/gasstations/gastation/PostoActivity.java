package com.gasstations.gastation;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;






import com.google.android.gms.maps.model.LatLng;

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

public class PostoActivity extends Activity
{
	// variável para exibir uma mensagem
	private ProgressDialog pDialog;

	Button cadastro,cancelar;
	EditText nomeposto, bandeiraPosto;
	EditText horarioFunc;
	EditText precogasolina,precoetanol,precodiesel;
	EditText formaPagamento,infadicionais;
	String latitude, longitude;
	
	
	private static String url_create_posto = "create_Posto.php";
	
	
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posto);

		url_create_posto =  Servers.getServersInstancia(this).getServerEmUso().urlString+url_create_posto;
		cancelar = (Button) findViewById(R.id.buttoncancelar);
		cadastro = (Button) findViewById(R.id.cadastrarP);
		nomeposto = (EditText) findViewById(R.id.editnomeposto);
		bandeiraPosto = (EditText) findViewById(R.id.editbandeira);
		precogasolina =  (EditText) findViewById(R.id.editgasolina);
		precoetanol =  (EditText) findViewById(R.id.editetanol);
		precodiesel =  (EditText) findViewById(R.id.editdiesel);
		formaPagamento =  (EditText) findViewById(R.id.editpagamento);
		infadicionais =  (EditText) findViewById(R.id.editadicionais);
		latitude=this.getIntent().getStringExtra("latitude");
		longitude=this.getIntent().getStringExtra("longitude");
		horarioFunc=(EditText)findViewById(R.id.horarioFuncionamento);
		


		// verifica preenchimento do login
		cadastro.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				String nomePosto = nomeposto.getText().toString();
				String bandeira = bandeiraPosto.getText().toString();
				String precoGasolina = precogasolina.getText().toString();
				String formaDePagamento = formaPagamento.getText().toString();
				String precoDiesel = precodiesel.getText().toString();
				String precoEtanol = precoetanol.getText().toString();
				String adicionais = infadicionais.getText().toString();
				String horarioFuncionamento=horarioFunc.getText().toString();
				

				if (nomePosto.equals("") && bandeira.equals("") && precoGasolina.equals("")&&  precoDiesel.equals("")&& precoEtanol.equals("")&&formaDePagamento.equals("")&&(horarioFuncionamento.equals("")))
				{
					Toast.makeText(getApplicationContext(),
							"Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
				} else 
				{
					//Toast.makeText(getApplicationContext(), "O Posto foi cadastrado",Toast.LENGTH_LONG).show();
					
					new NovoPosto().execute();

				}
			}
		});
		cancelar.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				abrirTelaDoMapa();
			}
		});


	}
	class NovoPosto extends AsyncTask<String, String, String> 
	{

		int success = 0;


		//Mostra dialogo

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(PostoActivity.this);
			pDialog.setMessage("Efetuando Cadastro ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			
		}


		//Criando Login
		protected String doInBackground(String... args) 
		{

			String nomePosto = nomeposto.getText().toString();
			String bandeira = bandeiraPosto.getText().toString();
			String precoGasolina = precogasolina.getText().toString();
			String formaDePagamento = formaPagamento.getText().toString();
			String precoDiesel = precodiesel.getText().toString();
			String precoEtanol = precoetanol.getText().toString();
			String adicionais = infadicionais.getText().toString();
			String coordLatid=latitude;
			String coordLongit=longitude;
			String horarioDeFuncionamento=horarioFunc.getText().toString();
			
			// Parâmetros de construção
			Map<String,String> params = new HashMap<String, String>();
			params.put("coordLatid", coordLatid);
			params.put("coordLongit", coordLongit);
			params.put("nomePosto", nomePosto);
			params.put("bandeira", bandeira);
			params.put("precoGasolina", precoGasolina);
			params.put("precoEtanol", precoEtanol);
			params.put("precoDiesel", precoDiesel);
			params.put("formaDePagamento", formaDePagamento);
			params.put("adicionais", adicionais);
			params.put("horarioDeFuncionamento",horarioDeFuncionamento);


			// getting JSON Object
			// aceita método POST
			JSONObject json = JSONParser.makeHttpRequest(url_create_posto,
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

			if (success == 1) 
			{
				Toast.makeText(getApplicationContext(), " Posto Cadastrado com sucesso!Muito Obrigada!",
						Toast.LENGTH_LONG).show();	
				abrirTelaDoMapa();
				
				
				
			} else {
				Toast.makeText(getApplicationContext(), " Falha no cadastro do posto. Tente novamente.",
						Toast.LENGTH_LONG).show();	
				abrirTelaDoMapa();

				

			}


		}
	}
	protected void abrirTelaDoMapa() 
	{
		//Toast.makeText(getApplicationContext(), " Mapa", Toast.LENGTH_LONG).show();

		Intent trocaTela = new Intent(PostoActivity.this, GPSMapaActivity.class);

		startActivity(trocaTela);

		finish();
	}



}