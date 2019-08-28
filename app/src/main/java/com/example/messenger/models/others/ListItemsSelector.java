package com.example.messenger.models.others;

import java.util.ArrayList;
import java.util.Collections;

public class ListItemsSelector {
    private ArrayList<Boolean> selectedItems;
    private int selectedItemsCounter;

    public ListItemsSelector(int size)
    {
        init(size);
    }

    public void init()
    {
        Collections.fill(selectedItems, Boolean.FALSE);
        selectedItemsCounter = 0;
    }

    public void init(int size)
    {
        if (size < 0)
            size = 0;
        selectedItems = new ArrayList<>(size);
        for (int i=0; i<size; i++)
            selectedItems.add(false);
        selectedItemsCounter = 0;
    }

    public boolean atLeastOneSelected()
    {
        return selectedItemsCounter > 0;
    }

    public ArrayList<Boolean> getSelectedItems()
    {
        return selectedItems;
    }

    public int getCounter()
    {
        return selectedItemsCounter;
    }

    public void selectMember(int index)
    {
        if(selectedItems.get(index))
            selectedItemsCounter--;
        else
            selectedItemsCounter++;

        selectedItems.set(index, !selectedItems.get(index));
    }

    public boolean isSelected(int index)
    {
        return selectedItems.get(index);
    }

    public void expand()
    {
        selectedItems.add(false);
    }

    public void remove(int index)
    {
        selectedItems.remove(index);
    }
}
