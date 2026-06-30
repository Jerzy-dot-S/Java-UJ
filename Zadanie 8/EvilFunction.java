import java.util.function.Function;

public class EvilFunction implements Function<Double, Double> {
    @Override
    public Double apply(Double arg0) {
        return Math.cos(
            Math.pow(arg0 + 3.0, Math.abs(arg0) + 0.1)
            * Math.sqrt(1.0 + Math.sqrt(arg0 + 2.0))
            * Math.exp(Math.sin(arg0)) * arg0
            + Math.exp(-(3.0 - arg0) * (3.0 - arg0))
        );
    }
}