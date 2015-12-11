package main.domain.criptography.matrix;

import com.sun.deploy.util.StringUtils;
import main.domain.criptography.BinaryWord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ignas on 15.11.15.
 */
public class GeneratorMatrix extends Matrix {

    public GeneratorMatrix(Integer columnsCount, Integer linesCount) {
        super(columnsCount, linesCount);
    }



    @Override
    public List<Byte> multiply(List<Byte> word){
        List<List<Byte>> multipliedMatrix = multiplyMatrix(word);

        List<Byte> scalarSum = new ArrayList<>();

        for(int index = 0; index < columnsCount; index++){
            scalarSum.add(calculateColumnSum(multipliedMatrix, index));
        }

        return scalarSum;
    }

    private List<List<Byte>> multiplyMatrix(List<Byte> word) {
        List<List<Byte>> multipliedMatrix = new ArrayList<>();

        for(int index = 0; index < linesCount; index++){
            Byte byteValue = word.get(index);
            MatrixLine line = matrixLines.get(index);

            multipliedMatrix.add(multiplyMatrixLine(byteValue, line));
        }
        return multipliedMatrix;
    }

    private List<Byte> multiplyMatrixLine(Byte byteValue, MatrixLine line) {
        return line.stream()
                .map(value -> {
                    Integer res = value * byteValue;
                    return res.byteValue();
                })
                .collect(Collectors.toList());
    }

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
