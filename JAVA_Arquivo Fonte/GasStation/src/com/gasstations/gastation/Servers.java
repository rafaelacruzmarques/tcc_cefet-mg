package com.gasstations.gastation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class Servers {
	Context mContext;

	List<Servidor> lista = new ArrayList<Servidor>();

	static private Servidor serverEmUso;

	static private Servers singletonServers;

	private boolean servidoresAvaliados = false;

	public static Servers getServersInstancia(Context ctx) {
		if (singletonServers == null)
			singletonServers = new Servers(ctx);
		return singletonServers;
	}

	private Servers(Context ctx) {

		mContext = ctx;

		lista.add(new Servidor("Alisson",
				"http://alissonrs.dlinkddns.com/gasstations/php/", false));
		lista.add(new Servidor("helioHost",
				"http://gasstations.heliohost.org/php/", false));
		lista.add(new Servidor("MB16", "http://gasstations.16mb.com/php/",
				false));

		avaliaServidores();

	}

	public Servidor getServerEmUso() {	
		return serverEmUso;
	}

	private void avaliaServidores() {		
			new AvaliadorDeServer(mContext).execute(lista);			
	}

	class Servidor {

		String urlString;
		boolean isOnline;
		String nome;

		public Servidor(String nome, String urlString, boolean isOnline) {
			this.urlString = urlString;
			this.isOnline = isOnline;
			this.nome = nome;
		}

		@Override
		public String toString() {
			return urlString;
		}
	}

	class AvaliadorDeServer extends AsyncTask<List<Servidor>, Void, Boolean> {

		private Context mContext;

		public AvaliadorDeServer(Context context) {
			mContext = context;
		}

	
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			servidoresAvaliados = false;
			serverEmUso = null;
			Log.d("Gas-Servers", "Avaliando servidores");
		}

		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		protected Boolean doInBackground(List<Servidor> ... server) {
			boolean retorno = false;
			for (Servidor servidor : server[0]) {
				retorno = retorno || isURLReachable(mContext, servidor);
				if (retorno) {return retorno;}
			}
			return retorno;
		}

		public boolean isURLReachable(Context context, Servidor server) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				try {
					URL url = new URL(server.urlString); // Change to
															// "http://google.com"
															// for www test.
					HttpURLConnection urlc = (HttpURLConnection) url
							.openConnection();
					urlc.setConnectTimeout(2 * 1000); // 2 s.
					urlc.connect();
					if (urlc.getResponseCode() == 200) { // 200 = "OK" code
															// (http connection
															// is fine).
						Log.wtf("Connection", "Success ! " + server.nome);
						server.isOnline = true;
						return true;
					} else {
						Log.wtf("Connection", "Fail ! " + server.nome);
						server.isOnline = false;
						return false;
					}
				} catch (MalformedURLException e1) {
					return false;
				} catch (IOException e) {
					return false;
				}
			}
			return false;
		}

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */
		protected void onPostExecute(Boolean result) {
			for (Servidor servidor : lista) {
				if (servidor.isOnline) {
					serverEmUso = servidor;
					break;
				}
			}
			servidoresAvaliados = true;
			Log.d("Gas-Servers", "Servidor em uso: "
					+ (serverEmUso == null ? "Nenhum" : serverEmUso.nome));

		}
	}

}
