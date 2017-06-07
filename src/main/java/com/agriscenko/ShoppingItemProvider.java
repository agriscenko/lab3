package com.agriscenko;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Iterator;

public class ShoppingItemProvider implements ISortableDataProvider<ShoppingItem, ShoppingItem>
{
    private static final long serialVersionUID = 1L;
    private ArrayList<ShoppingItem> shoppingList;

    public void setShoppingList(String listName)
    {
        if (listName == null) {
            shoppingList = new ArrayList<ShoppingItem>();
        } else {
            shoppingList = AppSession.get().getShoppingLists().getShoppingList(listName);
        }
    }

    @Override
    public Iterator iterator(long first, long count)
    {
        return shoppingList.iterator();
    }

    @Override
    public long size()
    {
        return shoppingList.size();
    }

    @Override
    public IModel<ShoppingItem> model(final ShoppingItem object) {
        return new AbstractReadOnlyModel<ShoppingItem>() {
            private static final long serialVersionUID = 1L;

            @Override
            public ShoppingItem getObject() {
                return object;
            }
        };
    }

    @Override
    public void detach() {}

    @Override
    public ISortState<ShoppingItem> getSortState() {
        return null;
    }
}
