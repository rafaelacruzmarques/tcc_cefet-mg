package com.gasstations.gastation;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
 
public class JSONParser 
{
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    // construtor
    public JSONParser() 
    {
 
    }
    //função que obtem JSON do URL
    public static JSONObject makeHttpRequest2(String url, String method,List<NameValuePair> params) 
    {
    	// Fazer pedido HTTP
        try 
        {
        	// verifica a variável method
            if(method == "POST")
            {
            	
            	 Log.d("DEBUG", "Marca 1");	
            	HttpParams httpParameters = new BasicHttpParams();
     			HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
     			HttpConnectionParams.setSoTimeout(httpParameters, 3000);
    			
    			// create HttpClient
    			HttpClient httpClient = new DefaultHttpClient(httpParameters);
    			          	            	
          	
                // se o metodo for do tipo POST
                HttpPost httpPost = new HttpPost(url);
                
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                Log.d("DEBUG", "Marca 2");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                Log.d("DEBUG", "Marca 3");
                HttpEntity httpEntity = httpResponse.getEntity();
                
                is = httpEntity.getContent();
 
            }else if(method == "GET")
            {
                // se o método for do tipo GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
 
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }           
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        try 
        {
        	//leitura de um fluxo de caracteres de entrada
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;            
            while ((line = reader.readLine()) != null) 
            {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.d("DEBUG", json);
        } catch (Exception e) 
        {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // analisa uma cadeia para um objeto JSON
        try 
        {
            jObj = new JSONObject(json);
        } catch (JSONException e) 
        {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        //retorna a string JSON
        return jObj;
 
    }
    
    
    
    /*
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     *      * 
     * 
     */
    
    
    
    public static JSONObject makeHttpRequest(String strUrl, String method, Map<String,String> dataToSend ) 
    {
    	  
    	
    	    //Server Communication part - it's relatively long but uses standard methods

    	    //Encoded String - we will have to encode string by our custom method (Very easy)
    	    String encodedStr = getEncodedData(dataToSend);

    	    Log.d("JSONParser", encodedStr);
    	    
    	    //Will be used if we want to read some data from server
    	    BufferedReader reader = null;

    	    //Connection Handling
    	    try {
    	        //Converting address String to URL
    	        URL url = new URL(strUrl);
    	        //Opening the connection (Not setting or using CONNECTION_TIMEOUT)
    	        Log.d("JSONParser", "Marca 2 " + strUrl);
    	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	        
    	        /*
    	        con.setReadTimeout(10000);
    	        con.setConnectTimeout(15000);
    	        con.setRequestMethod("POST");
    	        con.setDoInput(true);
    	        con.setDoOutput(true);
    	         */
    	        
    	        //Post Method
    	        con.setRequestMethod("POST");
    	        //To enable inputting values using POST method 
    	        //(Basically, after this we can write the dataToSend to the body of POST method)
    	        con.setDoOutput(true);
    	        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
    	        //Writing dataToSend to outputstreamwriter
    	        writer.write(encodedStr);
    	        //Sending the data to the server - This much is enough to send data to server
    	        //But to read the response of the server, you will have to implement the procedure below
    	        writer.flush();

    	        //Data Read Procedure - Basically reading the data comming line by line
    	        StringBuilder sb = new StringBuilder();
    	        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

    	        String line;
    	        while((line = reader.readLine()) != null) { //Read till there is something available
    	            sb.append(line + "\n");     //Reading and saving line by line - not all at once
    	        }
    	        line = sb.toString();           //Saving complete data received in string, you can do it differently

    	            	        
    	        //Just check to the values received in Logcat
    	        Log.i("custom_check","The values received in the store part are as follows:");
    	        Log.i("custom_check",line);
    	        
    	        jObj  = cvJson(line);
    	        

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    } finally {
    	        if(reader != null) {
    	            try {
    	                reader.close();     //Closing the 
    	            } catch (IOException e) {
    	                e.printStackTrace();
    	            }
    	        }
    	    }

    	  return jObj;
 
    }
    private static String getEncodedData(Map<String,String> data) {
        StringBuilder sb = new StringBuilder();
        for(String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if(sb.length()>0)
                sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
    }
    private static JSONObject cvJson(String sb){
    	
        json = sb;


    // analisa uma cadeia para um objeto JSON
    try 
    {
        jObj = new JSONObject(json);
    } catch (JSONException e) 
    {
        Log.e("JSON Parser", "Error parsing data " + e.toString());
    }

    //retorna a string JSON
    return jObj;
    }

    
    
    
    
    
    
    
    
}