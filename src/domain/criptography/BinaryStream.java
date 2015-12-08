package domain.criptography;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ignas on 15.12.1.
 */
public class BinaryStream extends ArrayList<Integer> {

    public static BinaryStream from(List<Integer> values){
        BinaryStream stream = new BinaryStream();
        stream.addAll(values);
        return stream;
    }


    public List<BinaryWord> splitToWords(Integer wordLength) {
        List<BinaryWord> words = new ArrayList<>();
        for(int i = 0 ; i < size(); i += wordLength){
            BinaryWord word =  new BinaryWord();
            word.addAll(subList(i, i + wordLength));
            words.add(word);
        }

        return words;
    }

}
