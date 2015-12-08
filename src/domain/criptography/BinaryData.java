package domain.criptography;

import java.util.List;

/**
 * Created by ignas on 15.11.15.
 */
public interface BinaryData {
    List<Integer> getBytes();
    BinaryStream getStream();
    boolean isValid();
}
