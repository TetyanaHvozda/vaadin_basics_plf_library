package at.spengergasse.company1.views.employeeform;


import at.spengergasse.company1.Application;
import at.spengergasse.company1.model.Company;
import at.spengergasse.company1.model.CompanyException;
import at.spengergasse.company1.model.Employee;
import at.spengergasse.company1.views.MainLayout;
import at.spengergasse.company1.views.employeelist.EmployeeListView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.open.App;

import java.util.function.Consumer;


@PageTitle("Edit Employee")
@Route(value = "employee-edit-form", layout = MainLayout.class)
public class EmployeeEditView extends VerticalLayout implements HasUrlParameter<Long> {

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("Email address");
    private TextField phone = new TextField("Phone");
    private DatePicker dateOfBirth = new DatePicker("Birthday");
    private ComboBox<Employee.Role> role = new ComboBox<>("Role");
    private TextField salary = new TextField("Salary");
    private Checkbox subcontractor = new Checkbox();

    private Button save = new Button("Update", new Icon( VaadinIcon.CHECK ));
    private Button cancel = new Button("Cancel", new Icon( VaadinIcon.CLOSE ));

    // 1. Binder instanzieren
    private Binder<Employee> binder = new Binder<>( Employee.class );
    private Employee editEmployee;

    public EmployeeEditView() {
        initUI();
    }


    private void initUI() {
        role.setItems( Employee.Role.values() );

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, email, phone, dateOfBirth,
                role, salary, subcontractor);

        HorizontalLayout buttonLayout = new HorizontalLayout( save, cancel );
        add( formLayout,
                new Hr(),
                buttonLayout);

        save.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                onSave();
            }
        });
        cancel.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                onCancel();
            }
        });

        this.binder.bindInstanceFields( this );

    }

    @Override
    public void setParameter(BeforeEvent event, Long eId) {

        System.out.println( "employeeId=" + eId );
        if(eId == null) {
            Application.error( "Employee with id null not found." );
            getUI().ifPresent(new Consumer<UI>() {
                @Override
                public void accept(UI ui) {
                    ui.navigate( EmployeeListView.class );
                }
            });
        }

        try {
            this.editEmployee = Company.getInstance().getEmployee( eId );
            System.out.println("Employee=" + editEmployee);

            // Anzeige der Daten in den UI-Komponenten
            this.binder.readBean( editEmployee );
        } catch (CompanyException e) {
            Application.error( e );
        }

    }

    // -- ACTIONS ----------------------------------------------------------------

    private void onSave() {

        try {
            System.out.println("firstName=" + firstName.getValue());

            // 3. Daten aus UI in Model-Klasse schreiben
            this.binder.writeBean( editEmployee );


            // Save in DB

            Application.info( "Employee " + editEmployee.getLastName() + " updated." );
            getUI().ifPresent(new Consumer<UI>() {
                @Override
                public void accept(UI ui) {
                    ui.navigate( EmployeeListView.class );
                }
            });

        } catch (ValidationException e) {
            Application.error(e);
        }catch (Exception e) {
            Application.error(e);
        }
    }

    private void onCancel() {
        System.out.println("Cancel");
        getUI().ifPresent(new Consumer<UI>() {
            @Override
            public void accept(UI ui) {
                ui.navigate( EmployeeListView.class );
            }
        });
    }
}
