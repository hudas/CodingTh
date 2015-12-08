package domain.criptography.matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignas on 15.12.5.
 */
public abstract class Matrix {
    protected Integer columnsCount;
    protected Integer linesCount;
    protected List<MatrixLine> matrixLines = new ArrayList<>();

    public Matrix(Integer columnsCount, Integer linesCount) {
        this.columnsCount = columnsCount;
        this.linesCount = linesCount;
    }

    abstract List<Byte> multiply(List<Byte> vector);

    public void addMatrixLine(MatrixLine matrixLines) {
        this.matrixLines.add(matrixLines);
    }

    public MatrixLine getLine(Integer index) {
        return matrixLines.get(index);
    }
}
