package com.gasstations.gastation;
//A Classe GaStations 

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

@SuppressLint("NewApi")
public class GaStations extends Activity {

	int countVerificaServidor = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ga_stations);

		VerificaServidores();
	
		

	}

	private boolean VerificaServidores() {

		if (Servers.getServersInstancia(this).getServerEmUso() == null) {
			// Aguarda
			if ((countVerificaServidor++) >= 5) {
				alertaServidorOff();
			}else{
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Log.d("Server", "Aguardando");
					VerificaServidores();
				}
			}, 2000);
			}
			
			return false;
		} else {
			carragarTelaUsuarios();
			return true;
		}

	}

	private void alertaServidorOff() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage("Nenhum servidor encontrado. Deseja Aguardar ?.");

		alertDialogBuilder.setPositiveButton("Sim",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						countVerificaServidor = 0;
						VerificaServidores();
						
					}
				});

		alertDialogBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void carragarTelaUsuarios() {

		new Timer().schedule(new TimerTask() {
			public void run() {
				finish();

				Intent intent = new Intent();
				intent.setClass(GaStations.this, UsuarioActivity.class);
				startActivity(intent);
			}
		}, 4000);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ga_stations, menu);
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

}
