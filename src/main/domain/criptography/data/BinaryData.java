package main.domain.criptography.data;

import main.domain.criptography.BinaryStream;

import java.util.List;

/**
 * Abstrakcija skirta abibrezti kontrakta tarp duomenu ir dvejetaines bitu sekos
 */
public interface BinaryData {
    BinaryStream getStream();
    boolean isValid();
}
