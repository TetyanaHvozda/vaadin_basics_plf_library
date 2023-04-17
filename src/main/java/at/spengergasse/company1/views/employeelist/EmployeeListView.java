package at.spengergasse.company1.views.employeelist;

import at.spengergasse.company1.Application;
import at.spengergasse.company1.model.Company;
import at.spengergasse.company1.model.CompanyException;
import at.spengergasse.company1.model.Employee;
import at.spengergasse.company1.views.MainLayout;
import at.spengergasse.company1.views.employeeform.EmployeeEditView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.function.Consumer;

@PageTitle("Employee List")
@Route(value = "employee-list", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class EmployeeListView extends VerticalLayout {

    private Grid<Employee> grid;

    public EmployeeListView() {
        initUI();
    }

    private void initUI() {
        grid = new Grid<>(Employee.class, false);

        grid.addColumns( "firstName", "lastName", "email", "phone", "dateOfBirth",
                "role", "salary", "subcontractor");

        grid.addComponentColumn(new ValueProvider<Employee, Component>() {
            @Override
            public Component apply(Employee employee) {
                Button edit = new Button( VaadinIcon.EDIT.create() );
                edit.addThemeVariants(
                        ButtonVariant.LUMO_SUCCESS,
                        ButtonVariant.LUMO_SMALL,
                        ButtonVariant.LUMO_TERTIARY_INLINE);
                edit.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> event) {
                        onEdit( employee );
                    }
                });

                Button fire = new Button( VaadinIcon.TRASH.create(), e -> onFire( employee ) );
                fire.addThemeVariants(
                        ButtonVariant.LUMO_ERROR,
                        ButtonVariant.LUMO_SMALL,
                        ButtonVariant.LUMO_TERTIARY_INLINE);
                HorizontalLayout buttonLayout = new HorizontalLayout( edit, fire );
                return buttonLayout;
            }
        }).setHeader( "Actions" ).setFrozenToEnd( true );

        reloadData();

        add( grid );
    }

    // -- ACTIONS --------------------------------------------------------------------------------

    private void onEdit(Employee employee) {
        System.out.println("Edit " + employee.getLastName());

        getUI().ifPresent(new Consumer<UI>() {
            @Override
            public void accept(UI ui) {
                ui.navigate( EmployeeEditView.class, employee.getId() );
            }
        });

    }

    private void onFire(Employee employee) {
        System.out.println("Fire " + employee.getLastName());
        try {

            Company.getInstance().fire( employee.getId() );
            reloadData();

        } catch (CompanyException e) {
            Application.error(e);
        }
    }

    private void reloadData() {
        grid.setItems( Company.getInstance().getStaff() );
    }


    // Service Endpoints
    /*
    server:port/path
    localhost:8080/employee-list
    localhost:8080/employee/list
    localhost:8080/company/list
    localhost:8080/company/edit
    localhost:8080/employee/edit/[dynamisch]

    localhost:8080/employee/edit/100

    localhost:8080/employee-edit?eid=100
     */
}
