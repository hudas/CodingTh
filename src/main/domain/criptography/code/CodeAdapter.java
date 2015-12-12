package main.domain.criptography.code;

import main.domain.criptography.BinaryStream;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignas on 15.12.6.
 * Pritaikytas Adapter Pattern, kadangi Teksto ir paveikslėlio atveju leidžiama palikti baitų perteklių
 */
public class CodeAdapter implements Crypto {
    Code code;

    List<Byte> leftOverBytes = new ArrayList<>();

    public CodeAdapter(Code code) {
        this.code = code;
    }

    @Override
    public BinaryStream encode(BinaryStream dataStream) throws EncodingException {
        Integer countOfBytesLeft = dataStream.getBytes().size() % code.getDimension();
        List<Byte> rawBytes = dataStream.getBytes().subList(0, dataStream.getBytes().size() - countOfBytesLeft);
        leftOverBytes = dataStream.getBytes().subList(dataStream.getBytes().size() - countOfBytesLeft, dataStream.getBytes().size());

        return code.encode(BinaryStream.from(rawBytes));
    }

    @Override
    public BinaryStream decode(BinaryStream dataStream) throws EncodingException {
        BinaryStream decodedStream = code.decode(dataStream);
        decodedStream.addBytes(leftOverBytes);
        return decodedStream;
    }
}
