package main.domain.criptography.code;

import main.domain.criptography.*;
import main.domain.criptography.matrix.GeneratorMatrix;
import main.domain.criptography.matrix.MatrixLine;
import main.domain.criptography.matrix.ParityCheckMatrix;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Logine esybe atvaizduojanti koda
 */
public class Code implements Crypto {
    // Pagal uždavinio sąlygą q = 2, sprendžiamas uždavinys tik dvejetainiams kodams
    private static final Integer DICTIONARY_SIZE = 2;

    // Kodo ilgis
    private Integer length;

    // Kodo dimensija
    private Integer dimension;

    // Generuojanti matrica
    private GeneratorMatrix matrix;

    // Lyginumo matrica
    private ParityCheckMatrix checkMatrix;

    // Standartine lentele
    private StandartArray standartArray;


    public Code(Integer length, Integer dimension, GeneratorMatrix matrix) {
        this.length = length;
        this.dimension = dimension;
        this.matrix = matrix;
        this.checkMatrix = generateCheckMatrix(matrix);
        this.standartArray = generateStandartArray(checkMatrix);
    }


    /**
     * Gauda dvejetaine bitu seka uzkoduoja naudojant generuojanciaja matrica
     *
     * @param dataStream dvejetaine bitu seka
     * @return uzkoduota dvejetaine bitu seka
     * @throws EncodingException
     */
    @Override
    public BinaryStream encode(BinaryStream dataStream) throws EncodingException {
        if (!isValid(dataStream)){
            throw new EncodingException();
        }

        BinaryStream encodedStream = new BinaryStream();

        BinaryWord encodingWord = new BinaryWord();

        // Iteruojama per dvejetaine bitu seka, is jos isimant kodo dimensijai lygu kieki elementu
        for(int index = 0; index < dataStream.getBytes().size(); index += dimension){
            for(int cpIndex = 0; cpIndex < dimension; cpIndex++){
                encodingWord.addBit(dataStream.getBytes().get(index + cpIndex));
            }
            // Isrinkta dimensijos dydzio bitu seka uzkoduojama ir prijungiama prie rezultato bitu sekos
            encodedStream.addBytes(encode(encodingWord).getBits());
            encodingWord.clearBits();
        }

        return encodedStream;
    }

    /**
     * Gautas dvejetainis zodis uzkoduojama naudojant generuojanciaja matrica
     * @param word dvejetainis zodis
     * @return uzkoduotas dvejetainis zodis
     */
    public BinaryWord encode(BinaryWord word){
        return convertToBinary(matrix.multiply(word.getBits()));
    }

    /**
     * Gauta dvejetaine bitu seka dekoduojama naudojant step by step algoritma
     * @param dataStream dvejetaine bitu seka
     * @return dekoduotas dvejetainis zodis
     */
    @Override
    public BinaryStream decode(BinaryStream dataStream){
        BinaryStream decodedStream = new BinaryStream();

        BinaryWord decodingWord = new BinaryWord();

        //
        for(int index = 0; index < dataStream.getBytes().size(); index += length){
            for(int cpIndex = 0; cpIndex < length; cpIndex++){
                decodingWord.addBit(dataStream.getBytes().get(index + cpIndex));
            }
            decodedStream.addBytes(decode(decodingWord).getBits());
            decodingWord.clearBits();
        }

        return decodedStream;
    }

    /**
     * Gautas dvejetainis zodis dekoduojamas naudojant step by step algoritma
     * @param word dvejetainis zodis
     * @return dekoduotas dvejetainis zodis
     */
    public BinaryWord decode(BinaryWord word){
        BinaryWord transmittedWord = null;

        // 1. Gaunamas uzkoduoto zodzio sindromas, o is jo standartineje lenteleje randamas klases lyderio svoris
        Integer leaderWeight = standartArray.getSindromeWeight(convertToBinary(checkMatrix.multiply(word.getBits())));
        // Jei lyderio svoris yra nulinis - zodis nebuvo iskraipytas
        if (leaderWeight == 0){
            transmittedWord = word;
        }


        int index = 0;
        // Jei zodis buvo iskraipytas, iteruojame per visus bitus nuo pradziu
        while (index < length && transmittedWord == null) {
            // Suskaiciuojame uzkoduoto zodzio sindroma, o is jo randame lyderio svori
            leaderWeight = standartArray.getSindromeWeight(convertToBinary(checkMatrix.multiply(word.getBits())));

            // Pakeiciame i-taji bita uzkoduotame zodyje
            word.invertAtIndex(index);
            // Suskaiciuojame zodzio su pakeistu bitu sindroma, randame lyderio svori
            Integer invertedWordLeaderWeight = standartArray.getSindromeWeight(convertToBinary(checkMatrix.multiply(word.getBits())));

            // Jeigu naujojo zodzio lyderio svoris nulinis - radome persiusta zodi.
            if (invertedWordLeaderWeight == 0){
                transmittedWord = word;
                break;
            }

            // Jei naujojo zodzio lyderio svoris mazesnis nei, zodzio pries pakeiciant bito reiksme, pagal algoritma
            // laikome kad naujasis zodis yra teisingesnis ir artimesnis siustajam, issaugome si zodi tolimesnius veiksmus atliksime su juo
            if (invertedWordLeaderWeight >= leaderWeight){
                word.invertAtIndex(index);
            }

            index++;
        }


        // Gave kanalu siusta zodi, nukerpame generatoriaus matricos prideda galune, ir graziname dekoduota bita
        return BinaryWord.from(transmittedWord.getBits().subList(0, dimension));
    }


    public Integer getLength() {
        return length;
    }

    public Integer getDimension() {
        return dimension;
    }


    /**
     * Veiksmus vykdome dvejetaineje sistemoje, metodas skirtas paversti bitu sekai i dvejetaine seka
     * Atliekant daugyba matricos viduje galima gauti netik dvejetaines sumas, todel sis metodas uztikrina kad veiksmai butu atliekami moduliu 2
     * @param multipliedMatrix bitu sarasas
     * @return dvejetainiu bitu zodis
     */
    private BinaryWord convertToBinary(List<Byte> multipliedMatrix){
        return new BinaryWord(multipliedMatrix.stream()
                                              .map(value -> {
                                                  Integer res = Math.abs(value) % 2;
                                                  return res.byteValue();
                                              }) // Kadangi sistema dvejetaine pavertimas paprastas, aukstesniam laipsniui reiktu sudetingesnes logikos
                                              .collect(Collectors.toList()));
    }


    private boolean isValid(BinaryStream stream){
        return stream.getBytes().size() % dimension == 0;
    }


    /**
     * Metodas is standartines generuojancios matricos sugeneruojantis patikrinimo matrica
     * @param generatorMatrix generatoriaus matrica
     * @return
     */
    private ParityCheckMatrix generateCheckMatrix(GeneratorMatrix generatorMatrix){
        ParityCheckMatrix checkMatrix = new ParityCheckMatrix(length, length - dimension);

        for(int index = 0; index < length - dimension; index++){
            checkMatrix.addMatrixLine(generateParityLine(generatorMatrix, index));
        }

        return checkMatrix;
    }


    /**
     * Metodas generuojantis n-taja patikrinimo matricos eilute
     * @param generatorMatrix generavimo matrica
     * @param lineIndex eilutes numeris
     * @return matricos eilute
     */
    private MatrixLine generateParityLine(GeneratorMatrix generatorMatrix, int lineIndex) {
        MatrixLine checkLine = new MatrixLine();

        // Pirmoms eilutems sugeneruojama reiksme is generatoriaus matricos invertuotos dalies
        for (int dimensionIterator = 0; dimensionIterator < dimension; dimensionIterator++) {
            checkLine.add(
                    -1 * generatorMatrix.getLine(dimensionIterator)
                            .get(lineIndex + dimension));
        }

        // Paskutines likusios eilutes uzpildamos standartines vienetines matricos reiksme
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


    /**
     * Generuojama standartine kodo lentele
     *
     * @param checkMatrix patikrinimo matrica
     * @return standartine lentele
     */
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

    //
    private List<Sindrome> generateSindromes(ParityCheckMatrix checkMatrix, int weight, Set<BinaryWord> possibleWords) {
        List<Sindrome> sindromes = new ArrayList<>();
        for(BinaryWord word : possibleWords){
            Sindrome sindrome = new Sindrome(convertToBinary(checkMatrix.multiply(word.getBits())), weight);
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

    private List<BinaryWord> getAllWordsForWeight(BinaryWord word, Integer weight, Integer currentWeight, Integer fromIndex){
        List<BinaryWord> possibleWords = new ArrayList<>();

        for(int index = fromIndex; index < length; index++){
            word.set(index, (byte) 1);

            if (weight == currentWeight){
                possibleWords.add(word.getCopy());
            } else {
                possibleWords.addAll(getAllWordsForWeight(word, weight, currentWeight + 1, index + 1)); // Recursion
            }

            word.set(index, (byte) 0);
        }

        return possibleWords;
    }
}
