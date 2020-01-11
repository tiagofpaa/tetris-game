package histogram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcellHandler {

    private static final String FILE_NAME = "data/Simulation ANALYZER.xlsx";
    private static String desktop_path = System.getProperty("user.home") + "/Desktop";

    public static void main(String[] args) throws IOException {
    	
    	double[] sim_of_bonustime = Simulator.simulate_bonustime();
    	double[] sim_of_increaseSpeed  = Simulator.simulate_increaseSpeed();
    	int[] sim_of_randomRotation = Simulator.simulate_randomRotation();
    	int[] sim_of_RandomPiece = Simulator.simulate_generateRandomPiece();
    	int[] sim_of_RandomPosition = Simulator.simulate_randomPosition();
    	
    	
    	FileInputStream fis = new FileInputStream(new File(FILE_NAME));
    	XSSFWorkbook workbook = new XSSFWorkbook(fis);
    	XSSFSheet bonus_time_sheet = workbook.getSheetAt(0);
        XSSFSheet increase_speed_sheet = workbook.getSheetAt(1);
        XSSFSheet rotation_sheet = workbook.getSheetAt(2);
        XSSFSheet random_piece_sheet = workbook.getSheetAt(3);
        XSSFSheet position_sheet = workbook.getSheetAt(4);
        
    	Object[][] bonus_time_datatypes = new Object[10000][1];
    	Object[][] increase_speed_datatypes = new Object[10000][1];
    	Object[][] rotation_datatypes = new Object[10000][1];
    	Object[][] random_piece_datatypes = new Object[10000][1];
    	Object[][] position_datatypes = new Object[10000][1];
   
      
        for(int i = 0; i < 10000; i++){
        	bonus_time_datatypes[i][0] = sim_of_bonustime[i];
        }
        
       
        for(int i = 0; i < 10000; i++){
        	increase_speed_datatypes[i][0] = sim_of_increaseSpeed[i];
        }
        
        
        for(int i = 0; i < 10000; i++){
        	rotation_datatypes[i][0] = sim_of_randomRotation[i];
        }
        
        
        for(int i = 0; i < 10000; i++){
        	random_piece_datatypes[i][0] = sim_of_RandomPiece[i];
        }
        
        
        for(int i = 0; i < 10000; i++){
        	position_datatypes[i][0] = sim_of_RandomPosition[i];
        }
        
        
        
        System.out.println("Generating new Simulation Sheet");
        
    
        
       int rowNum = 0;
        for(Object[] datatype : bonus_time_datatypes){
        	Row row = bonus_time_sheet.getRow(rowNum++);
        	for(Object field : datatype){
        		Cell cell = row.createCell(0);
        		if(cell != null){
        			if(field instanceof String){
        				cell.setCellValue((String) field);
        			}else if(field instanceof Integer){
        				cell.setCellValue((Integer) field);
        			}else if(field instanceof Double){
        				cell.setCellValue((Double) field);
        			}
        		}
        	}
        }
       
        
        rowNum = 0;
        for(Object[] datatype : increase_speed_datatypes){
        	Row row = increase_speed_sheet.getRow(rowNum++);
        	for(Object field : datatype){
        		Cell cell = row.createCell(0);
        		if(cell != null){
        			if(field instanceof String){
        				cell.setCellValue((String) field);
        			}else if(field instanceof Integer){
        				cell.setCellValue((Integer) field);
        			}else if(field instanceof Double){
        				cell.setCellValue((Double) field);
        			}
        		}
        	}
        }


        rowNum = 0;
        for(Object[] datatype : rotation_datatypes){
        	Row row = rotation_sheet.getRow(rowNum++);
        	for(Object field : datatype){
        		Cell cell = row.createCell(0);
        		if(cell != null){
        			if(field instanceof String){
        				cell.setCellValue((String) field);
        			}else if(field instanceof Integer){
        				cell.setCellValue((Integer) field);
        			}else if(field instanceof Double){
        				cell.setCellValue((Double) field);
        			}
        		}
        	}
        }
        
        
        
        rowNum = 0;
        for(Object[] datatype : random_piece_datatypes){
        	Row row = random_piece_sheet.getRow(rowNum++);
        	for(Object field : datatype){
        		Cell cell = row.createCell(0);
        		if(cell != null){
        			if(field instanceof String){
        				cell.setCellValue((String) field);
        			}else if(field instanceof Integer){
        				cell.setCellValue((Integer) field);
        			}else if(field instanceof Double){
        				cell.setCellValue((Double) field);
        			}
        		}
        	}
        }
        
        
        
        rowNum = 0;
        for(Object[] datatype : position_datatypes){
        	Row row = position_sheet.getRow(rowNum++);
        	for(Object field : datatype){
        		Cell cell = row.createCell(0);
        		if(cell != null){
        			if(field instanceof String){
        				cell.setCellValue((String) field);
        			}else if(field instanceof Integer){
        				cell.setCellValue((Integer) field);
        			}else if(field instanceof Double){
        				cell.setCellValue((Double) field);
        			}
        		}
        	}
        }
        
       

        try {
        	String name = "/Simulation (" + LocalDateTime.now().getDayOfMonth() + "-" + LocalDateTime.now().getMonthValue() + "-"
        	+ LocalDateTime.now().getYear() + " " + LocalDateTime.now().getHour() + "-" +LocalDateTime.now().getMinute()
            + "-" +LocalDateTime.now().getSecond() + ").xlsx";
        	desktop_path+=name;
            FileOutputStream outputStream = new FileOutputStream(desktop_path);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }
}