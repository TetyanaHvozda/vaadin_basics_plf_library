package at.spengergasse.company1;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "company010")
//@Theme(themeClass = Material.class)
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    public static void info(String message) {
        Notification.show( message );
    }

    public static void error(Throwable exception) {
        Notification errorNoti = Notification.show( exception.getMessage() );
        errorNoti.addThemeVariants( NotificationVariant.LUMO_ERROR );
    }

    public static void error(String message) {
        Notification errorNoti = Notification.show( message );
        errorNoti.addThemeVariants( NotificationVariant.LUMO_ERROR );
    }
}
