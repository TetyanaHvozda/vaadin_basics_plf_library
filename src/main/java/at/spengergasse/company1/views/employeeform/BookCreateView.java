package at.spengergasse.company1.views.employeeform;

import at.spengergasse.company1.Application;
import at.spengergasse.company1.model.Book;
import at.spengergasse.company1.model.LibraryException;
import at.spengergasse.company1.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.InputStream;

@PageTitle("Publish Book")
@Route(value = "publish-book-form", layout = MainLayout.class)
public class BookCreateView extends VerticalLayout {
    
    private TextField title = new TextField("Title");
    private TextField description = new TextField("Description");
    private NumberField isbn = new NumberField("isbn");
    private ComboBox<Book.Category> category = new ComboBox<>("Category");

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");

    private Binder<Book> binder = new Binder<>(Book.class);
    
    public BookCreateView(){
        initUI();
    }

    private void initUI() {
        category.setItems(Book.Category.values());

        FormLayout formLayout = new FormLayout();
        formLayout.add(title, description, isbn, category);

        HorizontalLayout buttonLayout = new HorizontalLayout(save, cancel);

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setDropAllowed(true);
        upload.setMaxFiles(1);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
            onFileLoad(inputStream);
        });

        add(formLayout,
                upload,
                new Hr(),
                buttonLayout);

        this.binder.bindInstanceFields(this);
    }

    private void onFileLoad(InputStream stream) {

    }
}
