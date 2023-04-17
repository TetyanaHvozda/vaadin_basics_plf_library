package at.spengergasse.company1.views.employeelist;

import at.spengergasse.company1.Application;
import at.spengergasse.company1.model.Book;
import at.spengergasse.company1.model.Library;
import at.spengergasse.company1.model.LibraryException;
import at.spengergasse.company1.views.MainLayout;
import at.spengergasse.company1.views.employeeform.BookCreateView;
import at.spengergasse.company1.views.employeeform.BookEditView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.function.Consumer;

@PageTitle("Book List")
@Route(value = "book-list-view", layout = MainLayout.class)
public class BookListView extends VerticalLayout {

    private Grid<Book> grid = new Grid<>(Book.class, false);;
    private Button publish = new Button("Publish" );
    private TextField searchField = new TextField();

    private GridListDataView<Book> dataView = grid.setItems(Library.getInstance().getBooks());

    public BookListView(){
        initUI();
    }

    private void initUI() {
        HorizontalLayout buttonLayout = new HorizontalLayout(publish);



        //grid = new Grid<>(Book.class, false);

        grid.addComponentColumn(book -> {
            Image im = new Image(Book.getImageUrl(), "alt text");
            im.setWidth("200px");
            im.setHeight("150px");
            return im;
        }).setHeader("Cover");

        grid.addColumns("title", "description", "isbn", "category");

        grid.addComponentColumn(new ValueProvider<Book, Component>() {
            @Override
            public Component apply(Book book) {
                Button edit = new Button(VaadinIcon.EDIT.create(), e -> onEdit(book));
                edit.addThemeVariants(
                        ButtonVariant.LUMO_SUCCESS,
                        ButtonVariant.LUMO_SMALL,
                        ButtonVariant.LUMO_TERTIARY_INLINE
                );

                Button delete = new Button(VaadinIcon.TRASH.create(), e -> onDelete(book));
                delete.addThemeVariants(
                        ButtonVariant.LUMO_ERROR,
                        ButtonVariant.LUMO_SMALL,
                        ButtonVariant.LUMO_TERTIARY_INLINE
                );

                HorizontalLayout buttonLayout = new HorizontalLayout(edit, delete);
                return buttonLayout;
            }
        }).setHeader("Actions").setFrozenToEnd(true);


        reloadData();

        VerticalLayout searchLayout = new VerticalLayout(searchField);
        searchField.setWidth("100%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(book -> {
            String searchTerm = searchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesTitle = matchesTerm(book.getTitle(),
                    searchTerm);
            boolean matchesDescription = matchesTerm(book.getDescription(), searchTerm);
            //boolean matchesProfession = matchesTerm(person.getProfession(),
             //       searchTerm);

            return matchesTitle || matchesDescription;
        });

        add(buttonLayout,
                new Hr(),
                searchLayout,
                grid);

        publish.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                UI.getCurrent().navigate(BookCreateView.class);
            }
        });
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private void reloadData() {
        grid.setItems(Library.getInstance().getBooks());
    }


    private void onDelete(Book book) {
        try {
            Library.getInstance().delete(book.getId());
            reloadData();
        }catch (LibraryException e){

            Application.error(e);
        }
    }

    private void onEdit(Book book) {
        getUI().ifPresent(new Consumer<UI>() {
            @Override
            public void accept(UI ui) {
                ui.navigate(BookEditView.class, book.getId());
            }
        });
    }


}
