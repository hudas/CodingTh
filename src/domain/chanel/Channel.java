package domain.chanel;

import domain.criptography.BinaryStream;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by ignas on 15.12.1.
 */
public class Channel {

    public static final int MAX_NOISE = 100;
    private Integer noiseThreshold;
    private Random noise = new Random();

    public Channel(Integer noisePercentage) {
        noise.setSeed(new Date().getSeconds());
        this.noiseThreshold = MAX_NOISE - noisePercentage;
    }

    public BinaryStream send(BinaryStream input){
        List<Integer> values = input.getBytes().stream().map(value -> distort(value)).collect(Collectors.toList());
        return BinaryStream.from(values);
    }

    private Integer distort(Integer inputBit){
        Integer currentNoise = noise.nextInt(MAX_NOISE);
        return (currentNoise > noiseThreshold) ? 1 - inputBit : inputBit;
    }
}
