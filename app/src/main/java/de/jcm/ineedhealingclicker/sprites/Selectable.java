package de.jcm.ineedhealingclicker.sprites;

/**
 * Created by JCM on 25.11.2018.
 */
public interface Selectable
{
	boolean isSelected();

	void setSelected(boolean selected);

	void setGroup(SelectionGroup group);
}
