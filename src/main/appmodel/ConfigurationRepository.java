package main.appmodel;

import main.domain.chanel.Channel;
import main.domain.criptography.code.Code;

/**
 * Repozitorija imituojanti esybe kuri skirta saugomi kodo ir kanalo logines esybes sukonstruotos konfiguracijos lange,
 * kad veliau juos butu galima pasiekti is aplikacijos
 */
public class ConfigurationRepository {
    //Singleton for Imaginary persistence layer

    private final static ConfigurationRepository repository = new ConfigurationRepository();

    public static ConfigurationRepository getRepository(){
        return repository;
    }


    private Channel channel;
    private Code code;


    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }
}
