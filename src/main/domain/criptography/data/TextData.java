package main.domain.criptography.data;

import main.domain.criptography.BinaryStream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Esybe atspindinti Teksta
 */
public class TextData implements BinaryData {
    String rawVector;
    List<Byte> binaryData = new ArrayList<>();

    /**
     * Konstruojamas is tekstines eilutes
     * @param rawText
     */
    public TextData(String rawText) {
        this.rawVector = rawText;

        // Iteruojama per visus tekstines eilutes simbolius - imame kiekvieno jo reiksme ir verciame i dvejetaniu bitu sarasa
        rawVector.chars()
                 .forEach(charValue -> binaryData.addAll(convertCharacterToBinaryList(charValue)));
    }


    /**
     * Metodas verciantis tekstini simboli is jo ASCII kodo i dvejetainiu bitu seka
     * @param charValue
     * @return
     */
    private List<Byte> convertCharacterToBinaryList(Integer charValue){
        return String.format("%8s", Integer.toBinaryString(charValue))
                     .replace(' ', '0')
                     .chars()
                     .map(Character::getNumericValue)
                     .boxed()
                     .map(Integer::byteValue)
                     .collect(Collectors.toList());
    }

    /**
     * Konstruoja tekstine esybe is dvejetaines bitu sekos
     * @param stream
     * @return
     */
    public static TextData fromBytes(BinaryStream stream){
        StringBuilder builder = new StringBuilder();

        // dvejetainiu bitu srautas skaidomas i zodzius po 1 baita, iteruojama per kiekviena zodi, gaunant jo tekstini atvaizda ir dedant prie rezultato
        stream.splitToWords(8)
              .forEach(word -> builder.append((char) Integer.parseInt(word.toString(), 2)));

        return new TextData(builder.toString());
    }

    /**
     * Grazina paveikslelio atvaizda uzkoduota dvejetaine bitu seka
     * @return
     */
    @Override
    public BinaryStream getStream() {
        return new BinaryStream(binaryData);
    }

    /**
     * validuoja ar konvertuojant nebuvo klaidu - ar visos bitu reiksmes dvejetaines
     * @return
     */
    @Override
    public boolean isValid(){
        return binaryData.stream().allMatch(e -> e == 0 || e == 1);
    }

    public String getRepresentation(){
        return rawVector;
    }
}
