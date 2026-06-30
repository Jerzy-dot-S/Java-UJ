import java.util.*;

public class RunMatcher {
    public static void main(String[] args) {
        HistogramPatternMatcher matcher = new HistogramPatternMatcher();
        int[] inputs = {5,3,4,2,5,5,4,-1,-2,2,3,4};
        for (int v : inputs) matcher.data(v);
        System.out.println("histogram() -> " + matcher.histogram());

        List<Integer> p1 = Arrays.asList(1,1);
        System.out.println("match([1,1]) -> " + matcher.match(p1));

        List<Integer> p2 = Arrays.asList(1,1,2);
        System.out.println("match([1,1,2]) -> " + matcher.match(p2));
    }
}