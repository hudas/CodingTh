package main.domain.criptography;

/**
 * Esybe atspindinti sindroma
 */
public class Sindrome {
    // Sindromo dvejetaine reiksme
    BinaryWord value;
    // Sindromo klases lyderio svoris
    Integer weight;

    public Sindrome(BinaryWord value, Integer weight) {
        this.value = value;
        this.weight = weight;
    }

    /**
     * Grazina sindromo dvejetaine reiksme
     * @return
     */
    public BinaryWord getValue() {
        return value;
    }

    /**
     * Grazina sindromo klases lyderio svori
     * @return
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * Palygina ar sindromu dvejetaines reiksmes yra lygios
     * @param sindrome
     * @return
     */
    public boolean equalsTo(Sindrome sindrome){
        return value.equalsTo(sindrome.getValue());
    }
}
