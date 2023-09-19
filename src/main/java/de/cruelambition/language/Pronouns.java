
package de.cruelambition.language;

import de.cruelambition.oo.PC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;

public class Pronouns extends Language {
    private PC pc;
    private Player p;
    private String current;
    private String last;
    private Lang l;
    public static List<String> REPLACEMENTS;
    public static ArrayList<List<String>> PRONOUNS_EN, PRONOUNS_DE;

    public Pronouns(PC pPc, @Nullable String pCurrent) {
        pc = pPc;
        p = pc.thisPlayer();

        current = pCurrent;
        last = null;

        l = new Lang(p);
        REPLACEMENTS = new ArrayList<>(Arrays.asList("@pc0", "@pc1", "@pc2", "@pc3", "@pc4", "@name"));
        PRONOUNS_EN = new ArrayList<>(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("they", "them", "their", "theirs", "themselves")),
                new ArrayList<>(Arrays.asList("she", "her", "her", "hers", "herself")),
                new ArrayList<>(Arrays.asList("he", "him", "his", "his", "himself")),
                new ArrayList<>(Arrays.asList("it", "it", "its", "its", "itself")),
                new ArrayList<>(Arrays.asList("one", "one", "one's", "one's", "oneself")))));
        PRONOUNS_DE = new ArrayList<>(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("dey", "deren", "denen", "dey")),
                new ArrayList<>(Arrays.asList("sie", "ihr", "ihr", "sie")),
                new ArrayList<>(Arrays.asList("er", "sein", "ihm", "ihn")),
                new ArrayList<>(Arrays.asList("es", "sein", "ihm", "es")),
                new ArrayList<>(Arrays.asList("mensch", "menschs", "mensch", "mensch")))));
    }

    public String process() {
        if (!hasPronounsReplacement()) return getCurrent();
        replace();
        return getMove();
    }

    public String fromKey(String key) {
        last = current;
        current = null;
        return Lang.getMessage(l.getLang(p), key);
    }

    public void setFromKey(String key) {
        last = current;
        current = null;
        current = Lang.getMessage(l.getLang(p), key);
    }

    public void fromString(String string) {
        last = current;
        current = string;
    }

    public void move() {
        last = current;
        current = null;
    }

    public String getMove() {
        last = current;
        String cCurrent = current;
        current = null;
        return cCurrent;
    }

    public void move(String pCurrent) {
        last = current;
        current = pCurrent;
    }

    public String getCurrent() {
        return current;
    }

    public boolean unset() {
        return (current == null);
    }

    public boolean hasPronounsReplacement() {
        if (unset()) return false;

        for (String s : REPLACEMENTS)
            if (current.contains(s)) return true;
        return false;
    }

    public List<String> getCustomPronouns() {
        PronounsSetup ps = new PronounsSetup(pc, current);
        return ps.getCustomPronouns();
    }

    public List<String> getPronouns() {
        return (new PronounsSetup(pc, current)).getPronouns();
    }

    public List<String> getUndefinedPronouns() {
        PronounsSetup ps = new PronounsSetup(pc, current);

        if (ps.isCustomPermitted() && ps.hasCustomPronouns())
            return ps.getCustomPronouns();

        return ps.getPronouns();
    }

    public void replace() {
        if (!hasPronounsReplacement()) return;

        PronounsSetup ps = new PronounsSetup(pc, current);
        List<String> pronouns = ps.hasCustomPronouns() ? ps.getCustomPronouns() : ps.getPronouns();

        String be = pronouns.get(0).equalsIgnoreCase("they") ? "are" : "is";

        int i = 0;
        for (String s : REPLACEMENTS) {

            if (current.contains(s)) current.replaceAll(s, pronouns.get(i));
            i++;
        }

        current = current.replaceAll("#be", be);
    }
}