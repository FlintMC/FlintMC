package net.labyfy.component.i18n;

import com.google.inject.Singleton;
import net.labyfy.base.structure.annotation.AutoLoad;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

@Singleton
@AutoLoad
public class Localizer {

    private final Localizer instance;
    private final Logger logger;
    private ResourceBundle messages;


    public Localizer(){
        this.logger = LogManager.getLogger(Localizer.class);
        try {
            this.messages = ResourceBundle.getBundle("messages");
        } catch (Exception e){
            this.logger.warn(e);
        }

        this.instance = this;
    }

    public boolean setLanguage(String language, String country){

        if (language == null || country == null)
            return false;

        try {
            Locale locale = new Locale(language,country);
            this.messages = ResourceBundle.getBundle("messages",locale);
        } catch (Exception e){
            this.logger.warn(e);
        }

        return true;
    }

    public String dsp(String key){
        if (this.messages.containsKey(key))
            return this.messages.getString(key);
        else {
            logger.warn("Dispatcher key not found" + key);
            return key;
        }
    }

    public Localizer getInstance() {
        return instance;
    }
}
