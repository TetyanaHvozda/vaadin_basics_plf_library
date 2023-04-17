package at.spengergasse.company1.views;


import at.spengergasse.company1.views.company.CompanyForm;
import at.spengergasse.company1.views.employeeform.EmployeeCreateView;
import at.spengergasse.company1.views.employeelist.BookListView;
import at.spengergasse.company1.views.employeelist.EmployeeListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Übungen - GUI, Layout");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private Component createNavigation() {

        // Neue Views hier registrieren
        VerticalLayout navLayout = new VerticalLayout(

                create("Company", CompanyForm.class, VaadinIcon.BUILDING ),
                create("New Employee", EmployeeCreateView.class, VaadinIcon.USER_CHECK ),
                create("Employee List", EmployeeListView.class, VaadinIcon.LIST ),
                create("Book List", BookListView.class, VaadinIcon.LIST)
        );
        navLayout.setWidthFull();
        navLayout.setPadding(true);

        navLayout.setSpacing(false);
        return navLayout;
    }

    private RouterLink create(String label, Class<? extends Component> view, VaadinIcon icon) {
        RouterLink link = new RouterLink( view );
        link.getElement().setAttribute("part", "button");
        link.addClassName("nav-item");
        link.setHighlightCondition( HighlightConditions.locationPrefix()  );
        link.add( new Icon( icon ), new Text(" " + label));
        return link;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
