package main.domain.chanel;

import main.domain.criptography.BinaryStream;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Logine esybe imituojanti Kanala.
 */
public class Channel {

    // Kanalas gali imituoti triuksma nuo [0;100]
    public static final int MAX_NOISE = 100;

    // Kanale toleruojamas triuksmo lygis kuri virsijus bitas iskraipomas
    private Integer noiseThreshold;

    // Atsitiktiniu skaiciu generatorius imituojantis kanale esanti triuksma
    private Random noise = new Random();

    public Channel(Integer noisePercentage) {
        noise.setSeed(new Date().getSeconds());
        this.noiseThreshold = MAX_NOISE - noisePercentage;
    }


    /**
     * Sis metodas siuncia dvejetaine bitu seka kanalu ir esant triuksmui kanale ja iskraipo
     *
     * @param input dvejetaine bitu seka kuri siunciama kanalu
     * @return dvejetaine seka kuri atspindi is kanalo isejusia bitu seka
     */
    public BinaryStream send(BinaryStream input){
        List<Byte> values = input.getBytes().stream().map(value -> distort(value)).collect(Collectors.toList());
        return BinaryStream.from(values);
    }

    /**
     * Pagalbinis metodas, grazina persiusto kanalu bito reiksme.
     * Jei triuskmas kanale virsija toleruojama lygi bitas iskraipomas ir grazinamas priesingas bitas.
     *
     * @param inputBit i kanala iejes bitas
     * @return is kanalo gautas bitas
     */
    private Byte distort(Byte inputBit){
        Integer currentNoise = noise.nextInt(MAX_NOISE);
        Integer bit = (currentNoise > noiseThreshold) ? 1 - inputBit : inputBit;
        return bit.byteValue();
    }
}
