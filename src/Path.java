import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Path implements Comparable<Path>{
    public int distance_covered;
    public List<Integer> roadmap;


    public Path(int distance_covered, List<Integer> roadmap) {
        this.distance_covered = distance_covered;
        this.roadmap = roadmap;
    }

    public int getCurrentCity(){
        if (this.roadmap.isEmpty()) {
            return -1;
        }
        return this.roadmap.get(this.roadmap.size()-1);
    }

    public int getVisitedCityCount() {
        Set<Integer> set = new HashSet<>(this.roadmap);
        return set.size();
    }

    @Override
    public int compareTo(Path o) {
        if (this.getVisitedCityCount() != o.getVisitedCityCount()) {
            return this.getVisitedCityCount() - o.getVisitedCityCount();
        } else {
            return o.distance_covered - this.distance_covered;
        }
    }
}