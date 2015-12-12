package main.appclient.configview.viewforms;

/**
 * Duomenu perdavimo objekto klase (DTO)
 * Skirta perduoti duomenis is generatoriaus formos i kontrolerius
 */
public class GeneratorViewForm {
    Integer codeLength;
    Integer codeDimension;

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
}
