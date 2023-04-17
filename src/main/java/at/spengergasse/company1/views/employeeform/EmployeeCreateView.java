package at.spengergasse.company1.views.employeeform;


import at.spengergasse.company1.model.Company;
import at.spengergasse.company1.model.CompanyException;
import at.spengergasse.company1.model.Employee;
import at.spengergasse.company1.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("New Employee")
@Route(value = "employee-create-form", layout = MainLayout.class)
public class EmployeeCreateView extends VerticalLayout {

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("Email address");
    private TextField phone = new TextField("Phone");
    private DatePicker dateOfBirth = new DatePicker("Birthday");
    private ComboBox<Employee.Role> role = new ComboBox<>("Role");
    private TextField salary = new TextField("Salary");
    private Checkbox subcontractor = new Checkbox();

    private Button save = new Button("Save", new Icon( VaadinIcon.CHECK ));
    private Button cancel = new Button("Cancel", new Icon( VaadinIcon.CLOSE ));

    // 1. Binder instanzieren
    private Binder<Employee> binder = new Binder<>( Employee.class );


    public EmployeeCreateView() {
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

    private void onSave() {

        try {
            System.out.println("firstName=" + firstName.getValue());
            Employee newEmployee = new Employee();

            // 3. Daten aus UI in Model-Klasse schreiben
            this.binder.writeBean( newEmployee );


            // Save in DB

            System.out.println( "Save " + newEmployee );

            Company.getInstance().hire( newEmployee );
            System.out.println("save");
            Notification.show( "Saving employee " + newEmployee.getLastName() + " successfully." );


            // 4. reload()
            binder.refreshFields();


        } catch (ValidationException e) {
            e.printStackTrace();
//            System.out.println( e.getMessage() );
            Notification.show( e.getMessage() );
        } catch (CompanyException e) {
            e.printStackTrace();
            Notification.show( e.getMessage() );
        } catch (Exception e) {
            e.printStackTrace();
            Notification.show( e.getMessage() );
        }
    }

    private void onCancel() {
        System.out.println("Cancel");
    }
}
