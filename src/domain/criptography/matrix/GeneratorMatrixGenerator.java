package domain.criptography.matrix;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by ignas on 15.11.22.
 */
public class GeneratorMatrixGenerator {

    public Random valueGenerator = new Random();

    public static final int MAX_VALUE = 2;
    private Integer rowsCount;
    private Integer linesCount;
    private List<MatrixLine> matrixLines;

    public GeneratorMatrixGenerator rows(Integer rows){
        rowsCount = rows;
        return this;
    }

    public GeneratorMatrixGenerator lines(Integer lines){
        linesCount = lines;
        return this;
    }

    public GeneratorMatrix generate(){
        valueGenerator.setSeed(new Date().getSeconds());

        GeneratorMatrix matrix = new GeneratorMatrix(rowsCount, linesCount);

        for(int index = 0; index < rowsCount; index++){
            matrix.addMatrixLine(generateLine(index));
        }

        return matrix;
    }

    private MatrixLine generateLine(Integer lineIndex){
        MatrixLine matrixLine = new MatrixLine();

        for(int index = 0; index < linesCount; index++){
            Integer value;

            if (index == lineIndex){
                value = 1;
            } else if (index < rowsCount) {
                value = 0;
            } else {

                value = valueGenerator.nextInt(MAX_VALUE);
            }

            matrixLine.add(value);
        }
        return matrixLine;
    }
}
