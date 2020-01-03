package com.gasstations.gastation;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.gasstations.gastation.PostoActivity.NovoPosto;
import com.google.android.gms.maps.model.LatLngBounds;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
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

public class DadosPosto extends ActionBarActivity  
{
	private ProgressDialog pDialog1;
	int i;
	int posicao;
	public String nome ,bandeira,fPagamento,adicionais,numPosto;
	String precogasolina,precoetanol,precodiesel;

	String horarioFuncionamento;
	public String idPosto;
	Button salvar,cancelar;
	
    EditText nomeposto, bandeiraPosto,pGasolina,pEtanol,pDiesel,formaPagamento,infadicionais,idpost,horarioFunc;
    GPSMapaActivity dadosMapa=new GPSMapaActivity();
    
    private static final String TAG_SUCCESS = "success";
    
    private static String url_dados_posto = "update_posto.php";
    
    
    @Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_dados_posto);
		salvar = (Button) findViewById(R.id.salvar);
		cancelar=(Button) findViewById(R.id.cancelar);
		nomeposto = (EditText) findViewById(R.id.editnomeposto);
		bandeiraPosto = (EditText) findViewById(R.id.editbandeira);
		pGasolina =  (EditText) findViewById(R.id.editgasolina);
	    pEtanol=  (EditText) findViewById(R.id.editetanol);
	    pDiesel =  (EditText) findViewById(R.id.editdiesel);
	    formaPagamento =  (EditText) findViewById(R.id.editpagamento);
	    infadicionais =  (EditText) findViewById(R.id.editadicionais);
	    horarioFunc=(EditText)findViewById(R.id.horarioFuncionamento);
	    
	  
    	nome=this.getIntent().getStringExtra("nomePosto");
    	bandeira=this.getIntent().getStringExtra("bandeiraPosto");
    	fPagamento=this.getIntent().getStringExtra("formPagamento");
    	adicionais=this.getIntent().getStringExtra("infAdicionais");
    	precogasolina=this.getIntent().getStringExtra("pGasolina");
    	precoetanol=this.getIntent().getStringExtra("pEtanol");
    	precodiesel=this.getIntent().getStringExtra("pDiesel");
    	numPosto=this.getIntent().getStringExtra("idPosto");
    	horarioFuncionamento=this.getIntent().getStringExtra("horarioFunc");
    	
    	//campos do formulario recebe os valores
    	nomeposto.setText(nome);
    	bandeiraPosto.setText(bandeira);
    	pGasolina.setText(precogasolina);
    	pEtanol.setText(precoetanol);
    	pDiesel.setText(precodiesel);
    	pGasolina.setText(precogasolina);
    	formaPagamento.setText(fPagamento);
    	infadicionais.setText(adicionais);
	    horarioFunc.setText(horarioFuncionamento);	
    	url_dados_posto = Servers.getServersInstancia(this).getServerEmUso().urlString + url_dados_posto;	
	    
	    salvar.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				new UpdatePosto().execute();
				
				
			}
		});
	    cancelar.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				 abreTelaDoMapa();
			}
		});
	}
	 
	 
	class UpdatePosto extends AsyncTask<String, String, String> 
	{

		int success = 0;
		
		
		 //Mostra dialogo
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			pDialog1 = new ProgressDialog(DadosPosto.this);
			pDialog1.setMessage("Atualizando Cadastro ...");
			pDialog1.setIndeterminate(false);
			pDialog1.setCancelable(true);
			pDialog1.show();
			
		}

		
		  //Criando Login
		 protected String doInBackground(String... args) 
		{

			String nomePosto = nomeposto.getText().toString();
			String bandeira = bandeiraPosto.getText().toString();
			String precoGasolina = pGasolina.getText().toString();
			String formaDePagamento = formaPagamento.getText().toString();
			String precoDiesel = pDiesel.getText().toString();
			String precoEtanol = pEtanol.getText().toString();
			String adicionais = infadicionais.getText().toString();
			String idPosto=numPosto;
			String horarioDeFuncionamento=horarioFunc.getText().toString();
			
			// Parâmetros de construção
			Map<String,String> params = new HashMap<String, String>();
			
			params.put("idPosto", idPosto);
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
			JSONObject json = JSONParser.makeHttpRequest(url_dados_posto,
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
	
		protected void onPostExecute(String file_url) 
		{
	
			// dismiss the dialog once done
			pDialog1.dismiss();
	
			if (success == 1) 
			{
				Toast.makeText(getApplicationContext(), "Sucesso na atualização dos dados!",
						Toast.LENGTH_LONG).show();						
				abreTelaDoMapa();
			
			} else {
				// failed to create product
				Toast.makeText(getApplicationContext(), " Falha na atualização do posto",
						Toast.LENGTH_LONG).show();
				abreTelaMostraDados() ;
				
			}
			
	
		}
	}
	protected void abreTelaDoMapa() 
	{
		//Toast.makeText(getApplicationContext(), " Mapa", Toast.LENGTH_LONG).show();
		
		Intent trocaTela = new Intent(DadosPosto.this, GPSMapaActivity.class);

		startActivity(trocaTela);
	
		finish();
	}
	protected void abreTelaMostraDados() 
	{
		//Toast.makeText(getApplicationContext(), " Mapa", Toast.LENGTH_LONG).show();
		
		Intent trocaTela = new Intent(DadosPosto.this, DadosPosto.class);

		startActivity(trocaTela);
	
		finish();
	}

}