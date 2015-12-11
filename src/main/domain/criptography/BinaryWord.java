package main.domain.criptography;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ignas on 15.12.1.
 */
public class BinaryWord {
    List<Byte> word = new ArrayList<>();

    public BinaryWord() {
    }

    public BinaryWord(List<Byte> word) {
        this.word = word;
    }

    public static BinaryWord from(List<Byte> values){
        return new BinaryWord(values);
    }

    public void invertAtIndex(Integer index){
        Byte valueAtIndex = word.get(index);
        word.set(index, (byte) (1 - valueAtIndex));
    }

    public BinaryWord getCopy(){
        return BinaryWord.from(word.stream()
                        .map(e -> e)
                        .collect(Collectors.toList())
        );
    }

    public static BinaryWord getZero(Integer length){
        List<Byte> zero = new ArrayList<>();

        for(int i = 0; i < length; i++){
            zero.add((byte) 0);
        }

        return new BinaryWord(zero);
    }

    public boolean equalsTo(BinaryWord word){
        if (this.word.size() != word.getBits().size()){
            return false;
        }

        boolean equal = true;

        for(int index = 0; index < getBits().size(); index++){
            if (getBits().get(index) != getBits().get(index)){
                equal = false;
                break;
            }
        }

        return equal;
    }

    public void set(Integer index, Byte value){
        word.set(index, value);
    }

    public static Integer toDecimal(BinaryWord word){
        Integer result = 0;
        for(Byte binaryDigit : word.getBits()){
            result = result << 1;
            result += binaryDigit;
        }
        return result;
    }

    public List<Byte> getBits() {
        return word;
    }

    public void addBit(Byte bit) {
        this.word.add(bit);
    }

    public void addBits(List<Byte> word) {
        this.word.addAll(word);
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        word.forEach(builder::append);
        return builder.toString();
    }

    public void clearBits(){
        word.clear();
    }
}
