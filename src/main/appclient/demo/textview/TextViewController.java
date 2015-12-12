package main.appclient.demo.textview;

import main.appmodel.ConfigurationRepository;
import main.domain.chanel.Channel;
import main.domain.criptography.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import main.domain.criptography.code.Code;
import main.domain.criptography.code.CodeAdapter;
import main.domain.criptography.code.Crypto;
import main.domain.criptography.code.EncodingException;
import main.domain.criptography.data.BinaryData;
import main.domain.criptography.data.TextData;


/**
 * Konfigūracijos valdymo formos kontroleris - Integracinis taškas tarp vartotojo sąsajos ir logines dalies
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
        // Is repozitorijos gaunami kodo ir kanalo logines esybes
        code = ConfigurationRepository.getRepository().getCode();
        channel = ConfigurationRepository.getRepository().getChannel();

        send.setOnAction(event -> {
            // Is formos nuskaitomas tekstas, sukuriama Dvejetainiu duomenu logine esybe
            BinaryData inputData = new TextData(inputText.getText());
            if (!inputData.isValid()){
                errorLabel.setText("Įeities vektoriaus reikšmė turi būti dvejetainio formato");
                return;
            }

            // Kanalu siunciamas nekoduotas tekstas dvejetainiu formatu. Is is kanalo gautu bitu sukonstruojamas naujas tekstas atvaizduojamas
            rawResult.setText(TextData.fromBytes(channel.send(inputData.getStream())).getRepresentation());

            Crypto crypto = new CodeAdapter(code);
            try {
                // Uzkoduojamas tekstas ivestas vartotojo sasajoje
                BinaryStream encodedBytes = crypto.encode(inputData.getStream());
                // Uzkoduotas tekstas siunciamas kanalu
                BinaryStream dissortedBytes = channel.send(encodedBytes);
                // Is kanalo gauti bitai dekoduojami, is ju atstatomas tekstas ir atvaizduojamas
                BinaryStream decodedBytes = crypto.decode(dissortedBytes);
                encodedResult.setText(TextData.fromBytes(decodedBytes).getRepresentation());
            } catch (EncodingException e) {
                errorLabel.setText("Įeities vektoriaus reikšmė turi būti dvejetainio formato");
            }
        });
    }
}
