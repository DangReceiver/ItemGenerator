package de.cruelambition;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Language;

public class Main {

    public static void main(String[] args) {
        ItemGenerator ig = new ItemGenerator();

        Language.printAllMessages(Language.getLangFile("en"));
    }

}
