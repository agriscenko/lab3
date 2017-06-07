package com.agriscenko;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;
import java.util.Map;

final class ShoppingListSynchronizer
{
    private static AppSession session = AppSession.get();
    private final static String WEBSERVICE_URL = "http://146.185.141.100:8080/services/purchases/";

    static void sendGetRequest()
    {
        Client client = Client.create();
        WebResource webResource = client.resource(WEBSERVICE_URL + session.getToken());
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String json = response.getEntity(String.class);
        System.out.println("JSON of GET request: " + json);
        Gson gson = new Gson();
        Map<String, ArrayList<ShoppingItem>> map = gson
                .fromJson(
                        json,
                        new TypeToken<Map<String, ArrayList<ShoppingItem>>>(){}.getType()
                );
        ShoppingLists shoppingLists = new ShoppingLists();

        for (Map.Entry<String, ArrayList<ShoppingItem>> entry : map.entrySet()) {
            shoppingLists.addShoppingList(entry.getKey());
            for (ShoppingItem shoppingItem : entry.getValue()) {
                shoppingLists.addShoppingItem(entry.getKey(), shoppingItem);
            }
        }

        session.setShoppingLists(shoppingLists);
    }

    static void sendPostRequest()
    {
        Client client = Client.create();
        WebResource webResource = client.resource(WEBSERVICE_URL + session.getToken());

        Gson gson = new Gson();
        String json = gson.toJsonTree(session.getShoppingLists()).getAsJsonObject().get("shoppingLists").toString();
        System.out.println("JSON for POST request: " + json);
        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, json);

        if (response.getStatus() != 201 && response.getStatus() != 204) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
    }
}
