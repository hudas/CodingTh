package main.domain.criptography;

import java.util.ArrayList;
import java.util.List;

/**
 *  Abstrakcija atvaizduojanti dvejetaini bitu srauta, ir pagrindinus veiksmus atliekamus su juo.
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


    /**
     * Suskaido ir grazina suskaidytu bitu srauta i nurodyto ilgio zodzius
     * @param wordLength nurodo zodzio ilgi i kuri reikia suskaidydi zodzius
     * @return suskaidytu zodziu sarasa
     */
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
