import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@DataProcessor
public class ComparisonFilterProcessor implements ProcessorInterface {
    private Function<Clone, Number> getter;
    private Number comparisonValue;
    private boolean isLowerThan;
    private boolean isStrictly;

    public ComparisonFilterProcessor(Function<Clone, Number> getter, Number comparisonValue, boolean isLowerThan, boolean isStrictly) {
        this.getter = getter;
        this.comparisonValue = comparisonValue;
        this.isLowerThan = isLowerThan;
        this.isStrictly = isStrictly;
    }

    @Override
    @DataProcessor
    public List<Clone> process(List<Clone> data) {
        return data.stream()
            .filter(clone -> {
                double cloneDouble = getter.apply(clone).doubleValue();
                double comparisonDouble = comparisonValue.doubleValue();
                if (!isLowerThan) {
                    if (isStrictly) return cloneDouble > comparisonDouble;
                    else return cloneDouble >= comparisonDouble;
                }
                else {
                    if (isStrictly) return cloneDouble < comparisonDouble;
                    else return cloneDouble <= comparisonDouble;
                }
            })
            .collect(Collectors.toList());
    } 
}
