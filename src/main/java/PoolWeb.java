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


public class PoolWeb {
	
	private String json;
	private static int DNI_INICIO;
	private static int DNI_FIN;


	
	public PoolWeb() {
		DNI_INICIO = 1;
		DNI_FIN = 10;
	}

	private void generarPeticion(Integer dni) {
		URL url;
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
			json=json+"{"+atr_value_dni+","+atr_value_apenom+"},";		
		} 
		catch (IOException e) {
			//System.out.println("No existe DNI "+dni);
		}
	}

	public String obtenerClientesJSON() {
		List<Future> futureTaskList = new LinkedList<>();
		ExecutorService executor = Executors.newFixedThreadPool(100);

		for (int dni = DNI_INICIO; dni < DNI_FIN; dni++) {
			int dniCliente = dni;
			json=new String("");
			Runnable thread = new Runnable() {
				@Override
				public void run() {
					generarPeticion(dniCliente);
				}
			};
			FutureTask<String> task = new FutureTask(thread, "thread done");
			futureTaskList.add(executor.submit(task));
		}
		boolean allTerminated = futureTaskList.parallelStream().allMatch(t -> t.isDone());
		while (!allTerminated) {
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
