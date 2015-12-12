package main.domain.criptography.data;

import main.domain.criptography.BinaryStream;
import main.domain.criptography.BinaryWord;
import main.domain.criptography.ByteConverter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Atvaizdo duomenu esybė.
 *
 */
public class ImageData implements BinaryData {

    BufferedImage rawImage;
    List<Byte> bytes = new ArrayList();

    Integer width;
    Integer height;

    /**
     * Konstruojama is paveikslėlio, iteruojant per visus paveikslelio reiksminius baitus, konvertuojant juos i dvejetaine bitu seka
     * @param image
     */
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

    /**
     * Metodas is dvejetaines bitu sekos atgaminantis nurodyto dydzio atvaizda
     * @param bytes dvejetaine bitu seka
     * @param width plotis
     * @param height aukstis
     * @return
     */
    public static ImageData from(BinaryStream bytes, Integer width, Integer height){
        // Bitus gauname tiesiai iš kanalo t.y. sąrašas su reikšmėmis su 1 ir 0, reikia pasiversti atgal į
        // dešimtainių reikšmių sąrašą iš kurių galėsime atgaminti pikselius

        List<Integer> decimalBytes = bytes.splitToWords(32)
                                          .stream()
                                          .map(BinaryWord::toDecimal)
                                          .collect(Collectors.toList());

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for(int verticalIndex = 0; verticalIndex < height; verticalIndex++){
            for(int horizontalIndex = 0; horizontalIndex < width; horizontalIndex++){
                image.setRGB(horizontalIndex, verticalIndex, decimalBytes.get(horizontalIndex + verticalIndex*height));
                 // Susisdedame visus pikselio spalvos baitus atgal į tam tikras koordinates taip sudarydami atvaizda.
            }
        }

        return new ImageData(image);
    }


    /**
     * Grazina paveikslelio atvaizda uzkoduota dvejetaine bitu seka
     * @return
     */
    @Override
    public BinaryStream getStream() {
        return BinaryStream.from(bytes);
    }

    /**
     * validuoja ar konvertuojant nebuvo klaidu - ar visos bitu reiksmes dvejetaines
     * @return
     */
    @Override
    public boolean isValid() {
        return bytes.stream().allMatch(e -> e == 0 || e == 1);
    }

    /**
     * Grazina atvaizdo esybę
     * @return
     */
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
