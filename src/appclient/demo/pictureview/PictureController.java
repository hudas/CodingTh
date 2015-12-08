package appclient.demo.pictureview;

import appmodel.ConfigurationRepository;
import domain.chanel.Channel;
import domain.criptography.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
            long endTime = 0;


            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(imageFile);
                bufferedImage.getRGB(0,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputImage.setImage(SwingFXUtils.toFXImage(bufferedImage, null));

            ImageData inputImage = new ImageData(bufferedImage);

            ImageData receivedImage = ImageData.from(channel.send(inputImage.getStream()), inputImage.getWidth(), inputImage.getHeight());
            rawResult.setImage(SwingFXUtils.toFXImage(receivedImage.getRepresentation(), null));

            endTime = System.currentTimeMillis();
            System.out.println("Step1 : " + (endTime - startTime));

            try {
                BinaryStream encodedStream = code.encode(inputImage.getStream());
                endTime = System.currentTimeMillis();
                System.out.println("Encode : " + (endTime - startTime));

                BinaryStream dissortedStream = channel.send(encodedStream);
                endTime = System.currentTimeMillis();
                System.out.println("Send : " + (endTime - startTime));

                ImageData decoded = ImageData.from(code.decode(dissortedStream), inputImage.getWidth(), inputImage.getHeight());

                endTime = System.currentTimeMillis();
                System.out.println("Decode : " + (endTime - startTime));

                encodedResult.setImage(SwingFXUtils.toFXImage(decoded.getRepresentation(), null));

            } catch (EncodingException e) {
                e.printStackTrace();
            }


            System.out.println("Užtruko : " + (endTime - startTime));

        });
    }

}
