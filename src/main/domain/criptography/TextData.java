package main.domain.criptography;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ignas on 15.12.6.
 */
public class TextData implements BinaryData {
    String rawVector;
    List<Byte> binaryData = new ArrayList<>();

    public TextData(String rawText) {
        this.rawVector = rawText;


        rawVector.chars()
                 .forEach(charValue -> binaryData.addAll(convertCharacterToBinaryList(charValue))
                 );
    }


    private List<Byte> convertCharacterToBinaryList(Integer charValue){
        return String.format("%8s", Integer.toBinaryString(charValue))
                     .replace(' ', '0')
                     .chars()
                     .map(Character::getNumericValue)
                     .boxed()
                     .map(Integer::byteValue)
                     .collect(Collectors.toList());
    }

    public static TextData fromBytes(BinaryStream stream){
        StringBuilder builder = new StringBuilder();

        stream.splitToWords(8)
              .forEach(word -> builder.append((char) Integer.parseInt(word.toString(), 2)));

        return new TextData(builder.toString());
    }


    @Override
    public List<Byte> getBytes() {
        return binaryData;
    }

    @Override
    public BinaryStream getStream() {
        return new BinaryStream(binaryData);
    }

    @Override
    public boolean isValid(){
        return binaryData.stream().allMatch(e -> e == 0 || e == 1);
    }

    public String getRepresentation(){
        return rawVector;
    }
}
