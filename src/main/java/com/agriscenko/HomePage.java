package com.agriscenko;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.*;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends WebPage
{
    private static final long serialVersionUID = 1L;
    private String listName;
    private AppSession session = AppSession.get();
    private ShoppingLists shoppingLists = session.getShoppingLists();

    public HomePage()
    {
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupPlaceholderTag(true);
        feedback.setVisible(false);
        add(feedback);

        final IModel<List<String>> choicesModel = new AbstractReadOnlyModel<List<String>>() {
            @Override
            public List<String> getObject()
            {
                return new ArrayList<String>(shoppingLists.getShoppingLists().keySet());
            }
        };

        final DropDownChoice<String> listDDC = new DropDownChoice<String>(
                "listDDC",
                new PropertyModel<String>(this, "listName"),
                choicesModel
        );
        add(listDDC);
        listDDC.setNullValid(true);

        final Link removeListLink = new Link<Void>("removeList")
        {
            public void onClick()
            {
                shoppingLists.removeShoppingList(listName);
                setResponsePage(HomePage.class);
            }
        };
        add(removeListLink);
        removeListLink.setOutputMarkupPlaceholderTag(true);
        removeListLink.setVisible(false);

        add(new Link<Void>("addList")
        {
            public void onClick()
            {
                setResponsePage(AddListPage.class);
            }
        });

        add(new Link<Void>("synchronizeLists")
        {
            public void onClick()
            {
                setResponsePage(SettingsPage.class);
            }
        });

        List<IColumn> columns = new ArrayList<IColumn>();
        columns.add(new AbstractColumn<ShoppingItem, String>(new Model<String>("#")) {
            @Override
            public void populateItem(Item<ICellPopulator<ShoppingItem>> cell, String componentId, IModel<ShoppingItem> model) {
                int rowId = ((Item) cell.getParent().getParent()).getIndex() + 1;
                cell.add(new Label(componentId, rowId));
            }
        });
        columns.add(new PropertyColumn(new Model<String>("Name"), "name"));
        columns.add(new PropertyColumn(new Model<String>("Quantity"), "quantity"));
        columns.add(new PropertyColumn(new Model<String>("Details"), "details"));
        columns.add(new AbstractColumn<ShoppingItem, String>(new Model<String>("Actions")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<ShoppingItem>> cell, String componentId, IModel<ShoppingItem> model) {
                cell.add(new ActionPanel(componentId, model));
            }
        });

        final ShoppingItemProvider shoppingItemProvider = new ShoppingItemProvider();

        final DefaultDataTable shoppingItemsTable = new DefaultDataTable(
                "shoppingItemsTable",
                columns,
                shoppingItemProvider,
                100
        );
        shoppingItemsTable.setOutputMarkupPlaceholderTag(true);
        add(shoppingItemsTable);
        shoppingItemsTable.setVisible(false);

        final ShoppingItemForm shoppingItemForm = new ShoppingItemForm("shoppingItemForm");
        add(shoppingItemForm);
        shoppingItemForm.setOutputMarkupPlaceholderTag(true);
        shoppingItemForm.setVisible(false);

        listDDC.add(new AjaxFormComponentUpdatingBehavior("change") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                shoppingItemProvider.setShoppingList(listName);
                session.setSelectedShoppingList(listName);

                if (listName == null) {
                    feedback.setVisible(false);
                    removeListLink.setVisible(false);
                    shoppingItemsTable.setVisible(false);
                    shoppingItemForm.setVisible(false);
                } else {
                    feedback.setVisible(true);
                    removeListLink.setVisible(true);
                    shoppingItemsTable.setVisible(true);
                    shoppingItemForm.setVisible(true);
                }
                target.add(feedback);
                target.add(removeListLink);
                target.add(shoppingItemsTable);
                target.add(shoppingItemForm);
            }
        });
    }

    class ActionPanel extends Panel
    {
        private AppSession session = AppSession.get();

        ActionPanel(String id, IModel<ShoppingItem> model)
        {
            super(id, model);

            add(new Link("removeItem") {
                @Override
                public void onClick() {
                    ShoppingItem selectedShoppingItem = (ShoppingItem) getParent().getDefaultModelObject();
                    String selectedShoppingList = session.getSelectedShoppingList();
                    session.getShoppingLists().removeShoppingItem(selectedShoppingList, selectedShoppingItem);
                }
            });
        }
    }

    class ShoppingItemForm extends Form<ValueMap>
    {
        private static final long serialVersionUID = 1L;

        ShoppingItemForm(final String id)
        {
            super(id, new CompoundPropertyModel<ValueMap>(new ValueMap()));
            add(new RequiredTextField<String>("name")
                    .setType(String.class)
                    .add(new ShoppingItemNameValidator())
            );
            add(new RequiredTextField<String>("quantity").setType(String.class));
            add(new TextArea<String>("details").setType(String.class));
        }

        @Override
        public final void onSubmit()
        {
            ValueMap values = getModelObject();
            ShoppingItem shoppingItem = new ShoppingItem(
                    (String) values.get("name"),
                    (String) values.get("quantity"),
                    (String) values.get("details")
            );
            session.getShoppingLists().addShoppingItem(listName, shoppingItem);
            values.put("name", "");
            values.put("quantity", "");
            values.put("details", "");
        }

        class ShoppingItemNameValidator implements IValidator<String>
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void validate(IValidatable<String> validatable) {
                String itemName = validatable.getValue();

                if (session.getShoppingLists().hasShoppingItem(session.getSelectedShoppingList(), itemName)) {
                    validatable.error(new ValidationError().setMessage("Item \"" + itemName + "\" already exists!"));
                }
            }
        }
    }
}
