package domain.criptography.matrix;

import com.sun.deploy.util.StringUtils;
import domain.criptography.BinaryWord;

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
    public List<Integer> multiply(List<Integer> word){
        List<List<Integer>> multipliedMatrix = multiplyMatrix(word);

        BinaryWord scalarSum = new BinaryWord();

        for(int index = 0; index < columnsCount; index++){
            scalarSum.add(calculateColumnSum(multipliedMatrix, index));
        }

        return scalarSum;
    }

    private List<List<Integer>> multiplyMatrix(List<Integer> word) {
        List<List<Integer>> multipliedMatrix = new ArrayList<>();

        for(int index = 0; index < linesCount; index++){
            Integer byteValue = word.get(index);
            MatrixLine line = matrixLines.get(index);

            multipliedMatrix.add(multiplyMatrixLine(byteValue, line));
        }
        return multipliedMatrix;
    }

    private List<Integer> multiplyMatrixLine(Integer byteValue, MatrixLine line) {
        return line.stream()
                .map(value -> value * byteValue)
                .collect(Collectors.toList());
    }

    private Integer calculateColumnSum(List<List<Integer>> matrix, int columnIndex) {
        Integer columnSum = 0;
        for(List<Integer> line : matrix){
            columnSum += line.get(columnIndex);
        }
        return columnSum;
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
