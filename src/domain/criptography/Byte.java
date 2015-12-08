package domain.criptography;

import java.util.List;

/**
 * Created by ignas on 15.12.8.
 */
public class Byte {
    Integer decimalValue;
    BinaryWord binaryValue;

    public Byte(Integer decimalValue) {
        this.decimalValue = decimalValue;
    }


    // Teksto ir vektoriaus atveju naudojau jau įgyvendintas funkcijas kurios gražina simboliu dvejetainį formatą,
    // tačiau pamėginus taip apdoroti nuotrauką nedidelę 512x512 px tai užtruko labai ilgai apie 20min (Konvertavimas į baitus, siuntimas kanalu ir atgal į paveikslėlį)
    // Todėl teko pasinaudoti esminiu privalumu kad kompiuteriai operuoja dvejetainiais duomenimis, ir iš skaičiaus gauti jo dvejetainį atvaizdą greičiau
    // ši optimizacija leidžia apdoroti full hd nuotrauką per 2s
    public static BinaryWord fromDecimalByte(Integer decimal){
        BinaryWord word = new BinaryWord();
        word.add((decimal & 0b10000000) >> 7);
        word.add((decimal & 0b01000000) >> 6);
        word.add((decimal & 0b00100000) >> 5);
        word.add((decimal & 0b00010000) >> 4);
        word.add((decimal & 0b00001000) >> 3);
        word.add((decimal & 0b00000100) >> 2);
        word.add((decimal & 0b00000010) >> 1);
        word.add(decimal & 0b00000001);
        return word;
    }


    public static BinaryWord fromDecimal(Integer decimal){
        BinaryWord word = new BinaryWord();

        word.addAll(fromDecimalByte((decimal >> 24) & 0xFF));
        word.addAll(fromDecimalByte((decimal >> 16) & 0xFF));
        word.addAll(fromDecimalByte((decimal >> 8) & 0xFF));
        word.addAll(fromDecimalByte(decimal & 0xFF));

        return word;
    }
}
