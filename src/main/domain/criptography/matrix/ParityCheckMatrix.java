package main.domain.criptography.matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Patikrinimo matricos esybe
 */
public class ParityCheckMatrix extends Matrix {

    public ParityCheckMatrix(Integer columnsCount, Integer linesCount) {
        super(columnsCount, linesCount);
    }


    /**
     * Patikrinimo Matricos daugyba is transponuoto vektoriaus
     * @param vector
     * @return
     */
    @Override
    public List<Byte> multiply(List<Byte> vector) {
        List<Byte> resultVector = new ArrayList<>();

        // Iteruojame per kiekviena matricos eilute
        // n-taji eilutes elementa dauginame is n-tojo vektoriaus elemento
        // skaiciuojame eilutes suma
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
