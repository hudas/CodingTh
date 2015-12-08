package appmodel;

import domain.chanel.Channel;
import domain.criptography.Code;

/**
 * Created by ignas on 15.12.1.
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
