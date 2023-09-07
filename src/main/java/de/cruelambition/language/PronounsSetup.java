package de.cruelambition.language;

import de.cruelambition.oo.PC;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public class PronounsSetup extends Pronouns {
	private PC pc;
	private boolean customPermitted;

	public PronounsSetup(PC pPc, @Nullable String pCurrent) {
		super(pPc, pCurrent);

		customPermitted = (pc.isSet("preferences.pronouns.custom.permitted")
				&& pc.getBoolean("preferences.pronouns.custom.permitted"));
	}

	public boolean isCustomPermitted() {
		return customPermitted;
	}

	public void setCustomPermitted(boolean pPermitted) {
		pc.set("preferences.pronouns.custom.permitted", Boolean.valueOf(customPermitted = pPermitted));
	}

	public int getPronounNum() {
		return pc.getInt("preferences.pronouns.used");
	}

	public List<String> getPronouns() {
		return PRONOUNS.get(getPronounNum());
	}

	public void setPronounNum(int num) {
		pc.set("preferences.pronouns.used", Integer.valueOf(num));
	}

	public boolean hasCustomPronouns() {
		if (!isCustomPermitted()) return false;
		return pc.getBoolean("preferences.pronouns.custom.isSet");
	}

	public void setCustomPronouns(List<String> pronouns) {
		pc.set("preferences.pronouns.custom.used", pronouns);
	}

	public List<String> getCustomPronouns() {

		return (hasCustomPronouns() && isCustomPermitted()) ?
				pc.getStringList("preferences.pronouns.custom.used") : null;
	}
}