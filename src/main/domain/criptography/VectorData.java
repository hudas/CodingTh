package main.domain.criptography;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ignas on 15.12.1.
 */
public class VectorData implements BinaryData {
    String rawVector;
    List<Byte> binaryData;

    public VectorData(String rawVector) {
        this.rawVector = rawVector;
        binaryData = rawVector.chars()
                              .map(Character::getNumericValue)
                              .boxed()
                              .map(Integer::byteValue)
                              .collect(Collectors.toList());
    }


    // Another bottleneck TODO REFACTOR
    public static VectorData fromBytes(BinaryStream stream){
        StringBuilder builder = new StringBuilder();
        stream.getBytes().forEach(builder::append);

        return new VectorData(builder.toString());
    }

    @Override
    public List<Byte> getBytes() {
;       return binaryData;
    }

    @Override
    public BinaryStream getStream() {
        return new BinaryStream(binaryData);
    }

    @Override
    public boolean isValid(){
        return binaryData.stream().allMatch(e -> e == 0 || e == 1);
    }

    public String getRawVector() {
        return rawVector;
    }
}
