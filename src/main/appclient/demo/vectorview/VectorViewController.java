package main.appclient.demo.vectorview;

import main.appmodel.ConfigurationRepository;
import main.domain.chanel.Channel;
import main.domain.criptography.data.BinaryData;
import main.domain.criptography.code.Code;
import main.domain.criptography.code.EncodingException;
import main.domain.criptography.data.VectorData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Konfigūracijos valdymo formos kontroleris - Integracinis taškas tarp vartotojo sąsajos ir logines dalies
 */
public class VectorViewController {

    @FXML
    Button sendButton;

    @FXML
    TextField inputVector;

    @FXML
    Label encodedVector;

    @FXML
    Button encode;

    @FXML
    Button decode;

    @FXML
    Label decodedVector;

    @FXML
    TextField dissortedVector;

    @FXML
    Label errorLabel;

    Channel channel;

    Code code;

    @FXML
    public void initialize(){
        code = ConfigurationRepository.getRepository().getCode();
        channel = ConfigurationRepository.getRepository().getChannel();

        encode.setOnAction(event -> {
            // Is ivestos dvejetaines sekos konstruojamas logine vektoriaus esybe ir validuojama
            BinaryData inputData = new VectorData(inputVector.getText());
            if (!inputData.isValid()){
                errorLabel.setText("Įeities vektoriaus reikšmė turi būti dvejetainio formato");
                return;
            }
            VectorData sentData = null;
            try {
                // Ivesta dvejetaine seka uzkoduojama ir atvaizduojama vartotojo sasajoje
                sentData = VectorData.fromBytes(code.encode(inputData.getStream()));
                encodedVector.setText(sentData.getRawVector());
            } catch (EncodingException e) {
                errorLabel.setText("Netinkamas vektorius - Nepavyko užkoduoti");
            }
        });

        sendButton.setOnAction(event -> {
            // Uzkoduota dvejetaine seka, is vartotojo sasajos vel uzkraunama i logine esybe
            BinaryData inputData = new VectorData(encodedVector.getText());
            if (!inputData.isValid()) {
                errorLabel.setText("Užkoduoto vektoriaus reikšmė turi būti dvejetainio formato");
                return;
            }
            // Uzkoduoti bitai siunciami kanalu, o is kanalo isejusiu bitu atgaminama dvejetaine seka
            VectorData received = VectorData.fromBytes(channel.send(inputData.getStream()));
            dissortedVector.setText(received.getRawVector());
        });

        decode.setOnAction(event -> {
            BinaryData inputData = new VectorData(dissortedVector.getText());
            if (!inputData.isValid()) {
                errorLabel.setText("Užkoduoto vektoriaus reikšmė turi būti dvejetainio formato");
                return;
            }

            // Iskraipyti bitai nuskaitomi is vartotojo sasajos ir atkoduojami Step by Step algoritmu
            VectorData decodedData = VectorData.fromBytes(code.decode(inputData.getStream()));
            decodedVector.setText(decodedData.getRawVector());
        });
    }
}
