import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PoolWeb {
	
	private String json;
	private static Integer DNI_INICIO;
	private static Integer DNI_FIN;
	private Integer dni;


	
	public PoolWeb() {
		DNI_INICIO = 1;
		DNI_FIN = 1000;
	}
	
	private boolean isJSONValid(String test) {
	    try {
	        new JSONObject(test);
	    } 
	    catch (JSONException ex) {
	        try {
	            new JSONArray(test);
	        } 
	        catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}

	private String obtenerClienteJSON(Integer dni) {
		URL url;
		String currentJSON=new String("");
		try {
			url = new URL("http://centro-estetica.ddns.net/api/v1/pacientes/activo?dni="+dni);
			URLConnection con = url.openConnection();
			BufferedReader resultado = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String linea;
			String str=new String();
			while ((linea = resultado.readLine()) != null) {
				str=new String(linea);
			}
			String atr_value_dni="\"dni\":\""+dni.toString()+"\"";
			String atr_value_apenom=str.split(",")[1].replaceAll("}", "");
			currentJSON = "{"+atr_value_dni+","+atr_value_apenom+"}";
			System.out.println(currentJSON);	
		} 
		catch (IOException e) {
			System.out.println("No existe DNI "+dni);
		}
		return currentJSON;
	}

	public String obtenerClientesJSON() {
		List<Future> futureTaskList = new LinkedList<>();
		ExecutorService executor = Executors.newFixedThreadPool(100);

		for (dni = DNI_INICIO; dni.compareTo(DNI_FIN)<=0; dni++) {
			int dniCliente = dni;
			json=new String("");
			Runnable thread = new Runnable() {
				@Override
				public void run() {
					String currentJSON = obtenerClienteJSON(dniCliente);
					if(isJSONValid(currentJSON)) {
						json=json+currentJSON+",";
					}
					else {
						if(!currentJSON.contains("\"}")&&(currentJSON.contains("}"))) {
							currentJSON=currentJSON.replaceAll("}", "\"}");
							json=json+currentJSON+",";
						}
					}
				}
			};
			FutureTask<String> task = new FutureTask(thread, "thread done");
			futureTaskList.add(executor.submit(task));
		}
		boolean allTerminated = futureTaskList.parallelStream().allMatch(t -> t.isDone());
		while (!allTerminated) {
			System.out.println("En proceso concurrente..");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			allTerminated = futureTaskList.parallelStream().allMatch(t -> t.isDone());
		}
		json = "{\"clientes\":["+json+"]}";
		json=json.replace(",]", "]");
		System.out.println("--------------------> FIN <-------------------");
		return json;
	}
}
