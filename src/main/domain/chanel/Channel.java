package main.domain.chanel;

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

    public List<Integer> send(List<Integer> input){
        return input.stream().map(value -> distort(value)).collect(Collectors.toList());
    }

    private Integer distort(Integer inputBit){
        Integer currentNoise = noise.nextInt(MAX_NOISE);
        return (currentNoise > noiseThreshold) ? 1 - inputBit : inputBit;
    }
}
