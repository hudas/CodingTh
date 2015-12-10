package main.appclient.configview;

import main.appclient.configview.validators.ConfigViewFormValidator;
import main.appclient.configview.validators.GeneratorFormValidator;
import main.appclient.configview.viewforms.ConfigViewForm;
import main.appclient.configview.viewforms.GeneratorViewForm;
import main.appclient.demo.DemoController;
import main.appmodel.ConfigurationRepository;
import main.domain.chanel.Channel;
import main.domain.criptography.*;
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
 * Created by ignas on 15.11.15.
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
        generate.setOnAction(event -> {
            Optional<ArrayList<String>> errors = Optional.empty();

            try {
                errors = new GeneratorFormValidator(generateViewForm()).validate();
            } catch (NumberFormatException e){
                errorLabel.setText("Visi kodo parametrai turi būti skaitiniai");
                return;
            }

            if (errors.isPresent()){
                errorLabel.setText(errors.get().get(0));
                return;
            }

            GeneratorMatrix matrix = new GeneratorMatrixGenerator()
                                        .lines(Integer.parseInt(codeLength.getText()))
                                        .rows(Integer.parseInt(codeDimension.getText()))
                                        .generate();

            generatorMatrix.setText(matrix.toString());
        });

        submit.setOnAction(event -> {
            errorLabel.setText("");

            Optional<ArrayList<String>> errors = Optional.empty();

            ConfigViewForm formData = generateConfigForm();
            try {
                errors = new ConfigViewFormValidator(formData).validate();
            } catch (NumberFormatException e){
                errorLabel.setText("Visi kodo parametrai turi būti skaitiniai");
                return;
            }

            if (errors.isPresent()){
                errorLabel.setText(errors.get().get(0));
                return;
            }

            Channel configuredChannel = new Channel(Integer.parseInt(channelNoise.getText()));
            repository.setChannel(configuredChannel);

            GeneratorMatrix matrix = buildMatrix(formData);

            Code cofiguredCode = new CodeBuilder()
                                    .dimension(Integer.parseInt(codeDimension.getText()))
                                    .length(Integer.parseInt(codeLength.getText()))
                                    .matrix(matrix)
                                    .build();
            repository.setCode(cofiguredCode);

            Stage stage = getStage();

            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("/resources/demoview.fxml"));
                stage.setTitle("Sep by step Demonstracija");
                stage.setScene(new Scene(root, 800, 600));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public ConfigViewForm generateConfigForm() throws NumberFormatException{
        ConfigViewForm form = new ConfigViewForm();

        form.setCodeDimension(Integer.parseInt(codeDimension.getText()));
        form.setCodeLength(Integer.parseInt(codeLength.getText()));
//        form.setChannelNoise(new BigDecimal(channelNoise.getText().replace(",", ".")));
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

    public GeneratorViewForm generateViewForm() throws NumberFormatException{
        GeneratorViewForm form = new GeneratorViewForm();

        form.setCodeDimension(Integer.parseInt(codeDimension.getText()));
        form.setCodeLength(Integer.parseInt(codeLength.getText()));

        return form;
    }

    private GeneratorMatrix buildMatrix(ConfigViewForm formData){
        GeneratorMatrix matrix = new GeneratorMatrix(formData.getCodeLength(), formData.getCodeDimension());

        for(List<Integer> lineValues : formData.getGeneratorMatrix()){
            MatrixLine matrixLine = new MatrixLine();
            matrixLine.addAll(lineValues);
            matrix.addMatrixLine(matrixLine);
        }

        return matrix;
    }

    public Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }
}
