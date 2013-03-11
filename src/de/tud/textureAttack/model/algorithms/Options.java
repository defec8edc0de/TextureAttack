/*******************************************************************************
 * Copyright (c) 2013 Sebastian Funke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Sebastian Funke - initial API and implementation
 ******************************************************************************/
package de.tud.textureAttack.model.algorithms;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;

import de.tud.textureAttack.model.utils.PropertyUtils;

public class Options {

	public static enum SelectionToolEnum {
		ColorSelection, GrabCut
	}

	public static enum AttackToolEnum {
		Random;
	}

	public static enum OptionIdentifierEnum {
		Selection_Algorithm, ColorSelection_forColor, ColorSelection_backColor, ColorSelection_ColorThreshold, ColorSelection_MinRegionThreshold, GrabCut_forColor, GrabCut_backColor, Attack_Algorithm, RandomAttack_Randomness
	}

	private HashMap<OptionIdentifierEnum, Object> options;

	public Options() {
		options = new HashMap<OptionIdentifierEnum, Object>();
		options.put(OptionIdentifierEnum.ColorSelection_forColor, Color.black);
		options.put(OptionIdentifierEnum.ColorSelection_backColor, Color.white);
		int colorSelection_colorThreshold = Integer
				.valueOf(PropertyUtils.properties
						.getProperty("colorSelection_colorThreshold"));
		options.put(OptionIdentifierEnum.ColorSelection_ColorThreshold,
				colorSelection_colorThreshold);
		int colorSelection_minRegionThreshold = Integer
				.valueOf(PropertyUtils.properties
						.getProperty("colorSelection_minRegionThreshold"));
		options.put(OptionIdentifierEnum.ColorSelection_MinRegionThreshold,
				colorSelection_minRegionThreshold);

		options.put(OptionIdentifierEnum.GrabCut_forColor, Color.black);
		options.put(OptionIdentifierEnum.GrabCut_backColor, Color.white);

		int random_t = Integer.valueOf(PropertyUtils.properties
				.getProperty("randomAttack_threshold"));
		options.put(OptionIdentifierEnum.RandomAttack_Randomness, random_t);

		options.put(OptionIdentifierEnum.Selection_Algorithm,
				SelectionToolEnum.ColorSelection);
		options.put(OptionIdentifierEnum.Attack_Algorithm,
				AttackToolEnum.Random);

	}

	public Options(HashMap<OptionIdentifierEnum, Object> optionsToSet) {
		for (Entry<OptionIdentifierEnum, Object> option : optionsToSet
				.entrySet())
			options.put(option.getKey(), option.getValue());
	}

	public SelectionToolEnum getSelectionTool() {
		return (Options.SelectionToolEnum) options
				.get(OptionIdentifierEnum.Selection_Algorithm);
	}

	public void setSelectionTool(SelectionToolEnum selectionTool) {
		options.put(OptionIdentifierEnum.Selection_Algorithm, selectionTool);
	}

	public void setOption(Entry<OptionIdentifierEnum, Object> entry) {
		options.put(entry.getKey(), entry.getValue());
	}

	public Object getOption(OptionIdentifierEnum id) {
		return options.get(id);
	}

	public AttackToolEnum getAttackTool() {
		return (Options.AttackToolEnum) options
				.get(OptionIdentifierEnum.Attack_Algorithm);
	}

	public void setAttackTool(AttackToolEnum attackTool) {
		options.put(OptionIdentifierEnum.Attack_Algorithm, attackTool);
	}

	public void setOption(OptionIdentifierEnum id, Object value) {
		options.put(id, value);

	}

}
