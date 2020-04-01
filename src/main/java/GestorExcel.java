import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GestorExcel {
	
	private XSSFWorkbook libro;
	private FileOutputStream fileOuS;
	private String [] header;
	private CellStyle style;
	private File file;
	int fila;

	public GestorExcel() {
		header= new String[]{"DNI", "Apellido y nombre"};
		libro= new XSSFWorkbook();
		//Estilo para la cabecera
		style = libro.createCellStyle();
        Font font = libro.createFont();
        font.setBold(true);
        style.setFont(font);
        fila=0;
	}
	
	public void escribirArchivo(String[][] document) {//Escribir en hoja1 de excel
		try {
			fila++;
			String hoja="Hoja1";			
			XSSFSheet hoja1 = libro.createSheet(hoja);
			for(fila = 0;fila<=document.length;fila++) {
				XSSFRow row=hoja1.createRow(fila);//se crea las filas
				for (int i = 0; i <header.length; i++) {
					if (fila==0) {//para la cabecera
						XSSFCell cell= row.createCell(i);//se crea las celdas para la cabecera, junto con la posición
						cell.setCellStyle(style); // se añade el style crea anteriormente
						cell.setCellValue(header[i]);//se añade el contenido
					}
					else{//para el contenido
						XSSFCell cell= row.createCell(i);//se crea las celdas para la contenido, junto con la posición
						if(document[fila-1][i]!=null) {
							cell.setCellValue(document[fila-1][i]); //se añade el contenido
						}
					}
				}
				if(fila>0 && document[fila-1][0]!=null) {
					System.out.println(document[fila-1][0]+","+document[fila-1][1]);
				}
			}
			libro.write(fileOuS);
			fileOuS.flush();
			fileOuS.close();

		
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void escribirArchivo(String[] document, boolean isHeader) {//Escribir en hoja1 de excel
		try {
			fila++;
			fileOuS = new FileOutputStream(file);
			String hoja="Hoja1";
			libro= new XSSFWorkbook();
			XSSFSheet hoja1 = libro.createSheet(hoja);
			XSSFRow row=hoja1.createRow(fila);//se crea las filas
			for (int i = 0; i <header.length; i++) {
				if (isHeader) {//para la cabecera
					XSSFCell cell= row.createCell(i);//se crea las celdas para la cabecera, junto con la posición
					//cell.setCellStyle(style); // se añade el style crea anteriormente
					cell.setCellValue(header[i]);//se añade el contenido
				}
				else{//para el contenido
					XSSFCell cell= row.createCell(i);//se crea las celdas para la contenido, junto con la posición
					cell.setCellValue(document[i]); //se añade el contenido
				}
			}
			libro.write(fileOuS);
			fileOuS.flush();
			fileOuS.close();
		
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	public void crearArchivo() {
		String nombreArchivo="Clientes.xlsx";
		String rutaArchivo= "./"+nombreArchivo;
		file = new File(rutaArchivo);
		if (file.exists()) {
			file.delete();
		}
		try {
			fileOuS = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
