package com.agriscenko;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.PatternValidator;

public class SettingsPage extends WebPage
{
    private AppSession session = AppSession.get();

    public SettingsPage()
    {
        add(new FeedbackPanel("feedback"));
        Label message = new Label("message", session.getToken());
        message.setVisible(false);
        add(message);

        Form form = new Form<ValueMap>("settingsForm", new CompoundPropertyModel<ValueMap>(new ValueMap())) {
            @Override
            public final void onSubmit()
            {
                ValueMap values = getModelObject();
                session.setToken((String) values.get("token"));
                setResponsePage(SettingsPage.class);
            }
        };
        form.setVisible(false);
        add(form);

        FormComponent tokenField = new RequiredTextField<String>("token")
                .setType(String.class)
                .add(new PatternValidator("[a-zA-Z0-9_-]+"));
        form.add(tokenField);

        Link changeSettingsLink = new Link("changeSettings") {
            @Override
            public void onClick() {
                session.setToken(null);
                setResponsePage(SettingsPage.class);
            }
        };
        changeSettingsLink.setVisible(false);
        add(changeSettingsLink);

        Link getListsLink = new Link("getLists") {
            @Override
            public void onClick() {
                ShoppingListSynchronizer.sendGetRequest();
            }
        };
        getListsLink.setVisible(false);
        add(getListsLink);

        Link postListsLink = new Link("postLists") {
            @Override
            public void onClick() {
                ShoppingListSynchronizer.sendPostRequest();
            }
        };
        postListsLink.setVisible(false);
        add(postListsLink);

        if (session.getToken() != null) {
            message.setVisible(true);
            changeSettingsLink.setVisible(true);
            getListsLink.setVisible(true);
            postListsLink.setVisible(true);
        } else {
            form.setVisible(true);
        }
    }
}
