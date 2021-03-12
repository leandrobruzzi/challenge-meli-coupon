package ar.com.meli.coupon.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Component
public class Messages {

    private Locale locale;

    @Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor accessor;

    public Messages(){
        this.locale = new Locale("es", "ES");
    }

    @PostConstruct
    private void init(){
        accessor = new MessageSourceAccessor(messageSource, locale);
    }

    public String get(String code){
        return accessor.getMessage(code);
    }

    public void changeLenguage(Locale locale){
        this.locale = locale;
    }
}
