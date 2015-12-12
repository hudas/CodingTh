package main.domain.criptography;

import java.util.List;

/**
 * Esybe kuri rupinasi desimatiniu zodziu vertimu i dvejetainius zodzius
 */
public class ByteConverter {
    Integer decimalValue;

    public ByteConverter(Integer decimalValue) {
        this.decimalValue = decimalValue;
    }


    // Teksto ir vektoriaus atveju naudojau jau JAVA kalboje įgyvendintas funkcijas kurios gražina simboliu dvejetainį formatą,
    // tačiau pamėginus taip apdoroti nuotrauką nedidelę 512x512 px tai užtruko labai ilgai apie 20min (Konvertavimas į baitus, siuntimas kanalu ir atgal į paveikslėlį)
    // Todėl teko pasinaudoti esminiu privalumu kad kompiuteriai operuoja dvejetainiais duomenimis, ir iš skaičiaus gauti jo dvejetainį atvaizdą greičiau

    /**
     * Konvertuoja 1 Baito desimatini skaiciu i 8 bitu dvejetaini zodi
     * @param decimal desimtainis skaicius
     * @return zodis
     */
    public static BinaryWord fromDecimalByte(Integer decimal){
        // Naudojame bitu lygmens opercaijas
        // naudojame logine AND operacija bitu lygmenije, su kiekvienu bitu,
        // perstumiame bitus iki pirmojo bito, kad gautume reiksme 1 arba 0
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


    /**
     * Konvertuoja 4 baitu (Java Integer) skaiciu i 32 bitu dvejetaini zodi
     * @param decimal desimtainis skaicius
     * @return 32 bitu dvejetainis zodis
     */
    public static BinaryWord fromDecimal(Integer decimal){
        BinaryWord word = new BinaryWord();

        word.addBits(fromDecimalByte((decimal >> 24) & 0xFF).getBits());
        word.addBits(fromDecimalByte((decimal >> 16) & 0xFF).getBits());
        word.addBits(fromDecimalByte((decimal >> 8) & 0xFF).getBits());
        word.addBits(fromDecimalByte(decimal & 0xFF).getBits());

        return word;
    }
}
