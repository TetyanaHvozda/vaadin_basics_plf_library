package at.spengergasse.company1.views.company;


import at.spengergasse.company1.Application;
import at.spengergasse.company1.model.Company;
import at.spengergasse.company1.model.CompanyException;
import at.spengergasse.company1.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.InputStream;

@PageTitle(value = "Company Form")
@Route(value = "company-form", layout = MainLayout.class)
public class CompanyForm extends VerticalLayout {

    private TextField companyName = new TextField("Company name");
    private TextField companyAddress = new TextField("Company address");

    private Button save = new Button("Save", VaadinIcon.CHECK.create(), e -> onSave() );
    private Button cancel = new Button("Cancel", VaadinIcon.CLOSE.create(), e -> onCancel() );
    private Button export = new Button("Save to file", VaadinIcon.FILE.create(), e -> onSaveToFile() );


    private Binder<Company> binder = new Binder<>( Company.class );


    public CompanyForm() {
        initUI();
    }

    private void initUI() {
        save.addThemeVariants( ButtonVariant.LUMO_PRIMARY );

        binder.bindInstanceFields( this );

        Company company = Company.getInstance();
        binder.readBean( company );



        // Upload
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload( buffer );
        upload.setMaxFiles(1);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream( fileName );
            onImportFromFile( inputStream );
        });


        // Details
        HorizontalLayout persistenzLayout = new HorizontalLayout( export, upload );
        Details details = new Details("Export/Import from/to file", persistenzLayout );
        details.setOpened(false);


        add( new FormLayout( companyName, companyAddress ),
                new Hr(),
                new HorizontalLayout( save, cancel ),
                details
        );


    }

    // -- ACTIONS --------------------------------------------------

    private void onSave() {
        try {
            binder.writeBean( Company.getInstance() );
            Application.info("Company successfully updated.");
        } catch (ValidationException e) {
            Application.error(e);
        }
    }

    private void onCancel() {
        binder.readBean( Company.getInstance() );
    }

    private void onSaveToFile() {

        /*
            Pfade:
            c:\data\files\meinedaten.csv
         */
        //System.out.println("c:\\files\\");
//        Company.getInstance().writeToFile( "" );

        try {
            String pathToFile = "data/company-changed.csv";
            Company.getInstance().writeToFile( pathToFile );
            Application.info("Data successfully written to file " + pathToFile);
        } catch (CompanyException e) {
            Application.error(e);
        }

    }

    private void onImportFromFile(InputStream stream) {
        try {
            Company.loadFromFile( stream );
            binder.readBean( Company.getInstance() );
            Application.info("File successfully loaded");
        } catch (CompanyException e) {
            Application.error(e);
        }
    }

}
