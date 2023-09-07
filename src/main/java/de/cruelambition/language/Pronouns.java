
package de.cruelambition.language;

import de.cruelambition.oo.PC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;

public class Pronouns
		extends Language {
	private PC pc;
	private Player p;
	private String current;
	private String last;
	private Lang l;
	public static List<String> REPLACEMENTS;
	public static List<List<String>> PRONOUNS;

	public Pronouns(PC pPc, @Nullable String pCurrent) {
		pc = pPc;
		p = pc.thisPlayer();

		current = pCurrent;
		last = null;

		l = new Lang(p);
		REPLACEMENTS = new ArrayList<>(Arrays.asList(new String[] {"@pc1 @pc2 @pc3 @name"}));
	}

	public String fromKey(String key) {
		last = current;
		return Lang.getMessage(l.getLang(p), key);
	}

	public void setFromKey(String key) {
		last = current;
		current = Lang.getMessage(l.getLang(p), key);
	}

	public void fromString(String string) {
		last = current;
		current = string;
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


		int i = 0;
		for (String s : REPLACEMENTS) {

			if (current.contains(s)) current.replaceAll(s, pronouns.get(i));
			i++;
		}
	}
}