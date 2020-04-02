import java.io.File;
import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;



public class GestorExcel {
	
	public void jsonToExcel(String json) {
		JSONObject output;
		try {
		   output = new JSONObject(json);
		   JSONArray docs = output.getJSONArray("clientes");
		   File file = new File("./target/Clientes.csv");
		   String csv = CDL.toString(docs);
		   FileUtils.writeStringToFile(file, csv, "UTF-8");
		} 
		catch(Exception e) {
		   e.printStackTrace();
		}
	}

}
