package main.domain.criptography.code;

import main.domain.criptography.BinaryStream;

import java.util.ArrayList;
import java.util.List;

/**
 * Pritaikytas Adapter Pattern.
 * Kadangi Teksto ir paveikslėlio atveju leidžiama palikti bitu perteklių ju neuzkoduojant nes kitaip reiktu papildomu bitu,
 * kad supaprastintinti demonstracija pries uzkoduojant perteklinis bitu skaicius nuimamas, po iskodavimo grazinamas,
 * kad gauti validu tekstini/vaido rezultata
 */
public class CodeAdapter implements Crypto {
    Code code;

    // Pertekliniai bitai
    List<Byte> leftOverBits = new ArrayList<>();

    public CodeAdapter(Code code) {
        this.code = code;
    }


    /**
     * Uzkoduoja dvejetaine bitu seka naudodamas gauta koda, ir isimena perteklinius bitus
     *
     * @param dataStream dvejetainiu bitu seka
     * @return uzkoduota dvejetainiu bitu seka
     * @throws EncodingException
     */
    @Override
    public BinaryStream encode(BinaryStream dataStream) throws EncodingException {
        Integer countOfBytesLeft = dataStream.getBytes().size() % code.getDimension();
        List<Byte> rawBytes = dataStream.getBytes().subList(0, dataStream.getBytes().size() - countOfBytesLeft);
        leftOverBits = dataStream.getBytes().subList(dataStream.getBytes().size() - countOfBytesLeft, dataStream.getBytes().size());

        return code.encode(BinaryStream.from(rawBytes));
    }

    /**
     * Dekoduoja dvejetaine bitu seka naudodamas koda, ir prideda isimintus perteklinius bitus
     *
     * @param dataStream uzkoduota dvejetainiu bitu seka
     * @return dvejetainiu bitu seka
     * @throws EncodingException
     */
    @Override
    public BinaryStream decode(BinaryStream dataStream) throws EncodingException {
        BinaryStream decodedStream = code.decode(dataStream);
        decodedStream.addBytes(leftOverBits);
        return decodedStream;
    }
}
