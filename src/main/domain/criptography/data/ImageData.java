package main.domain.criptography.data;

import main.domain.criptography.BinaryStream;
import main.domain.criptography.BinaryWord;
import main.domain.criptography.ByteConverter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ignas on 15.12.7.
 */
public class ImageData implements BinaryData {

    BufferedImage rawImage;
    List<Byte> bytes = new ArrayList();

    Integer width;
    Integer height;

    public ImageData(BufferedImage image) {
        rawImage = image;

        height = image.getHeight();
        width = image.getWidth();

        // Iteruojame per visą paveikslėliuo duomenų baitų matricą ir dedame kiekvieną baitą į sarašą.
        for(int verticalIndex = 0; verticalIndex < height; verticalIndex++){
            for(int horizontalIndex = 0; horizontalIndex < width; horizontalIndex++){
                bytes.addAll(ByteConverter.fromDecimal(image.getRGB(horizontalIndex, verticalIndex)).getBits()); // Susisdedame visus baitus į sąrašą.
            }
        }
    }

    public static ImageData from(BinaryStream bytes, Integer width, Integer height){
        // Baitus gauname tiesiai iš kanalo t.y. sąrašas su reikšmėmis su 1 ir 0, reikia pasiversti atgal į
        // dešimtainių reikšmių sąrašą iš kurių galėsime atgaminti pikselius

        List<Integer> decimalBytes = bytes.splitToWords(32)
                                          .stream()
                                          .map(BinaryWord::toDecimal)
                                          .collect(Collectors.toList());

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for(int verticalIndex = 0; verticalIndex < height; verticalIndex++){
            for(int horizontalIndex = 0; horizontalIndex < width; horizontalIndex++){
                image.setRGB(horizontalIndex, verticalIndex, decimalBytes.get(horizontalIndex + verticalIndex*height));
                 // Susisdedame visus pikselio spalvos baitus atgal į paveikslėlį.
            }
        }

        return new ImageData(image);
    }

    @Override
    public List<Byte> getBytes() {
        return bytes;
    }

    @Override
    public BinaryStream getStream() {
        return BinaryStream.from(bytes);
    }

    @Override
    public boolean isValid() {
        return bytes.stream().allMatch(e -> e == 0 || e == 1);
    }

    public BufferedImage getRepresentation(){
        return rawImage;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
