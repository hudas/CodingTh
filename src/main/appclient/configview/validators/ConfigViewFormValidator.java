package main.appclient.configview.validators;

import main.appclient.configview.viewforms.ConfigViewForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Klasė atsakinga už kodo konfigūracijos formos validaciją
 * Užtikrina, kad vartotojas įvestų tinkamus duomenis į konfigūracijos formą.
 * 1. Triukšmo tikimybė įvesta procentais nuo 0 iki 100
 * 2. Kodo dimensija natūralusis skaičius > 1
 * 3. Kodo ilgis natūralusis skaičius > 1
 * 4. Kodo ilgis didesnis ar ligūs už dimensiją
 * 5. Generatoriaus matrica atitinka ilgį
 * 6. Generatoriaus matrica atitinka dimensiją
 * 7. Generatoriaus matrica standartinio pavidalo
 */
public class ConfigViewFormValidator {

    ConfigViewForm form;

    public ConfigViewFormValidator(ConfigViewForm viewForm) {
        form = viewForm;
    }

    /**
     * Metodas validuoja ar formoje ivesti matricos parametrai atitinka reikalavimus ir grazina klaidu sarasa jeigu klaidu buvo rasta
     *
     * @return Optional sarasas su validatoriaus rastomis klaidu tekstinemis israiskomis.
     */
    public Optional<ArrayList<String>> validate(){
        ArrayList<String> errors = new ArrayList<>();

        if (form.getChannelNoise() < 0 || form.getChannelNoise() > 100){
            errors.add("Kanalo triukšmo tikimybė turi būti [0;100]");
        }

        if (form.getCodeDimension() < 2){
            errors.add("Kodo dimensija turi būti natūralusis skaičius didesnis už 1.");
        }

        if (form.getCodeLength() < 2){
            errors.add("Kodo ilgis turi būti natūralusis skaičius didesnis už 1.");
        }

        if (form.getCodeLength() < form.getCodeDimension()){
            errors.add("Kodo ilgis turi būti didesnis arba lygus dimensijai");
        }

        if (form.getGeneratorMatrix().size() != form.getCodeDimension()){
            errors.add("Generatoriaus matrica turi atitikti Kodo dimensiją.");
        }

        if (!isValidLinesLength(form.getGeneratorMatrix(), form.getCodeLength())){
            errors.add("Visos Generatoriaus matricos eilutės turi atitikti Kodo ilgį.");
        }

        if (!isStandard(form.getGeneratorMatrix())){
            errors.add("Generatoriaus Matrica turi būti standartinio pavidalo");
        }

        return (errors.size() == 0) ? Optional.empty() : Optional.of(errors);
    }


    /**
     * Pagalbinis metodas validuoja ar kiekviena matricos eilute yra tinkamo ilgio ir atitinka kodo ilgi
     * @param matrix matrica kurios pavidalas tikrinamas
     * @param codeLength kodo ilgis
     *
     * @return loginę reikšme atspindinti ar visos eilutes atitinka kodo ilgi
     */
    private boolean isValidLinesLength(List<List<Integer>> matrix, Integer codeLength) {
        return matrix.stream()
                    .allMatch(matrixLine ->
                        matrixLine.size() == codeLength
                    );
    }

    /**
     * Pagalbinis metodas validuoja ar matrica yra standartinio pavidalo
     * @param matrix matrica kurios pavidalas tikrinamas
     *
     * @return loginę reikšme atspindinti ar matrica yra standartinio pavidalo
     */
    private Boolean isStandard(List<List<Integer>> matrix){
        Boolean standardised = true;

        Integer lineIndex = 0;

        for(List<Integer> generatorLine: matrix){
            if (!isStandardLine(generatorLine, lineIndex++, matrix.size())){
                standardised = false;
                break;
            }
        }

        return standardised;
    }

    /**
     * Pagalbinis metodas validuoja ar n-toji matricos eilutė yra validi standartinės matricos eilutė
     * @param line Matricos eilutė
     * @param lineIndex eilutės numeris
     * @param lineCount iš viso esančių matricoje skaičius
     *
     * @return gražina loginę reikšmę atspindinčią ar matricos eilutė yra standartinė
     */
    private Boolean isStandardLine(List<Integer> line, Integer lineIndex, Integer lineCount){
        Boolean standardised = true;

        Integer valueIndex = 0;

        for(Integer value: line){
            standardised = (valueIndex == lineIndex && value == 1)
                    || (valueIndex < lineCount && value == 0)
                    || (valueIndex >= lineCount && (value == 0 || value == 1));

            if (!standardised) {
                break;
            }
            valueIndex++;
        }

        return standardised;
    }
}
