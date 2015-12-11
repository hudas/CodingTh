package main.appclient.configview.validators;

import main.appclient.configview.viewforms.ConfigViewForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ignas on 15.11.15.
 */
public class ConfigViewFormValidator {

    ConfigViewForm form;

    public ConfigViewFormValidator(ConfigViewForm viewForm) {
        form = viewForm;
    }

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

        if (!doesMatrixMatchDimmension(form.getGeneratorMatrix(), form.getCodeLength())){
            errors.add("Visos Generatoriaus matricos eilutės turi atitikti Kodo ilgį.");
        }

        if (!isStandartised(form.getGeneratorMatrix())){
            errors.add("Generatoriaus Matrica turi būti standartinio pavidalo");
        }

        return (errors.size() == 0) ? Optional.empty() : Optional.of(errors);
    }

    private boolean doesMatrixMatchDimmension(List<List<Integer>> matrix, Integer codeLength) {
        return matrix.stream()
                    .allMatch(matrixLine ->
                        matrixLine.size() == codeLength
                    );
    }

    private Boolean isStandartised(List<List<Integer>> matrix){
        Boolean standardised = true;

        Integer lineIndex = 0;

        for(List<Integer> generatorLine: matrix){
            if (!validLine(generatorLine, lineIndex++, matrix.size())){
                standardised = false;
                break;
            }
        }

        return standardised;
    }

    private Boolean validLine(List<Integer> line, Integer lineIndex, Integer lineCount){
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
