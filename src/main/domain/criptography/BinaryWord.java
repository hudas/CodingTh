package main.domain.criptography;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstrakcija atspindinti dvejetaini zodi sudaryta sudaryta is bitu
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

    /**
     * Metodas invertuojantis bita nurodytu indeksu
     * @param index bito indeksas kuri reikia indeksuoti
     */
    public void invertAtIndex(Integer index){
        Byte valueAtIndex = word.get(index);
        word.set(index, (byte) (1 - valueAtIndex));
    }

    /**
     * Grazina dvejetainio zodzio kopija, reikalinga, kadangi dirbant su dinaminemis strukturomis,
     * Todel pakoregavus viena zodi jis bus pakoreguotas visur
     * @return
     */
    public BinaryWord getCopy(){
        return BinaryWord.from(word.stream()
                        .map(e -> e)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Sunkonstruoja ir grazina nurodyto ilgio nulini zodi
     * @param length
     * @return
     */
    public static BinaryWord getZero(Integer length){
        List<Byte> zero = new ArrayList<>();

        for(int i = 0; i < length; i++){
            zero.add((byte) 0);
        }

        return new BinaryWord(zero);
    }

    /**
     * Palygina dvejetaini zodi su perduotu zodziu
     * @param word zodis su kuriuo lyginama
     * @return true - jeigu zodziai lygus
     */
    public boolean equalsTo(BinaryWord word){
        // Jei zodziu ilgis neatitinka, jie laikomi nelygiais
        if (this.word.size() != word.getBits().size()){
            return false;
        }

        boolean equal = true;

        // Tikrinama ar kiekvienas bitas lygus
        for(int index = 0; index < getBits().size(); index++){
            if (getBits().get(index) != getBits().get(index)){
                equal = false;
                break;
            }
        }

        return equal;
    }

    /**
     * Nustato n-tojo bito reiksme
     * @param index bitas indeksas
     * @param value reiksme kuria nustatysime
     */
    public void set(Integer index, Byte value){
        word.set(index, value);
    }

    /**
     * Dvejetaini zodi vercia i desimtaini jo ekvivalenta
     * @param word dvejetainis zodis
     * @return desimtaine reiksme
     */
    public static Integer toDecimal(BinaryWord word){
        Integer result = 0;
        for(Byte binaryDigit : word.getBits()){
            result = result << 1;
            result += binaryDigit;
        }
        return result;
    }

    /**
     * Grazina zodzio bitu sarasa
     * @return bitu sarasas
     */
    public List<Byte> getBits() {
        return word;
    }

    /**
     * Prie zodzio galo prideda viena bita
     * @param bit
     */
    public void addBit(Byte bit) {
        this.word.add(bit);
    }

    /**
     * Prie zodzio galo prijungia bitu sarasa
     * @param word
     */
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
