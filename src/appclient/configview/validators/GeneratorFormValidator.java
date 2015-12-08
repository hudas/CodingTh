package appclient.configview.validators;

import appclient.configview.viewforms.GeneratorViewForm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by ignas on 15.11.22.
 */
public class GeneratorFormValidator {
    GeneratorViewForm form;

    public GeneratorFormValidator(GeneratorViewForm form) {
        this.form = form;
    }

    public Optional<ArrayList<String>> validate(){
        ArrayList<String> errors = new ArrayList<>();

        if (form.getCodeDimension() < 2){
            errors.add("Kodo dimensija turi būti natūralusis skaičius didesnis už 1.");
        }

        if (form.getCodeLength() < 2){
            errors.add("Kodo ilgis turi būti natūralusis skaičius didesnis už 1.");
        }

        if (form.getCodeLength() < form.getCodeDimension()){
            errors.add("Kodo ilgis turi būti didesnis arba lygus dimensijai");
        }

        return (errors.size() == 0) ? Optional.empty() : Optional.of(errors);
    }
}
