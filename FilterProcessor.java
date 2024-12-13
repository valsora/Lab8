import java.util.List;
import java.util.stream.Collectors;

@DataProcessor
public class FilterProcessor implements ProcessorInterface {
    @Override
    @DataProcessor
    public List<String> process(List<String> data) {
        return data.stream().filter(string -> string.length() >= 3).collect(Collectors.toList());
    } 
}
