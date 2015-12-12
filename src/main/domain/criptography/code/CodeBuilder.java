package main.domain.criptography.code;

import main.domain.criptography.matrix.GeneratorMatrix;
import main.domain.criptography.matrix.GeneratorMatrixGenerator;

/**
 * Created by ignas on 15.11.22.
 */
public class CodeBuilder {
    private Integer length;
    private Integer dimension;
    private GeneratorMatrix matrix;

    public Code build(){
        Code code = new Code(length, dimension, matrix);
        return code;
    }

    public CodeBuilder length(Integer length){
        this.length = length;
        return this;
    }

    public CodeBuilder dimension(Integer dimension){
        this.dimension = dimension;
        return this;
    }

    public CodeBuilder generateMatrix(){
        this.matrix = new GeneratorMatrixGenerator()
                            .lines(length)
                            .rows(dimension)
                            .generate();
        return this;
    }

    public CodeBuilder matrix(GeneratorMatrix matrix){
        this.matrix = matrix;
        return this;
    }
}
