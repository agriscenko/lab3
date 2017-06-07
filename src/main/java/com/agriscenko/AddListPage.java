package com.agriscenko;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class AddListPage extends WebPage
{
    private AppSession session = AppSession.get();

    public AddListPage()
    {
        add(new FeedbackPanel("feedback"));
        add(new AddListForm("addListForm"));
    }

    private class AddListForm extends Form<ValueMap>
    {
        private static final long serialVersionUID = 1L;

        AddListForm(final String id)
        {
            super(id, new CompoundPropertyModel<ValueMap>(new ValueMap()));
            add(new RequiredTextField<String>("listName")
                    .setType(String.class)
                    .add(new ShoppingListNameValidator())
            );
        }

        @Override
        public final void onSubmit()
        {
            ValueMap values = getModelObject();
            session.getShoppingLists().addShoppingList((String) values.get("listName"));
            setResponsePage(HomePage.class);
        }

        class ShoppingListNameValidator implements IValidator<String>
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void validate(IValidatable<String> validatable) {
                String listName = validatable.getValue();

                if (session.getShoppingLists().hasShoppingList(listName)) {
                    validatable.error(new ValidationError().setMessage("List name \"" + listName + "\" already exists!"));
                }
            }
        }
    }
}
