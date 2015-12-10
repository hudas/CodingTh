package main.domain.criptography;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignas on 15.12.6.
 * Pritaikytas Adapter Pattern, kadangi Teksto ir paveikslėlio atveju leidžiama palikti baitų perteklių
 */
public class CodeAdapter implements Crypto {
    Code code;

    List<Integer> leftOverBytes = new ArrayList<>();

    public CodeAdapter(Code code) {
        this.code = code;
    }

    @Override
    public BinaryStream encode(BinaryStream dataStream) throws EncodingException {
        Integer countOfBytesLeft = dataStream.size() % code.getDimension();
        List<Integer> rawBytes = dataStream.subList(0, dataStream.size() - countOfBytesLeft);
        leftOverBytes = dataStream.subList(dataStream.size() - countOfBytesLeft, dataStream.size());

        return code.encode(BinaryStream.from(rawBytes));
    }

    @Override
    public BinaryStream decode(BinaryStream dataStream) throws EncodingException {
        BinaryStream decodedStream = new BinaryStream();
        decodedStream.addAll(code.decode(dataStream));
        decodedStream.addAll(leftOverBytes);
        return decodedStream;
    }
}
