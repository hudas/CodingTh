package main.domain.criptography.data;

import main.domain.criptography.BinaryStream;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Esybe atspindinti Vektoriu
 */
public class VectorData implements BinaryData {
    String rawVector;
    List<Byte> binaryData;


    /**
     * Konstruojama is tekstino vektoriaus atvaizdo
     * @param rawVector
     */
    public VectorData(String rawVector) {
        this.rawVector = rawVector;
        // Iteruojama per kiekviena vektoriaus simboli, gaunant jo skaitine reiksme ir dedant i sarasa
        binaryData = rawVector.chars()
                              .map(Character::getNumericValue)
                              .boxed()
                              .map(Integer::byteValue)
                              .collect(Collectors.toList());
    }


    /**
     * Konstuoruoja vektoriaus esybe is dvejetaines bitu sekos
     * @param stream
     * @return
     */
    public static VectorData fromBytes(BinaryStream stream){
        StringBuilder builder = new StringBuilder();

        // Kiekvienas bitas is bitu sekos yra prijungiamas prie simbolines eilutes galo
        stream.getBytes().forEach(builder::append);

        return new VectorData(builder.toString());
    }

    /**
     * Grazina dvejetaini bitu srauta atspindinti vektoriu
     * @return
     */
    @Override
    public BinaryStream getStream() {
        return new BinaryStream(binaryData);
    }

    /**
     * Tikrina ar Vektorine esybe tinkamai konvertuota i dvejetaine bitu seka
     * @return
     */
    @Override
    public boolean isValid(){
        return binaryData.stream().allMatch(e -> e == 0 || e == 1);
    }

    /**
     * Gaunama simbolinis bitu sekos atvaizdas
     * @return
     */
    public String getRawVector() {
        return rawVector;
    }
}
