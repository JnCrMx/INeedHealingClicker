package de.jcm.ineedhealingclicker.sprites;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by JCM on 25.11.2018.
 */
public class SelectionGroup
{
	private List<Selectable> selectables;
	private Selectable selected;

	public SelectionGroup()
	{
		selectables = new LinkedList<>();
	}

	public void onSelected(Selectable selected)
	{
		this.selected = selected;
		for(Selectable selectable : selectables)
		{
			if(selectable != selected)
				selectable.setSelected(false);
		}
	}

	public boolean isSelected(Selectable selectable)
	{
		return selected == selectable;
	}

	public Selectable getSelectedItem()
	{
		return selected;
	}

	public void setSelectedItem(Selectable selected)
	{
		selected.setSelected(true);
		onSelected(selected);
	}

	public int getSelectedIndex()
	{
		return selectables.indexOf(selected);
	}

	public void setSelectedIndex(int index)
	{
		selected = selectables.get(index);
		selected.setSelected(true);
		onSelected(selected);
	}

	public void addSelectable(Selectable selectable)
	{
		selectables.add(selectable);
		selectable.setGroup(this);
	}
}
