package domain.criptography;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ignas on 15.12.1.
 */
public class BinaryWord extends ArrayList<Integer> {

    public static BinaryWord from(List<Integer> values){
        BinaryWord word = new BinaryWord();
        word.addAll(values);
        return word;
    }

    public void invertAtIndex(Integer index){
        Integer valueAtIndex = this.get(index);
        this.set(index, 1 - valueAtIndex);
    }

    public BinaryWord getCopy(){
        return BinaryWord.from(this.stream()
                                   .map(e -> e)
                                   .collect(Collectors.toList())
               );
    }

    public static BinaryWord getZero(Integer length){
        BinaryWord word = new BinaryWord();

        for(int i = 0; i < length; i++){
            word.add(0);
        }

        return word;
    }

    public boolean equalsTo(BinaryWord word){
        if (this.size() != word.size()){
            return false;
        }

        boolean equal = true;

        for(int index = 0; index < this.size(); index++){
            if (this.get(index) != word.get(index)){
                equal = false;
                break;
            }
        }

        return equal;
    }

    public static Integer toDecimal(BinaryWord word){
        Integer result = 0;
        for(Integer binaryDigit : word){
            result = result << 1;
            result += binaryDigit;
        }
        return result;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        this.forEach(builder::append);
        return builder.toString();
    }
}
