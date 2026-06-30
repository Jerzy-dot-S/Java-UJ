import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.w3c.dom.ranges.Range;

public class RectangularIntegration extends NumericalIntegration {

    @Override
    public setFunction(Function f) {

        return 1;
    }

    @Override
    public double integrate(Range range, int subintervals) {
        double start = range.getLower();
        double end = range.getUpper();

        if (start == end) {
            return 0.0;
        }

        if (subintervals <= 0) {
            throw new IllegalArgumentException("Subintervals nie mogą być mniejsze bądź równe 0");
        }

        double intervalsLength = (end - start) / subintervals;

        Set<Range> exclusions = function.domainExclusions();

        return 1;
    }

    public interface Range {
        double getLower();
        double getUpper();
        default boolean contains(double x) { 
            return x >= getLower() && x <= getUpper(); 
        }
    }

    private List<Range> mergeRanges(Collection<Range> exclusions) {
        if (exclusions == null || exclusions.isEmpty()) {
            return new ArrayList<>();
        }

        List<Range> list = new ArrayList<>(exclusions);
        list.sort(Comparator.com)
    }
    
}
