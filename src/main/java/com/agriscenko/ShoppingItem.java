package com.agriscenko;

import java.io.Serializable;

public class ShoppingItem implements Serializable
{
    private String name;
    private String quantity;
    private String details;

    public ShoppingItem(String name, String quantity, String details)
    {
        this.name = name;
        this.quantity = quantity;
        this.details = details;
    }

    public String getName()
    {
        return name;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public String getDetails()
    {
        return details;
    }
}

