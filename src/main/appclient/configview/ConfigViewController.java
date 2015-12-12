package main.appclient.configview;

import main.appclient.configview.validators.ConfigViewFormValidator;
import main.appclient.configview.validators.GeneratorFormValidator;
import main.appclient.configview.viewforms.ConfigViewForm;
import main.appclient.configview.viewforms.GeneratorViewForm;
import main.appmodel.ConfigurationRepository;
import main.domain.chanel.Channel;
import main.domain.criptography.code.Code;
import main.domain.criptography.code.CodeBuilder;
import main.domain.criptography.matrix.GeneratorMatrix;
import main.domain.criptography.matrix.GeneratorMatrixGenerator;
import main.domain.criptography.matrix.MatrixLine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Konfigūracijos valdymo formos kontroleris - Integracinis taškas tarp vartotojo sąsajos ir logines dalies
 */
public class ConfigViewController {

    @FXML
    Parent root;

    @FXML
    TextField codeLength;

    @FXML
    TextField codeDimension;

    @FXML
    TextArea generatorMatrix;

    @FXML
    TextField channelNoise;

    @FXML
    Label errorLabel;

    @FXML
    Button submit;

    @FXML
    Button generate;

    ConfigurationRepository repository = ConfigurationRepository.getRepository();

    @FXML
    public void initialize(){
        // Veiksmai kuriuos programa vykdys paspaudus mygtuka - Generuoti matrica
        generate.setOnAction(event -> {
            Optional<ArrayList<String>> errors = Optional.empty();

            try {
                // Validuojami ivesti duomenys reikalingi sugeneruoti matrica
                errors = new GeneratorFormValidator(generateViewForm()).validate();
            } catch (NumberFormatException e){
                errorLabel.setText("Visi kodo parametrai turi būti skaitiniai");
                return;
            }

            if (errors.isPresent()){
                errorLabel.setText(errors.get().get(0));
                return;
            }


            // Generuojama matrica, pagal pateiktus parametrus
            GeneratorMatrix matrix = new GeneratorMatrixGenerator()
                                        .lines(Integer.parseInt(codeLength.getText()))
                                        .rows(Integer.parseInt(codeDimension.getText()))
                                        .generate();

            generatorMatrix.setText(matrix.toString());
        });


        // Veiksmai kuriuos programa vykdys paspaudus mygtuką - Išsaugoti
        submit.setOnAction(event -> {
            errorLabel.setText("");

            Optional<ArrayList<String>> errors = Optional.empty();

            ConfigViewForm formData = generateConfigForm();
            try {
                // Validuojama konfigūracijos forma
                errors = new ConfigViewFormValidator(formData).validate();
            } catch (NumberFormatException e) {
                errorLabel.setText("Visi kodo parametrai turi būti skaitiniai");
                return;
            }

            if (errors.isPresent()) {
                errorLabel.setText(errors.get().get(0));
                return;
            }

            // Jei visi parametrai validus: Sukuriamas ir repozitorijoje issaugomas kanala imituojantis objektas
            Channel configuredChannel = new Channel(Integer.parseInt(channelNoise.getText()));
            repository.setChannel(configuredChannel);


            // Is sasajoje ivestos ar sugeneruotos formos sukonstruojama generuojanti matrica
            GeneratorMatrix matrix = buildMatrix(formData);

            // Is sasajoje ivestu duomenu sugeneruojamas loginis kodo vienetas kuris issaugomas repozitorijoje
            Code cofiguredCode = new CodeBuilder()
                    .dimension(Integer.parseInt(codeDimension.getText()))
                    .length(Integer.parseInt(codeLength.getText()))
                    .matrix(matrix)
                    .build();
            repository.setCode(cofiguredCode);


            // Integraciniai veiksmai, atverciamas ir paruosiamas demonstracinis langas
            Stage stage = getStage();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/demoview.fxml"));
                stage.setTitle("Sep by step Demonstracija");
                stage.setScene(new Scene(root, 800, 600));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }


    /**
     * Is sasajoje ivestu duomenu sukonstruojamas duomenu perdavimo objektas su konfiguracijos duomenimis
     * @return duomenu sasajos objektas
     * @throws NumberFormatException grazinama klaida jeigu ivesti duomenis neatitinka skaitinio formato
     */
    public ConfigViewForm generateConfigForm() throws NumberFormatException{
        ConfigViewForm form = new ConfigViewForm();

        form.setCodeDimension(Integer.parseInt(codeDimension.getText()));
        form.setCodeLength(Integer.parseInt(codeLength.getText()));
        form.setChannelNoise(Integer.parseInt(channelNoise.getText()));
        List<List<Integer>> matrix = new ArrayList<>();
        String matrixString = generatorMatrix.getText();

        List<String> matrixLines = Arrays.asList(matrixString.split("\n"));
        for(String line: matrixLines){
            List<Integer> intLine =
                    Arrays.asList(line.split(" "))
                            .stream()
                            .mapToInt(Integer::parseInt)
                            .boxed()
                            .collect(Collectors.toList());
            matrix.add(intLine);
        }
        form.setGeneratorMatrix(matrix);

        return form;
    }

    /**
     * Is sasajoje ivestu duomenu sukonstruojamas duomenu perdavimo objektas su matricos generavimui reikalingais duomenimis
     * @return duomenu sasajos objektas
     * @throws NumberFormatException grazinama klaida jeigu ivesti duomenis neatitinka skaitinio formato
     */
    public GeneratorViewForm generateViewForm() throws NumberFormatException{
        GeneratorViewForm form = new GeneratorViewForm();

        form.setCodeDimension(Integer.parseInt(codeDimension.getText()));
        form.setCodeLength(Integer.parseInt(codeLength.getText()));

        return form;
    }


    /**
     * Is sasajoje ivestu duomenu sukonstruojamas generatoriaus matricos objektas
     * @param formData konfiguracijos duomenu perdavimo objektas su kodo konfiguracija
     * @return GeneratorMatrix generatoriaus matrica
     */
    private GeneratorMatrix buildMatrix(ConfigViewForm formData){
        GeneratorMatrix matrix = new GeneratorMatrix(formData.getCodeLength(), formData.getCodeDimension());

        for(List<Integer> lineValues : formData.getGeneratorMatrix()){
            MatrixLine matrixLine = new MatrixLine();
            matrixLine.addAll(lineValues);
            matrix.addMatrixLine(matrixLine);
        }

        return matrix;
    }


    /**
     * Pagalbinis metodas, gauti tevini vartotojo sasajos langa
     *
     */
    public Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }
}
