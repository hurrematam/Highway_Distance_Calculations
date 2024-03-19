import java.util.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


public class CustomizedSheet {
    private final ArrayList<String> city_names = new ArrayList<>(81);
    private final int[][] city_distances = new int[81][81];
    public CustomizedSheet(Sheet sheet) {
        derive_city_names_and_distances(sheet);
    }

    private void derive_city_names_and_distances(Sheet sheet){

        for (int r = 2; r < 83; r++) {

            Row row = sheet.getRow(r);
            this.city_names.add(row.getCell(1).getStringCellValue());

            for (int c = 2; c < 83; c++) {

                Cell cell = row.getCell(c);

                switch (cell.getCellType()) {
                    case NUMERIC:
                        this.city_distances[r-2][c-2] = (int) cell.getNumericCellValue();
                        break;
                    case BLANK:
                        this.city_distances[r-2][c-2] = 0;
                        break;
                }

            }

        }

    }

    public void find_reachable_cities(String city_name, int distance){

        find_reachable_cities(this.city_names.indexOf(city_name), distance);

    }

    public void find_reachable_cities(int city_index, int distance){

        LinkedHashMap<String, Integer> reachable_cities = new LinkedHashMap<>();
        int[] distances = this.city_distances[city_index];

        for (int c = 0; c < 81; c++) {

            if (city_index == c){
                continue;
            }

            if (distance >= distances[c]){
                reachable_cities.put(this.city_names.get(c), distances[c]);
            }

        }

        System.out.println("Number of city that is closer to given location than given distance: " + reachable_cities.size());

        for (Map.Entry<String, Integer> entry: reachable_cities.entrySet()){

            System.out.println("City: " + entry.getKey() + ", Distance: " + entry.getValue());

        }
    }

    public void find_two_nearest_and_furthest_cities(){

        TreeMap<Integer, List<String>> distance_tree = new TreeMap<>();

        for (int row = 0; row < 81; row++) {

            for (int column = 0; column < 81; column++) {

                if (row == column) {
                    continue;
                }

                int distance = this.city_distances[row][column];
                List<String> cities = List.of(this.city_names.get(row), this.city_names.get(column));
                distance_tree.put(distance, cities);

            }

        }

        Map.Entry<Integer, List<String>> nearest = distance_tree.firstEntry();
        Map.Entry<Integer, List<String>> furthest = distance_tree.lastEntry();
        System.out.println("Two Nearest Cities: " + nearest.getValue() + ", Distance: " + nearest.getKey());
        System.out.println("Two Furthest Cities: " + furthest.getValue() + ", Distance: " + furthest.getKey());

    }

    public void find_max_visitable_city_count(String city_name, int distance){

        find_max_visitable_city_count(this.city_names.indexOf(city_name), distance);

    }

    public void find_max_visitable_city_count(int city_index, int distance){

        Queue<Path> unfinished_paths = new LinkedList<>();
        Queue<Path> finished_paths = new LinkedList<>();
        Path root = new Path(0, new ArrayList<>(List.of(city_index)));
        unfinished_paths.add(root);

        while (!unfinished_paths.isEmpty()){

            boolean is_path_finished = true;
            Path parent_path = unfinished_paths.remove();
            int current_city = parent_path.getCurrentCity();
            int[] distances = this.city_distances[current_city];

            for (int c = 0; c < 81; c++) {

                if (current_city == c){
                    continue;
                }

                if ((distance - parent_path.distance_covered) >= distances[c]) {
                    is_path_finished = false;
                    int distance_covered = parent_path.distance_covered + distances[c];
                    List<Integer> roadmap = new ArrayList<>(parent_path.roadmap);
                    roadmap.add(c);
                    Path child_path = new Path(distance_covered, roadmap);
                    unfinished_paths.add(child_path);
                }

            }

            if (is_path_finished) {
                finished_paths.add(parent_path);
            }

        }

        Path result = Collections.max(finished_paths);
        System.out.println("distance covered: " + result.distance_covered);
        System.out.println("Visited cities: " + result.roadmap.stream().map(this.city_names::get).toList());

    }

    public void print_distances_between_random_cities(){

        Random rand = new Random();
        HashSet<Integer> set = new HashSet<Integer>();

        while(set.size() < 5) {
            int num = rand.nextInt(82);
            set.add(num);
        }

        int[] random_cities = set.stream().mapToInt(Integer::intValue).toArray();
        int[][] matrix = new int[5][5];

        for (int row = 0; row < 5; row++) {

            for (int column = 0; column < 5; column++) {

                matrix[row][column] = this.city_distances[random_cities[row]][random_cities[column]];

            }

        }

        System.out.printf(
                " %38s  %19s  %19s  %19s  %19s\n",
                this.city_names.get(random_cities[0]),
                this.city_names.get(random_cities[1]),
                this.city_names.get(random_cities[2]),
                this.city_names.get(random_cities[3]),
                this.city_names.get(random_cities[4])
        );

        for (int row = 0; row < 5; row++) {

            System.out.printf("%19s", this.city_names.get(random_cities[row]));

            for (int column = 0; column < 5; column++) {

                System.out.printf(" %19d ", matrix[row][column]);

            }

            System.out.println();

        }

    }

}
