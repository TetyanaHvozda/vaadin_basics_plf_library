package at.spengergasse.company1.views.employeeform;

import at.spengergasse.company1.Application;
import at.spengergasse.company1.model.Book;
import at.spengergasse.company1.model.Library;
import at.spengergasse.company1.model.LibraryException;
import at.spengergasse.company1.views.MainLayout;
import at.spengergasse.company1.views.employeelist.BookListView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.function.Consumer;

@PageTitle("Book edit")
@Route(value = "book-edit-form", layout = MainLayout.class)
public class BookEditView extends VerticalLayout implements HasUrlParameter<Long> {

    private TextField title = new TextField("Title");
    private TextField description = new TextField("Description");
   // private NumberField isbn = new NumberField("isbn");
    private ComboBox<Book.Category> category = new ComboBox<>("Category");

    private Book editBook;

    private Button update = new Button("Update");
    private Button cancel = new Button("Cancel");

    private Binder<Book> binder = new Binder<>(Book.class);

    public BookEditView(){
        initUI();
    }

    private void initUI() {
        category.setItems(Book.Category.values());

        FormLayout formLayout = new FormLayout();
        formLayout.add(title, description, category);

        HorizontalLayout buttonLayout = new HorizontalLayout(update, cancel);

        add(formLayout,
                new Hr(),
                buttonLayout);

        update.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                onUpdate();
            }
        });

        cancel.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                onCancel();
            }
        });

        this.binder.bindInstanceFields(this);
    }

    @Override
    public void setParameter(BeforeEvent event, Long eId){
        if(eId == null){
            Application.error( "Book with id null not found." );
            getUI().ifPresent(new Consumer<UI>() {
                @Override
                public void accept(UI ui) {
                    ui.navigate(BookListView.class);
                }
            });
        }
        try{
            this.editBook = Library.getInstance().getBook(eId);
            this.binder.readBean(editBook);
        }catch(LibraryException e){
            Application.error(e);
        }
    }

    private void onCancel() {
    }

    private void onUpdate() {
    }
}
