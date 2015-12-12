package main.appclient.configview.validators;

import main.appclient.configview.viewforms.GeneratorViewForm;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Klasė atsakinga už kodo generatoriaus matricos validavimą.
 * Validuojama, kad:
 * 1. Kodo dimensija natūralusis skaičius.
 * 2. Kodo ilgis natūralusis skaičius.
 * 3. Kodo ilgis didesnis už dimensiją.
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
