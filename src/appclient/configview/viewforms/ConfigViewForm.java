package appclient.configview.viewforms;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ignas on 15.11.15.
 */
public class ConfigViewForm {
    Integer codeLength;
    Integer codeDimension;
    List<List<Integer>> generatorMatrix;
    Integer channelNoise;

    public Integer getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(Integer codeLength) {
        this.codeLength = codeLength;
    }

    public Integer getCodeDimension() {
        return codeDimension;
    }

    public void setCodeDimension(Integer codeDimension) {
        this.codeDimension = codeDimension;
    }

    public List<List<Integer>> getGeneratorMatrix() {
        return generatorMatrix;
    }

    public void setGeneratorMatrix(List<List<Integer>> generatorMatrix) {
        this.generatorMatrix = generatorMatrix;
    }

    public Integer getChannelNoise() {
        return channelNoise;
    }

    public void setChannelNoise(Integer channelNoise) {
        this.channelNoise = channelNoise;
    }
}
