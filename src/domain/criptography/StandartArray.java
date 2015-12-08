package domain.criptography;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by ignas on 15.12.5.
 */
public class StandartArray {
    List<Sindrome> sindromes = new ArrayList<>();

    public void addSindromes(Sindrome sindrome){
        this.sindromes.add(sindrome);
    }

    public List<Sindrome> getSindromes() {
        return sindromes;
    }

    public Integer getSindromeWeight(BinaryWord sindromeValue){
        Optional<Sindrome> sindromeMatch = sindromes.stream()
                                                    .filter(sindrome -> sindrome.getValue().equalsTo(sindromeValue))
                                                    .findFirst();

        if (!sindromeMatch.isPresent()){
            throw new RuntimeException("Standart Array fialure");
        }
        return sindromeMatch.get().getWeight();
    }
}
