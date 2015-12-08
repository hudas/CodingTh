package domain.criptography;

/**
 * Created by ignas on 15.12.5.
 */
public class Sindrome {
    BinaryWord value;
    Integer weight;

    public Sindrome(BinaryWord value, Integer weight) {
        this.value = value;
        this.weight = weight;
    }

    public BinaryWord getValue() {
        return value;
    }

    public Integer getWeight() {
        return weight;
    }

    public boolean equalsTo(Sindrome sindrome){

        return value.equalsTo(sindrome.getValue());
    }
}
