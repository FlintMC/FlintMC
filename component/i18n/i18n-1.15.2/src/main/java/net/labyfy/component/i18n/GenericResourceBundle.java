package net.labyfy.component.i18n;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Generic resource bundle for key value
 */
public class GenericResourceBundle {
    private Map<String, String> resources = new HashMap<>();
    private final Logger logger;

    @Inject
    GenericResourceBundle(Logger logger){
        this.logger = logger;
    }

    /**
     * Adding all keys to this bundle without override
     * @param bundle Bundle to add
     */
    public void addJavaResourceBundle(ResourceBundle bundle) {
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            addData(key, bundle.getString(key));
        }
    }

    /**
     * Add data if key not present
     * @param key Key to change
     * @param val Value to set
     */
    public void addData(String key, String val) {
        key = key.toLowerCase();
        if (resources.containsKey(key)) {
            logger.warn("Duplicated key '" + key + "'");
        }
        resources.put(key, val);
    }

    /**
     * Override all keys from this bundle
     * @param bundle Bundle with keys to override
     */
    public void overrideAll(ResourceBundle bundle) {
        for (String key : bundle.keySet()){
            resources.put(key,bundle.getString(key));
        }
    }

    /**
     * Retrieves the text for a language key
     * @param key Key to get
     * @return String with text
     */
    public String getString(String key) {
        if (key == null || key.isEmpty()) {
            return "";
        }
        return resources.get(key.toLowerCase());
    }
}
