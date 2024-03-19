import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Main {

    public static Sheet read_sheet(String file_path){
        try (InputStream inp = new FileInputStream(file_path)) {
            Workbook wb = WorkbookFactory.create(inp);
            return wb.getSheetAt(0);
        }catch(Exception e) {
            System.out.println(e);
            return null;
        }
    }


    public static void main(String[] args) {

        Sheet sheet = read_sheet("ilmesafe.xls");

        if (sheet == null){
            System.out.println("File couldn't read!");
            return;
        }

        CustomizedSheet custom_sheet = new CustomizedSheet(sheet);

        custom_sheet.find_reachable_cities("İZMİR", 590);
        System.out.println("*****************************************************************************************");
        custom_sheet.find_max_visitable_city_count("İZMİR", 590);
        System.out.println("*****************************************************************************************");
        custom_sheet.find_two_nearest_and_furthest_cities();
        System.out.println("*****************************************************************************************");
        custom_sheet.print_distances_between_random_cities();
    }
}