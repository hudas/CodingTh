package main.domain.criptography;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Esybe atspindinti standartine kodo lentele
 */
public class StandartArray {
    // Sindromu lenteleje sarasas su svoriais
    List<Sindrome> sindromes = new ArrayList<>();

    /**
     * Prideda nauja sindroma i sarasa
     * @param sindrome
     */
    public void addSindromes(Sindrome sindrome){
        this.sindromes.add(sindrome);
    }

    /**
     * Grazina sindromu esnaciu lenteleje sarasa
     * @return
     */
    public List<Sindrome> getSindromes() {
        return sindromes;
    }

    /**
     * Grazoma sindromo klases lyderio svori
     * @param sindromeValue
     * @return
     */
    public Integer getSindromeWeight(BinaryWord sindromeValue){
        Optional<Sindrome> sindromeMatch = sindromes.stream()
                                                    .filter(sindrome -> sindrome.getValue().equalsTo(sindromeValue))
                                                    .findFirst();

        // Jei lenteleje nerandamas sindromas su nurodyta reiksme - Keliama klaida - netinkamai sudaryta standartine lentele
        if (!sindromeMatch.isPresent()){
            throw new RuntimeException("Standart Array fialure");
        }
        return sindromeMatch.get().getWeight();
    }
}
