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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ignas on 15.11.14.
 */
public class PictureController {

    @FXML
    Label errorLabel;

    @FXML
    Button select;

    @FXML
    ImageView rawResult;

    @FXML
    ImageView encodedResult;

    @FXML
    ImageView inputImage;

    FileChooser chooser = new FileChooser();

    CodeAdapter code;

    Channel channel;

    public void initialize(){
        code = new CodeAdapter(ConfigurationRepository.getRepository().getCode());
        channel = ConfigurationRepository.getRepository().getChannel();

        select.setOnAction(event -> {
            File imageFile = chooser.showOpenDialog(null);

            long startTime = System.currentTimeMillis();

            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(imageFile);
                bufferedImage.getRGB(0,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputImage.setImage(SwingFXUtils.toFXImage(bufferedImage, null));

            ImageData inputImage = new ImageData(bufferedImage);

            ImageData receivedImage = ImageData.from(BinaryStream.from(channel.send(inputImage.getBytes())), inputImage.getWidth(), inputImage.getHeight());
            rawResult.setImage(SwingFXUtils.toFXImage(receivedImage.getRepresentation(), null));

            try {
                BinaryStream encodedStream = code.encode(inputImage.getStream());
                BinaryStream dissortedStream = BinaryStream.from(channel.send(encodedStream));
                ImageData decoded = ImageData.from(BinaryStream.from(code.decode(dissortedStream)), inputImage.getWidth(), inputImage.getHeight());

                encodedResult.setImage(SwingFXUtils.toFXImage(decoded.getRepresentation(), null));

            } catch (EncodingException e) {
                e.printStackTrace();
            }

            long endTime = System.currentTimeMillis();

            System.out.println("Užtruko : " + (endTime - startTime));

        });
    }

}
