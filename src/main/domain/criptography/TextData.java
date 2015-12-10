package main.domain.criptography;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ignas on 15.12.6.
 */
public class TextData implements BinaryData {
    String rawVector;
    List<Integer> binaryData = new ArrayList<>();

    public TextData(String rawText) {
        this.rawVector = rawText;


        rawVector.chars()
                 .forEach(charValue -> binaryData.addAll(convertCharacterToBinaryList(charValue))
                 );
    }


    private List<Integer> convertCharacterToBinaryList(Integer charValue){
        return String.format("%8s", Integer.toBinaryString(charValue))
                     .replace(' ', '0')
                     .chars()
                     .map(Character::getNumericValue)
                     .boxed()
                     .collect(Collectors.toList());
    }

    public static TextData fromBytes(List<Integer> bytes){
        StringBuilder builder = new StringBuilder();

        BinaryStream.from(bytes)
                    .splitToWords(8)
                    .forEach(word -> builder.append((char) Integer.parseInt(word.toString(), 2)));


        return new TextData(builder.toString());
    }


    @Override
    public List<Integer> getBytes() {
        ;       return binaryData;
    }

    @Override
    public BinaryStream getStream() {
        BinaryStream stream = new BinaryStream();
        stream.addAll(binaryData);
        return stream;
    }

    @Override
    public boolean isValid(){
        return binaryData.stream().allMatch(e -> e == 0 || e == 1);
    }

    public String getRepresentation(){
        return rawVector;
    }
}
