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
	
	private static GestorExcel gestorExcel;
	private static String[][] info;
	private static int fila;

	public static void generarPeticion(int dni) {
		

		URL url;
		try {
			// Creando un objeto URL
			url = new URL("http://centro-estetica.ddns.net/api/v1/pacientes/activo?dni="+dni);

			// Realizando la petición GET
			URLConnection con = url.openConnection();

			// Leyendo el resultado
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			
			String linea;
			String str=new String();
			while ((linea = in.readLine()) != null) {
				str=new String(linea);
			}
			info[fila][0]=(new Integer(dni)).toString();
			String apenom=str.split(",")[1].replace("}", "");
			apenom=apenom.replace("nombre", "").replace(":", "").replace("\"", "");
			info[fila][1]=apenom;
			System.out.println(dni+" DNI encontrado "+apenom);
			fila++;
		} 
		catch (IOException e) {
			System.out.println("No existe DNI "+dni);
		}
	}

	public static void main(String[] args) {
		gestorExcel = new GestorExcel();
		gestorExcel.crearArchivo();
		info = new String[10][2];
		fila=0;
		List<Future> futureTaskList = new LinkedList<>();
		ExecutorService executor = Executors.newFixedThreadPool(300);

		for(int dni=1;dni<10;dni++) {
			final int finalDni = dni;
			int finalDni1 = dni;
			Runnable thread = new Runnable() {
				@Override
				public void run() {
					generarPeticion(finalDni1);
				}
			};
			FutureTask<String> task = new FutureTask(thread, "thread done");
			futureTaskList.add(executor.submit(task));

		}

		
		gestorExcel.escribirArchivo(info);
	}
	
	
	}