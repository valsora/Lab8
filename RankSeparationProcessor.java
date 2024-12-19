import java.util.List;
import java.util.stream.Collectors;

@DataProcessor
public class RankSeparationProcessor implements ProcessorInterface {
    @Override
    @DataProcessor
    public List<Clone> process(List<Clone> data) {
        return data.stream()
            .map(clone -> {
                String name = clone.getAka().substring(clone.getAka().lastIndexOf(" ") + 1);
                String rank = clone.getAka().substring(0, clone.getAka().lastIndexOf(" "));
                return new Clone(clone.getId(), name, clone.getUnit(), clone.getKills(), clone.getMissions(), rank, -1.0);
            })
            .collect(Collectors.toList());
    } 
}
