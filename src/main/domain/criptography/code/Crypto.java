package main.domain.criptography.code;

import main.domain.criptography.BinaryStream;

/**
 * Abstrakcija skirta apibrezti kodo kontraktui
 */
public interface Crypto {
    BinaryStream encode(BinaryStream data) throws EncodingException;
    BinaryStream decode(BinaryStream data) throws EncodingException;
}
