package domain.criptography;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ignas on 15.12.1.
 */
public class BinaryStream {

    List<Integer> bytes = new ArrayList<>();

    public BinaryStream(){

    }

    public BinaryStream(List<Integer> bytes) {
        this.bytes = bytes;
    }

    public static BinaryStream from(List<Integer> values){
        return new BinaryStream(values);
    }


    public List<BinaryWord> splitToWords(Integer wordLength) {
        List<BinaryWord> words = new ArrayList<>();
        for(int i = 0 ; i < bytes.size(); i += wordLength){
            BinaryWord word =  new BinaryWord();
            word.addAll(bytes.subList(i, i + wordLength));
            words.add(word);
        }

        return words;
    }

    public List<Integer> getBytes() {
        return bytes;
    }
    public void addBytes(List<Integer> bytes){
        this.bytes.addAll(bytes);
    }
}
