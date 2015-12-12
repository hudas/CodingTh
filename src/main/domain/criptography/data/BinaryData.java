package main.domain.criptography.data;

import main.domain.criptography.BinaryStream;

import java.util.List;

/**
 * Created by ignas on 15.11.15.
 */
public interface BinaryData {
    List<Byte> getBytes();
    BinaryStream getStream();
    boolean isValid();
}
