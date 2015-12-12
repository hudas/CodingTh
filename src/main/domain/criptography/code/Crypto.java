package main.domain.criptography.code;

import main.domain.criptography.BinaryStream;

/**
 * Created by ignas on 15.12.6.
 */
public interface Crypto {
    BinaryStream encode(BinaryStream data) throws EncodingException;
    BinaryStream decode(BinaryStream data) throws EncodingException;
}
