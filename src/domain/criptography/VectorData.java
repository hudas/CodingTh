package domain.criptography;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ignas on 15.12.1.
 */
public class VectorData implements BinaryData {
    String rawVector;
    List<Integer> binaryData;

    public VectorData(String rawVector) {
        this.rawVector = rawVector;
        binaryData = rawVector.chars()
                .map(Character::getNumericValue)
                .boxed()
                .collect(Collectors.toList());
    }


    // Another bottleneck TODO REFACTOR
    public static VectorData fromBytes(List<Integer> bytes){
        StringBuilder builder = new StringBuilder();
        bytes.forEach(builder::append);

        return new VectorData(builder.toString());
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

    public String getRawVector() {
        return rawVector;
    }
}
