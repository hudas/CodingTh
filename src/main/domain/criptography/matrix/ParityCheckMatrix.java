package main.domain.criptography.matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignas on 15.12.5.
 */
public class ParityCheckMatrix extends Matrix {

    public ParityCheckMatrix(Integer columnsCount, Integer linesCount) {
        super(columnsCount, linesCount);
    }

    @Override
    public List<Byte> multiply(List<Byte> vector) {
        List<Byte> resultVector = new ArrayList<>();

        for(MatrixLine line : matrixLines){
            Integer lineResult = 0;

            for(int index = 0; index < line.size(); index++){
                lineResult += line.get(index) * vector.get(index);
            }

            resultVector.add(lineResult.byteValue());
        }

        return resultVector;
    }
}
