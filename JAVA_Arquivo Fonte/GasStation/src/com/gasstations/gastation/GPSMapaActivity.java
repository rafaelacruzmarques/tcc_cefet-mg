package com.gasstations.gastation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi")
public class GPSMapaActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback {

	

	private String url_busca_postos ="get_all_postos.php";
	
	private SupportMapFragment mapFrag;
	private GoogleMap map;
	private LatLng latlngCriacaoPosto;
	public int idPosto,recebeIdPosto;
	LatLng coordenadaLatLng;
	List<Posto> listaPostosVisiveis = new ArrayList<Posto>();

	GoogleMap gmap;
	private LocationManager locationManager;
	private boolean allowNetwork;
	private boolean buscando = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps__mapa);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment1);
		
		url_busca_postos =Servers.getServersInstancia(this).getServerEmUso().urlString+url_busca_postos;
		
		mapFragment.getMapAsync(this);

		// Getting a reference to the map
		gmap = mapFragment.getMap();

		// Setting a click event handler for the map
		gmap.setOnMapClickListener(new OnMapClickListener() {

			public void onMapClick(LatLng latLng) 
			{
				latlngCriacaoPosto = latLng;

				// CÓDIGO PARA CONFIRMAR INSERÇÃO DO POSTO
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(GPSMapaActivity.this);

				// Setting Dialog Title
				alertDialog.setTitle("GasStations-Adição de novo posto");

				// Setting Dialog Message
				alertDialog.setMessage("Deseja mesmo adicionar o novo posto...");

				// Setting Icon to Dialog
				// From:http://www.dojeitoquebrasileirogosta.com.br/wp-content/uploads/2014/10/abastecer-o-carro-nos-eua-o-que-fazere.jpg?ec5e74
				// alertDialog.setIcon(R.drawable.bomba);

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) 
					{

						// Write your code here to invoke YES event
						// Toast.makeText(getApplicationContext(),
						// "You clicked on YES",
						// Toast.LENGTH_SHORT).show();
						LatLng latLng = latlngCriacaoPosto;
						// Creating a marker
						MarkerOptions markerOptions = new MarkerOptions();

						// Setting the position for the marker
						markerOptions.position(latLng);
						//coordenadaLatLng=recebeCoordenada(latLng);
						
						Intent intent = new Intent(GPSMapaActivity.this,PostoActivity.class);
						String latitude =String.valueOf(latLng.latitude) ;
						String longitude =String.valueOf(latLng.longitude) ;
						intent.putExtra("latitude", latitude);
						intent.putExtra("longitude", longitude);
						startActivity(intent);
						// Clears the previously touched position
						//abrirTelaPosto();
						/************************************** Mudança **********************************************/
						// mensagem para aparecer o que o usuario digitou no
						// xml, com as id's precogasolina,adicionais

						// Animating to the touched position
						gmap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
						// setContentView(R.layout.cadastro_posto);
						// Placing a marker on the touched position

						// Setting the title for the marker.
						// This will be displayed on taping the marker
						// markerOptions.title(latLng.latitude + " : " +
						// latLng.longitude);
						// gmap.addMarker(markerOptions);

					}
				});

				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to invoke NO event
						// Toast.makeText(getApplicationContext(),
						// "You clicked on NO",
						// Toast.LENGTH_SHORT).show();
						dialog.cancel();
					}
				});

				// Showing Alert Message
				alertDialog.show();

				// FIM DO CÓDIGO PARA CONFIRMAR INSERÇÃO DO POSTO

			}
		});

		gmap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {
				buscaPostos();
			}
		});

		GoogleMapOptions options = new GoogleMapOptions();
		options.zOrderOnTop(true);
		mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment1);
		map = mapFrag.getMap();
		
		configMap();
	}
	public LatLng recebeCoordenada(LatLng latLng)
	{
		return latLng;
	}
	public LatLng getCoordenada()
	{
		return coordenadaLatLng;
	}
	@Override
	public void onResume() {
		super.onResume();

		allowNetwork = true;
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Intent it = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		} else {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	public void configMap() {
		map = mapFrag.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		LatLng latLng = new LatLng(-19.954884, -44.027308);
		
		configLocation(latLng);

	}

	public void configLocation(LatLng latLng) {
		CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(13).bearing(0).tilt(90)
				.build();
		CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
		map.setMyLocationEnabled(true);
		map.animateCamera(update);
		MyLocation myLocation = new MyLocation();
		map.setLocationSource(myLocation);
		myLocation.setLocation(latLng);
		buscaPostos();

	}

	public class MyLocation implements LocationSource {
		private OnLocationChangedListener listener;

		@Override
		public void activate(OnLocationChangedListener listener) {
			this.listener = listener;
			Log.i("Script", "activate()");

		}

		@Override
		public void deactivate() {
			Log.i("Script", "deactivate()");
		}

		public void setLocation(LatLng latLng)
		{
			Location location = new Location(LocationManager.GPS_PROVIDER);
			location.setLatitude(latLng.latitude);
			location.setLongitude(latLng.longitude);

			if (listener != null) {
				listener.onLocationChanged(location);
			}
		}
		
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
			allowNetwork = false;
		}
		if (allowNetwork
				|| location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
			configLocation(new LatLng(location.getLatitude(),
					location.getLongitude()));
		}


	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gps__mapa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {

		/*LatLng posto = new LatLng(-19.952336, -44.025455);
		googleMap.addMarker(new MarkerOptions().position(posto)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(true)
				.title("Posto Ipiranga:Informações").snippet("Preços,Serviços adicionais,Formas de pagamentos"));
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posto, 13));
		LatLng posto1 = new LatLng(-19.954041, -44.025788);
		googleMap.addMarker(new MarkerOptions().position(posto1)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).draggable(true)
				.title("Posto Vila Maria:Informações").snippet("Preços,Serviços adicionais,Formas de pagamentos"));
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posto1, 13));*/

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	private void abrirTelaPosto() 
	{
		Intent trocaTela = new Intent(GPSMapaActivity.this, Posto.class);
		startActivity(trocaTela);
	}

	private void buscaPostos() {
		LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;

		// fetchData(bounds);
		Log.d("Bounds", "{(" + bounds.northeast.latitude + "," + bounds.northeast.longitude + "),("
				+ bounds.southwest.latitude + "," + bounds.southwest.longitude + ")}");

		if (buscando == false) {
			buscando = true;
			new SelecaoDePostosNaArea(url_busca_postos).execute(bounds);
		}
	}

	/**
	 * Background Async Task to Create new product
	 */
	class SelecaoDePostosNaArea extends AsyncTask<LatLngBounds, String, String> {

		String url_busca_todos_postos = "";

		ProgressDialog pDialog = null;

		JSONObject jsonPostos = null;

		// JSON Node names
		private static final String TAG_SUCCESS = "success";
		int success = 0;

		public SelecaoDePostosNaArea(String Url) {
			url_busca_todos_postos = Url;

		}

		// Mostra dialogo

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			pDialog = new ProgressDialog(GPS_Mapa.this);
//			pDialog.setMessage("Buscando postos ...");
//			pDialog.setIndeterminate(false);
//			pDialog.setCancelable(true);
//			pDialog.show();
			
		}

		// Criando Login
		protected String doInBackground(LatLngBounds... bounds) {

			// String email = editlogin.getText().toString();
			// String senha = editsenha.getText().toString();

			String latNoroeste = Double.toString(bounds[0].northeast.latitude);
			String longNoroeste = Double.toString(bounds[0].northeast.longitude);
			String latSudeste = Double.toString(bounds[0].southwest.latitude);
			String longSudeste = Double.toString(bounds[0].southwest.longitude);

			// Parâmetros de construção
			Map<String, String> params = new HashMap<String, String>();
			params.put("latN", latNoroeste);
			params.put("logN", longNoroeste);
			params.put("latS", latSudeste);
			params.put("logS", longSudeste);

			// getting JSON Object
			// aceita método POST
			JSONObject json = JSONParser.makeHttpRequest(url_busca_todos_postos, "POST", params);
			// verifica o log cat de resposta
			Log.d("Create Response", json.toString());

			// verifica a variável sucess
			try {
				success = json.getInt(TAG_SUCCESS);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			jsonPostos = json;

			return null;
		}

		/**
		 * apresenta resultado após executar os comandos acima
		 **/
		protected void onPostExecute(String args) {

			// dismiss the dialog once done
			//pDialog.dismiss();
			
			if (success == 1) {
				//Toast.makeText(GPS_Mapa.this, " Postos retornaram corretamente.", Toast.LENGTH_LONG).show();
				converteJson2ListPostos(jsonPostos);

			} else {
				// failed to create product
				//Toast.makeText(GPS_Mapa.this, " Erro ao buscar postos.", Toast.LENGTH_LONG).show();
				converteJson2ListPostos(null);
			}

		}

	}

	public void converteJson2ListPostos(JSONObject json) 
	{
		Float coordLatid;
		Float coordLongit;
		String nomePosto;
		String bandeira ;
		String adicionais ;
		String formaDePagamento ;
		Float precoEtanol ;
		Float precoGasolina ;
		Float precoDiesel ;
		String horarioDeFuncionamento;
		

		if (json != null) 
		{
			Log.d("JSON", json.toString());

			try 
			{
				System.out.println("parsing data");
				JSONArray jArray = json.getJSONArray("Postos");

				Posto posto = null;
				if (jArray.length() > 0) 
				{
					listaPostosVisiveis.clear();
					gmap.clear();
					LatLng latLng = new LatLng(-19.954884, -44.027308);
					gmap.addMarker(new MarkerOptions()
			        .position(latLng)
			        .title("Hello world"));
				}
				
				Log.d("Postos Marks", "Qtd de postos: " + jArray.length());

				for (int i = 0; i < jArray.length(); i++) 
				{
					// System.out.println("ARRAY LENGTH " + jArray.length());
					JSONObject json_data = jArray.getJSONObject(i);

					posto = new Posto();
					/************************************************
					 * Mudança: Foi adicionado as outras variáveis para serem
					 * salvas na lista
					 *************/
					posto.id = json_data.getInt("id");
					posto.latitude = (float) (json_data.isNull("lat")?0:json_data.getDouble("lat"));
					posto.longitude = (float) (json_data.isNull("log")?0: json_data.getDouble("log"));
					posto.nome = (json_data.isNull("nome")?"Sem nome": json_data.getString("nome"));
					posto.horarioDeFuncionamento = (json_data.isNull("horarioDeFuncionamento")?"Não definido": json_data.getString("horarioDeFuncionamento"));
					posto.bandeira = (json_data.isNull("bandeira")?"Não definida": json_data.getString("bandeira"));
					posto.adicionais = (json_data.isNull("adicionais")?"Sem mais informações.":json_data.getString("adicionais"));
					posto.formaDePagamento = (json_data.isNull("formaDePagamento")?"Sem mais informações.":json_data.getString("formaDePagamento"));
					posto.precoEtanol = (float) (json_data.isNull("etanol")?0: json_data.getDouble("etanol"));					
					posto.precoGasolina = (float) (json_data.isNull("gasolina")?0 : json_data.getDouble("gasolina"));					
					posto.precoDiesel = (float) (json_data.isNull("diesel")?0 :json_data.getDouble("diesel"));
					
					
					//retornar o valor
					listaPostosVisiveis.add(posto);
					// Variaveis recebem os valores que vão ser salvos na lista
					coordLatid = posto.latitude;
					coordLongit = posto.longitude;
					nomePosto = posto.nome;
					bandeira = posto.bandeira;
					adicionais = posto.adicionais;
					formaDePagamento = posto.formaDePagamento;
					precoEtanol = posto.precoEtanol;
					precoGasolina = posto.precoGasolina;
					precoDiesel = posto.precoDiesel;
					horarioDeFuncionamento=posto.horarioDeFuncionamento;

					LatLng latLng = new LatLng(posto.latitude, posto.longitude);
					
					Log.d("PostosMark",latLng.latitude + " - " + latLng.longitude + " - " +  posto.nome);
					gmap.addMarker(new MarkerOptions().position(latLng)
							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
							.draggable(true).title(posto.nome));
					//.snippet("Preços,Serviços adicionais,Formas de pagamentos"));
					gmap.setOnMarkerClickListener(new OnMarkerClickListener() 
					{

						@Override
						public boolean onMarkerClick(Marker muda) 
						{
							final int i;

							String nome,bandeira,formpag,adicionais,horarioFunc;
							float pgasolina,petanol,pdiesel;
							int id;
							//if (muda.getTitle().equals(adicionais)) // if
							for ( i = 0; i < listaPostosVisiveis.size(); i++) 
							{

								if((listaPostosVisiveis.get(i).latitude!=0)&&(listaPostosVisiveis.get(i).longitude!=0))
								{
									id=listaPostosVisiveis.get(i).id;
									nome=listaPostosVisiveis.get(i).nome;
									bandeira=listaPostosVisiveis.get(i).bandeira;
									formpag=listaPostosVisiveis.get(i).formaDePagamento;
									adicionais=listaPostosVisiveis.get(i).adicionais;
									pgasolina=listaPostosVisiveis.get(i).precoGasolina;
									petanol=listaPostosVisiveis.get(i).precoEtanol;
									pdiesel=listaPostosVisiveis.get(i).precoDiesel;
									horarioFunc=listaPostosVisiveis.get(i).horarioDeFuncionamento;
									buscaPostos(muda.getPosition().latitude,muda.getPosition().longitude);

								}
								else
								{
									AlertDialog.Builder alertDialog = new AlertDialog.Builder(
											GPSMapaActivity.this);
									alertDialog.setMessage("Posto não encontrado!");
								}																				// marker
								// source
								// is
								// clicked
								//Toast.makeText(GPS_Mapa.this, muda.getTitle(),100).show();
								// trocaTela2();
								// display toast
								return false;
							}
							return allowNetwork;


						}
					});
				}


			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		buscando = false;
	}


	public void abrirTelaPegaDadosPosto() {

		Intent trocaTela = new Intent(GPSMapaActivity.this, DadosPosto.class);

		startActivity(trocaTela);

		finish();
	}
	public void buscaPostos(double lat,double longit )
	{

		Posto posto = new Posto();

		//String bandeira, formpag, adicionais;
		float pgasolina;
		float petanol, pdiesel;
		int id;
		for ( int i = 0; i < listaPostosVisiveis.size(); i++) 
		{

			if((listaPostosVisiveis.get(i).latitude==lat)&&(listaPostosVisiveis.get(i).longitude==longit))
			{
				id=listaPostosVisiveis.get(i).id;
				final String nome=listaPostosVisiveis.get(i).nome;
				final String bandeira=listaPostosVisiveis.get(i).bandeira;
				final String horarioDeFuncionamento=listaPostosVisiveis.get(i).horarioDeFuncionamento;
				final String formpag=listaPostosVisiveis.get(i).formaDePagamento;
				final String adicionais=listaPostosVisiveis.get(i).adicionais;
				pgasolina=listaPostosVisiveis.get(i).precoGasolina;
				petanol=listaPostosVisiveis.get(i).precoEtanol;
				pdiesel=listaPostosVisiveis.get(i).precoDiesel;
				final String idposto=String.valueOf(id);
				final String precoGasolina=String.valueOf(pgasolina);
				final String precoEtanol=String.valueOf(petanol);
				final String precoDiesel=String.valueOf(pdiesel);
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						GPSMapaActivity.this);
				alertDialog.setTitle("Posto "+idposto);
				alertDialog. 
				setMessage ("Nome do Posto: "+nome+"\nBandeira: "+bandeira+"\nGasolina($): "+pgasolina+"\nEtanol($): "+petanol+"\nDiesel($): "
						+pdiesel+"\nFormas de Pagamento: "+formpag+ "\nAdicionais: "+adicionais+"\nHorário de Funcionamento:"+horarioDeFuncionamento);

				alertDialog.setPositiveButton("Editar", 
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						Intent intentDados = new Intent(GPSMapaActivity.this,DadosPosto.class);
						String idPosto =idposto ;
						String nomePosto =nome ;
						String bandeiraPosto =bandeira ;
						String formPagamento=formpag;
						String infAdicionais=adicionais;
						String pGasolina=precoGasolina;
						String pEtanol=precoEtanol;
						String pDiesel=precoDiesel;
						String horarioFunc=horarioDeFuncionamento;
						intentDados.putExtra("idPosto", idPosto);
						intentDados.putExtra("nomePosto", nomePosto);
						intentDados.putExtra("bandeiraPosto", bandeiraPosto);
						intentDados.putExtra("formPagamento", formPagamento);
						intentDados.putExtra("infAdicionais", infAdicionais);
						intentDados.putExtra("pGasolina", pGasolina);
						intentDados.putExtra("pEtanol", pEtanol);
						intentDados.putExtra("pDiesel", pDiesel);
						intentDados.putExtra("horarioFunc", horarioFunc);
						startActivity(intentDados);
						
					}
				});
				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("Não",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						
						dialog.cancel();
						
					}
				});

				// Showing Alert Message
				alertDialog.show();
			}
			else
			{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						GPSMapaActivity.this);
				alertDialog.setMessage("Posto não encontrado!");
			}
		}

	}




}