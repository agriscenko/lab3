package com.agriscenko;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

public final class AppSession extends WebSession
{
    private static final long serialVersionUID = 1L;
    private ShoppingLists shoppingLists;
    private String selectedShoppingList;
    private String token;

    AppSession(Request request)
    {
        super(request);
    }

    String getSelectedShoppingList()
    {
        return selectedShoppingList;
    }

    void setSelectedShoppingList(String selectedShoppingList)
    {
        this.selectedShoppingList = selectedShoppingList;
    }

    ShoppingLists getShoppingLists()
    {
        if (shoppingLists == null) {
            shoppingLists = new ShoppingLists();
            /*
            shoppingLists.addShoppingList("My shopping list");
            shoppingLists.addShoppingList("Mom's shopping list");
            shoppingLists.addShoppingItem("My shopping list", new ShoppingItem("Bread", "1", ""));
            shoppingLists.addShoppingItem("My shopping list", new ShoppingItem("Jameson Whiskey", "1", ""));
            shoppingLists.addShoppingItem("Mom's shopping list", new ShoppingItem("Cranberry", "1 kg", ""));
            shoppingLists.addShoppingItem("Mom's shopping list", new ShoppingItem("Tomato", "2", ""));
            setShoppingLists(shoppingLists);
            */
        }

        return shoppingLists;
    }

    void setShoppingLists(ShoppingLists shoppingLists)
    {
        this.shoppingLists = shoppingLists;
    }

    void setToken(String token)
    {
        this.token = token;
    }

    String getToken()
    {
        return this.token;
    }

    public static AppSession get()
    {
        return (AppSession) Session.get();
    }
}
