import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@DataProcessor
public class ValuesFilterProcessor<T> implements ProcessorInterface {
    private Function<Clone, T> getter;
    private List<T> values;

    public ValuesFilterProcessor(Function<Clone, T> getter, List<T> values) {
        this.getter = getter;
        this.values = values;
    }

    @Override
    @DataProcessor
    public List<Clone> process(List<Clone> data) {
        return data.stream()
            .filter(clone -> {
                for (T value : values) {
                    if (getter.apply(clone) != null && getter.apply(clone).equals(value)) return true;
                }
                return false;
            })
            .collect(Collectors.toList());
    }
}
