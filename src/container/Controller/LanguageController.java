package container.Controller;

import java.util.Locale;
import java.util.ResourceBundle;

import container.Miscs.UTF8Control;

abstract public class LanguageController {
    private static int currentLang = 1;
    private static String[][] lang = new String[][]{{"vi", "en"}, {"VN", "US"}};
    private static ResourceBundle resourceBundle;

    //change the language
    public static void changeLanguage() {
        currentLang = 1 - currentLang;
        Locale.setDefault(new Locale(lang[0][currentLang], lang[1][currentLang]));
        resourceBundle = ResourceBundle.getBundle("MessageBundle", new UTF8Control());
    }

    //get the key
    public static String getString(String key) {
        resourceBundle = ResourceBundle.getBundle("MessageBundle", new UTF8Control());
        return resourceBundle.getString(key);
    }
}
