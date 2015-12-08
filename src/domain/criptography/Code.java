package domain.criptography;

import domain.criptography.matrix.GeneratorMatrix;
import domain.criptography.matrix.MatrixLine;
import domain.criptography.matrix.ParityCheckMatrix;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ignas on 15.11.15.
 */
public class Code implements Crypto{
    // Pagal uždavinio sąlygą q = 2, sprendžiamas uždavinys tik dvejetainiams kodams
    private static final Integer DICTIONARY_SIZE = 2;

    private Integer length;
    private Integer dimension;
    private GeneratorMatrix matrix;
    private ParityCheckMatrix checkMatrix;
    private StandartArray standartArray;


    public Code(Integer length, Integer dimension, GeneratorMatrix matrix) {
        this.length = length;
        this.dimension = dimension;
        this.matrix = matrix;
        this.checkMatrix = generateCheckMatrix(matrix);
        this.standartArray = generateStandartArray(checkMatrix);
    }

    @Override
    public BinaryStream encode(BinaryStream dataStream) throws EncodingException {
        if (!isValid(dataStream)){
            throw new EncodingException();
        }

        List<BinaryWord> encodedWords = dataStream.splitToWords(dimension)
                                            .stream()
                                            .map(word -> encode(word))
                                            .collect(Collectors.toList());

        BinaryStream encodedStream = new BinaryStream();
        encodedWords.stream()
                    .forEach(encodedWord -> encodedStream.addBytes(encodedWord));

        return encodedStream;
    }

    public BinaryWord encode(BinaryWord word){
        return convertToBinary(matrix.multiply(word));
    }

    @Override
    public BinaryStream decode(BinaryStream dataStream){
        List<BinaryWord> decodedWords = dataStream.splitToWords(length)
                                                        .stream()
                                                        .map(word -> decode(word))
                                                        .collect(Collectors.toList());

        BinaryStream decodedStream = new BinaryStream();
        decodedWords.stream()
                    .forEach(decodedWord -> decodedStream.addBytes(decodedWord));

        return decodedStream;
    }

    public BinaryWord decode(BinaryWord word){
        BinaryWord transmittedWord = null;

        Integer leaderWeight = standartArray.getSindromeWeight(BinaryWord.from(convertToBinary(checkMatrix.multiply(word))));
        if (leaderWeight == 0){
            transmittedWord = word;
        }

        int index = 0;
        while (index < length && transmittedWord == null) {
            leaderWeight = standartArray.getSindromeWeight(BinaryWord.from(convertToBinary(checkMatrix.multiply(word))));

            word.invertAtIndex(index);
            Integer invertedWordLeaderWeight = standartArray.getSindromeWeight(BinaryWord.from(convertToBinary(checkMatrix.multiply(word))));

            if (invertedWordLeaderWeight == 0){
                transmittedWord = word;
                break;
            }

            if (invertedWordLeaderWeight >= leaderWeight){
                word.invertAtIndex(index);
            }

            index++;
        }

        return BinaryWord.from(transmittedWord.subList(0, dimension));
    }


    public Integer getLength() {
        return length;
    }

    public Integer getDimension() {
        return dimension;
    }

    private BinaryWord convertToBinary(List<Integer> multipliedMatrix){
        BinaryWord word = new BinaryWord();
        word.addAll(multipliedMatrix.stream()
                .map(value -> Math.abs(value) % 2) // Kadangi sistema dvejetaine pavertimas paprastas, aukstesniam laipsniui reiktu sudetingesnes logikos
                .collect(Collectors.toList()));

        return word;
    }

    private boolean isValid(BinaryStream stream){
        return stream.getBytes().size() % dimension == 0;
    }

    private ParityCheckMatrix generateCheckMatrix(GeneratorMatrix generatorMatrix){
        ParityCheckMatrix checkMatrix = new ParityCheckMatrix(length, length - dimension);

        for(int index = 0; index < length - dimension; index++){
            checkMatrix.addMatrixLine(generateParityLine(generatorMatrix, index));
        }

        return checkMatrix;
    }

    private StandartArray generateStandartArray(ParityCheckMatrix checkMatrix){
        StandartArray array = new StandartArray();

        int weight = 0;

        while (weight < Math.pow(DICTIONARY_SIZE, length) && array.getSindromes().size() < Math.pow(DICTIONARY_SIZE, length - dimension)){
            Set<BinaryWord> possibleWords = getAllWordsForWeight(weight);

            List<Sindrome> sindromes = generateSindromes(checkMatrix, weight, possibleWords);
            for(Sindrome sindrome : sindromes){
                if (array.getSindromes().stream().noneMatch(existingSindrome -> existingSindrome.equalsTo(sindrome))){
                    array.addSindromes(sindrome);
                }
            }
            weight++;
        }

        return array;
    }

    private List<Sindrome> generateSindromes(ParityCheckMatrix checkMatrix, int weight, Set<BinaryWord> possibleWords) {
        List<Sindrome> sindromes = new ArrayList<>();
        for(BinaryWord word : possibleWords){
            Sindrome sindrome = new Sindrome(convertToBinary(checkMatrix.multiply(word)), weight);
            if (!sindromes.stream().anyMatch(existingSindrome -> existingSindrome.equalsTo(sindrome))){
                sindromes.add(sindrome);
            }
        }
        return sindromes;
    }


    /*
     * 'Wrapper' metodas užklojantis rekursijos implementaciją, kad suteikti paprastą API rekursijos naudojimui
     *
     */
    private Set<BinaryWord> getAllWordsForWeight(Integer weight){
        Set<BinaryWord> possibleWords = new HashSet<>();

        if (weight == 0){
            possibleWords.add(BinaryWord.getZero(length));
        } else {
            possibleWords.addAll(getAllWordsForWeight(BinaryWord.getZero(length), weight, 1, 0));
        }

        return possibleWords;
    }

    private Set<BinaryWord> getAllWordsForWeight(BinaryWord word, Integer weight, Integer currentWeight, Integer fromIndex){
        Set<BinaryWord> possibleWords = new HashSet<>();

        for(int index = fromIndex; index < length; index++){
            word.set(index, 1);

            if (weight == currentWeight){
                possibleWords.add(word.getCopy());
            } else {
                possibleWords.addAll(getAllWordsForWeight(word, weight, currentWeight + 1, index + 1)); // Recursion
            }

            word.set(index, 0);
        }

        return possibleWords;
    }

    private MatrixLine generateParityLine(GeneratorMatrix generatorMatrix, int lineIndex) {
        MatrixLine checkLine = new MatrixLine();

        for (int dimensionIterator = 0; dimensionIterator < dimension; dimensionIterator++) {
            checkLine.add(
                    -1 * generatorMatrix.getLine(dimensionIterator)
                            .get(lineIndex + dimension));
        }

        for (int index = 0; index < length - dimension; index++) {
            Integer value;
            if (index == lineIndex) {
                value = 1;
            } else {
                value = 0;
            }
            checkLine.add(value);
        }
        return checkLine;
    }
}
