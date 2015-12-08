package domain.criptography;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignas on 15.12.1.
 */
public class BinaryStream {

    List<Byte> bytes = new ArrayList<>();

    public BinaryStream(){

    }

    public BinaryStream(List<Byte> bytes) {
        this.bytes = bytes;
    }

    public static BinaryStream from(List<Byte> values){
        return new BinaryStream(values);
    }


    public List<BinaryWord> splitToWords(Integer wordLength) {
        List<BinaryWord> words = new ArrayList<>();
        for(int i = 0 ; i < bytes.size(); i += wordLength){
            words.add(new BinaryWord(bytes.subList(i, i + wordLength)));
        }

        return words;
    }

    public List<Byte> getBytes() {
        return bytes;
    }
    public void addBytes(List<Byte> bytes){
        this.bytes.addAll(bytes);
    }
}
