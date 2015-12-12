package main.domain.criptography.matrix;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generuojanciosios matricos esybe
 */
public class GeneratorMatrix extends Matrix {

    public GeneratorMatrix(Integer columnsCount, Integer linesCount) {
        super(columnsCount, linesCount);
    }


    /**
     * Igyvendinama vektoriaus daugyba is matricos
     * @param word vektorius kuri dauginame is matricos
     * @return vektorius gautas sudauginus matrica su vektoriumi
     */
    @Override
    public List<Byte> multiply(List<Byte> word){
        // Matrica sudauginama is vektoriaus
        List<List<Byte>> multipliedMatrix = multiplyMatrix(word);

        List<Byte> scalarSum = new ArrayList<>();

        // Iteruojama per stulpelius, kiekvienas stulpelis sumuojamas o skaitinis rezultatas dedamas i vektoriu
        for(int index = 0; index < columnsCount; index++){
            scalarSum.add(calculateColumnSum(multipliedMatrix, index));
        }

        return scalarSum;
    }

    /**
     * Metodas sudaugina perduota zodi is kiekvienos matricoje esancios atitinkamos reiksmes
     * @param word vektorius is kurio dauginama
     * @return
     */
    private List<List<Byte>> multiplyMatrix(List<Byte> word) {
        List<List<Byte>> multipliedMatrix = new ArrayList<>();

        // Iteruojama per matricos eilutes, n - toji matricos eilute sudauginama is n - tosios vektoriaus reiksmes
        for(int index = 0; index < linesCount; index++){
            Byte byteValue = word.get(index);
            MatrixLine line = matrixLines.get(index);

            multipliedMatrix.add(multiplyMatrixLine(byteValue, line));
        }
        return multipliedMatrix;
    }

    /**
     * Vektorius sudauginamas is skaitines reiksmes
     * @param byteValue skaitine reiksme
     * @param line vektorius
     * @return
     */
    private List<Byte> multiplyMatrixLine(Byte byteValue, MatrixLine line) {
        // Kiekviena vektoriaus reiksme padauginama is skaliaro
        return line.stream()
                .map(value -> {
                    Integer res = value * byteValue;
                    return res.byteValue();
                })
                .collect(Collectors.toList());
    }

    /**
     * Metodas sumuojantis matricos stulpeliu reiksmes ir grazinantis rezultato vektoriu
     * @param matrix
     * @param columnIndex
     * @return
     */
    private Byte calculateColumnSum(List<List<Byte>> matrix, int columnIndex) {
        Integer columnSum = 0;
        for(List<Byte> line : matrix){
            columnSum += line.get(columnIndex);
        }
        return columnSum.byteValue();
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        for(MatrixLine line : matrixLines){
            builder.append(StringUtils.join(line.stream().map(val -> val + "").collect(Collectors.toList()), " "));
            builder.append("\n");
        }
        return builder.toString();
    }

}
