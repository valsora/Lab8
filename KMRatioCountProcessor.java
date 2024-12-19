import java.util.List;
import java.util.stream.Collectors;

@DataProcessor
public class KMRatioCountProcessor implements ProcessorInterface {
    @Override
    @DataProcessor
    public List<Clone> process(List<Clone> data) {
        return data.stream()
            .map(clone -> {
                double KMRatio = (double)clone.getKills() / clone.getMissions();
                return new Clone(clone.getId(), clone.getAka(), clone.getUnit(), clone.getKills(), clone.getMissions(), clone.getRank(), KMRatio);
            })
            .collect(Collectors.toList());
    } 
}
