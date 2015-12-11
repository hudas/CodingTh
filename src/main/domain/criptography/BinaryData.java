package main.domain.criptography;

import java.util.List;

/**
 * Created by ignas on 15.11.15.
 */
public interface BinaryData {
    List<Byte> getBytes();
    BinaryStream getStream();
    boolean isValid();
}
