package appclient.demo.vectorview;

import appmodel.ConfigurationRepository;
import domain.chanel.Channel;
import domain.criptography.BinaryData;
import domain.criptography.Code;
import domain.criptography.EncodingException;
import domain.criptography.VectorData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by ignas on 15.11.14.
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
            BinaryData inputData = new VectorData(inputVector.getText());
            if (!inputData.isValid()){
                errorLabel.setText("Įeities vektoriaus reikšmė turi būti dvejetainio formato");
                return;
            }
            VectorData sentData = null;
            try {
                sentData = VectorData.fromBytes(code.encode(inputData.getStream()));
                encodedVector.setText(sentData.getRawVector());
            } catch (EncodingException e) {
                errorLabel.setText("Netinkamas vektorius - Nepavyko užkoduoti");
            }
        });

        sendButton.setOnAction(event -> {
            BinaryData inputData = new VectorData(encodedVector.getText());
            if (!inputData.isValid()) {
                errorLabel.setText("Užkoduoto vektoriaus reikšmė turi būti dvejetainio formato");
                return;
            }
            VectorData received = VectorData.fromBytes(channel.send(inputData.getBytes()));
            dissortedVector.setText(received.getRawVector());
        });

        decode.setOnAction(event -> {
            BinaryData inputData = new VectorData(dissortedVector.getText());
            if (!inputData.isValid()) {
                errorLabel.setText("Užkoduoto vektoriaus reikšmė turi būti dvejetainio formato");
                return;
            }

            VectorData decodedData = VectorData.fromBytes(code.decode(inputData.getStream()));
            decodedVector.setText(decodedData.getRawVector());
        });
    }
}
