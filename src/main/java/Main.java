
public class Main {

	public static void main(String[] args) {
		PoolWeb poolWeb = new PoolWeb();
		String json = poolWeb.obtenerClientesJSON();
		GestorExcel gestorExcel = new GestorExcel();
		gestorExcel.jsonToExcel(json);

	}

}
