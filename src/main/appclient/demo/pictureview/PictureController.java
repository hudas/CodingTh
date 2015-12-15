package main.appclient.demo.pictureview;

import main.appmodel.ConfigurationRepository;
import main.domain.chanel.Channel;
import main.domain.criptography.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import main.domain.criptography.code.CodeAdapter;
import main.domain.criptography.code.EncodingException;
import main.domain.criptography.data.ImageData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Konfigūracijos valdymo formos kontroleris - Integracinis taškas tarp vartotojo sąsajos ir logines dalies
 */
public class PictureController {

    @FXML
    Label errorLabel;

    @FXML
    Button select;

    @FXML
    Button sendRaw;

    @FXML
    Button sendEncoded;

    @FXML
    ImageView rawResult;

    @FXML
    ImageView encodedResult;

    @FXML
    ImageView inputImage;

    FileChooser chooser = new FileChooser();

    CodeAdapter code;

    Channel channel;

    ImageData imageData;


    public void initialize(){
        // Is repozitorijos isiimamai Kodo ir kanalo loginiai vienetai
        code = new CodeAdapter(ConfigurationRepository.getRepository().getCode());
        channel = ConfigurationRepository.getRepository().getChannel();

        select.setOnAction(event -> {
            long start = System.currentTimeMillis();

            // Atidaromas nuotraukos pasirinkimo langas
            File imageFile = chooser.showOpenDialog(null);

            BufferedImage bufferedImage = null;
            // Pasirinkta nuotrauka nuskaitoma is failines sitemos i atminti
            try {
                bufferedImage = ImageIO.read(imageFile);
                bufferedImage.getRGB(0,0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Is atmintyje saugomos nuotraukos sukuriamas Loginis duomenu vienetas
            imageData = new ImageData(bufferedImage);
            // Ikelta nuotrauka atvaizduojama lange
            this.inputImage.setImage(SwingFXUtils.toFXImage(bufferedImage, null));

            long end = System.currentTimeMillis();
            System.out.println("uzkrovimas: " + (end - start));
        });


        sendRaw.setOnAction(event -> {
            long start = System.currentTimeMillis();

            // Demonstracija su nekoduotu atvaizdu
            rawDemo(imageData);

            long end = System.currentTimeMillis();
            System.out.println("Siuntimas nekoduotu kanalu: " + (end - start));
        });


        sendEncoded.setOnAction(event -> {
            long start = System.currentTimeMillis();
            try {
                // Demonstracija su koduotu atvaizdu
                encodedDemo(imageData);
            } catch (EncodingException e) {
                errorLabel.setText("Nepavyko uzkoduoti");
            }
            long end = System.currentTimeMillis();
            System.out.println("Siuntimas koduotu kanalu: " + (end - start));
        });
    }

    private void rawDemo(ImageData inputImage) {
        // Atvaizdo duomenys siunciami kanalu.
        // Is kanalo gautu bitu konstruojamas naujas tokio pacio dydzio atvaizdas
        ImageData receivedImage = ImageData.from(channel.send(inputImage.getStream()), inputImage.getWidth(), inputImage.getHeight());
        rawResult.setImage(SwingFXUtils.toFXImage(receivedImage.getRepresentation(), null));
    }

    private void encodedDemo(ImageData inputImage) throws EncodingException {
        // Gaunami Atmintyje issaugoto atvaizdo bitai uzkoduojami kodu.
        BinaryStream encodedStream = code.encode(inputImage.getStream());

        // Atvaizdo uzkoduoti bitai siunciami kanalu
        BinaryStream dissortedStream = channel.send(encodedStream);

        // Is kanalo gauti atvaizdo bitai dekoduojami, ir is ju konstruojamas naujas tokio pay dydzio atvaizdas
        ImageData decoded = ImageData.from(code.decode(dissortedStream), inputImage.getWidth(), inputImage.getHeight());

        encodedResult.setImage(SwingFXUtils.toFXImage(decoded.getRepresentation(), null));
    }

}
