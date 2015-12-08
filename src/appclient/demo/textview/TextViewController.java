package appclient.demo.textview;

import appmodel.ConfigurationRepository;
import domain.chanel.Channel;
import domain.criptography.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.List;


/**
 * Created by ignas on 15.11.14.
 */
public class TextViewController {

    @FXML
    Button send;

    @FXML
    TextArea inputText;

    @FXML
    Label encodedResult;

    @FXML
    Label rawResult;

    @FXML
    Label errorLabel;

    Code code;

    Channel channel;

    public void initialize(){
        code = ConfigurationRepository.getRepository().getCode();
        channel = ConfigurationRepository.getRepository().getChannel();

        send.setOnAction(event -> {
            BinaryData inputData = new TextData(inputText.getText());
            if (!inputData.isValid()){
                errorLabel.setText("Įeities vektoriaus reikšmė turi būti dvejetainio formato");
                return;
            }

            rawResult.setText(TextData.fromBytes(channel.send(inputData.getStream())).getRepresentation());

            Crypto crypto = new CodeAdapter(code);
            try {
                BinaryStream encodedBytes = crypto.encode(inputData.getStream());
                BinaryStream dissortedBytes = channel.send(encodedBytes);
                BinaryStream decodedBytes = crypto.decode(dissortedBytes);
                encodedResult.setText(TextData.fromBytes(decodedBytes).getRepresentation());
            } catch (EncodingException e) {
                errorLabel.setText("Įeities vektoriaus reikšmė turi būti dvejetainio formato");
            }
        });
    }
}
