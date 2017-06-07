package com.agriscenko;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

public class Application extends WebApplication
{
    @Override
    public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

    @Override
    public void init()
    {
        super.init();

        mountPage("/home", HomePage.class);
        mountPage("/add-list", AddListPage.class);
        mountPage("/settings", SettingsPage.class);
    }

    @Override
    public Session newSession(Request request, Response response)
    {
        return new AppSession(request);
    }
}
