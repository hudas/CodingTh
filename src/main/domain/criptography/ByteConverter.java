package main.domain.criptography;

import java.util.List;

/**
 * Created by ignas on 15.12.8.
 */
public class ByteConverter {
    Integer decimalValue;

    public ByteConverter(Integer decimalValue) {
        this.decimalValue = decimalValue;
    }


    // Teksto ir vektoriaus atveju naudojau jau įgyvendintas funkcijas kurios gražina simboliu dvejetainį formatą,
    // tačiau pamėginus taip apdoroti nuotrauką nedidelę 512x512 px tai užtruko labai ilgai apie 20min (Konvertavimas į baitus, siuntimas kanalu ir atgal į paveikslėlį)
    // Todėl teko pasinaudoti esminiu privalumu kad kompiuteriai operuoja dvejetainiais duomenimis, ir iš skaičiaus gauti jo dvejetainį atvaizdą greičiau
    // ši optimizacija leidžia apdoroti full hd nuotrauką per 2s
    public static BinaryWord fromDecimalByte(Integer decimal){
        BinaryWord word = new BinaryWord();
        word.addBit((byte) ((decimal & 0b10000000) >> 7));
        word.addBit((byte) ((decimal & 0b01000000) >> 6));
        word.addBit((byte) ((decimal & 0b00100000) >> 5));
        word.addBit((byte) ((decimal & 0b00010000) >> 4));
        word.addBit((byte) ((decimal & 0b00001000) >> 3));
        word.addBit((byte) ((decimal & 0b00000100) >> 2));
        word.addBit((byte) ((decimal & 0b00000010) >> 1));
        word.addBit((byte) (decimal & 0b00000001));
        return word;
    }


    public static BinaryWord fromDecimal(Integer decimal){
        BinaryWord word = new BinaryWord();

        word.addBits(fromDecimalByte((decimal >> 24) & 0xFF).getBits());
        word.addBits(fromDecimalByte((decimal >> 16) & 0xFF).getBits());
        word.addBits(fromDecimalByte((decimal >> 8) & 0xFF).getBits());
        word.addBits(fromDecimalByte(decimal & 0xFF).getBits());

        return word;
    }
}
