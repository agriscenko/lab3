package com.agriscenko;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingLists implements Serializable
{
    private HashMap<String, ArrayList<ShoppingItem>> shoppingLists = new HashMap<String, ArrayList<ShoppingItem>>();

    public HashMap<String, ArrayList<ShoppingItem>> getShoppingLists()
    {
        return shoppingLists;
    }

    public void setShoppingLists(HashMap<String, ArrayList<ShoppingItem>> shoppingLists)
    {
        this.shoppingLists = shoppingLists;
    }

    public ArrayList<ShoppingItem> getShoppingList(String listName)
    {
        return shoppingLists.get(listName);
    }

    public void addShoppingList(String listName)
    {
        shoppingLists.put(listName, new ArrayList<ShoppingItem>());
    }

    public void removeShoppingList(String listName)
    {
        shoppingLists.remove(listName);
    }

    public void addShoppingItem(String listName, ShoppingItem item)
    {
        shoppingLists.get(listName).add(item);
    }

    public void removeShoppingItem(String listName, ShoppingItem item)
    {
        shoppingLists.get(listName).remove(item);
    }

    public boolean hasShoppingList(String listName)
    {
        return shoppingLists.containsKey(listName);
    }

    public boolean hasShoppingItem(String listName, String itemName)
    {
        if (!hasShoppingList(listName)) {
            return false;
        }

        ArrayList<ShoppingItem> items = shoppingLists.get(listName);
        for (ShoppingItem item : items) {
            if (item.getName().equals(itemName)) {
                return true;
            }
        }

        return false;
    }
}
