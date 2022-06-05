package ua.edu.sumdu.j2se.kikhtenkoDmytro;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jolbox.bonecp.BoneCPDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.FileQueryLoader;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.oracle.UserQueries;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Authorization;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.UserData;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.VerifiedDuration;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.Authority;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.jwt.JwtProcessor;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.UserService;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util.ServiceExceptionWrapper;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util.oracle.OracleExceptionWrapper;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableWebMvc
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages="ua.edu.sumdu.j2se.kikhtenkoDmytro")
public class WebAppConfig {
    private static final Logger logger =
            LoggerFactory.getLogger(WebAppConfig.class);
    private UserService userService;
    private final VerifiedDuration databaseWaitingTime;
    private Authorization adminUser;

    public WebAppConfig() {
        databaseWaitingTime = new VerifiedDuration(
                "Database availability waiting time",
                Duration.ofSeconds(10), true
        );
    }

    @Autowired
    public void setUserService(
            @Qualifier("defaultUserService")
            UserService accessService
    ) {
        this.userService = accessService;
    }

    public UserService getUserService() {
        return userService;
    }

    public Duration getDatabaseWaitingTime() {
        return databaseWaitingTime.getValue();
    }

    public void setDatabaseWaitingTime(
            @NonNull Duration databaseWaitingTime) {
        this.databaseWaitingTime.setValue(databaseWaitingTime);
    }

    @Autowired
    public void setDatabaseWaitingTime(
            @Value("${dbWaitingTime}") int seconds
    ) {
        this.databaseWaitingTime.setValue(Duration.ofSeconds(seconds));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Primary
    @Bean
    public BoneCPDataSource dataSource(
            @Value("${dbUrl}") String url,
            @Value("${dbUser}") String username,
            @Value("${dbPassword}") String password
    ) throws NamingException, ClassNotFoundException {
        if(url == null || url.equals("")) {
            throw new IllegalStateException(
                    "Database url is empty");
        }
        if(username == null || username.equals("")) {
            throw new IllegalStateException(
                    "Database username is empty");
        }
        if(password == null || password.equals("")) {
            throw new IllegalStateException(
                    "Database password is empty");
        }

        Class.forName("oracle.jdbc.driver.OracleDriver");
        InitialContext ic = new InitialContext();
        Context xmlContext = (Context) ic.lookup("java:comp/env");
        BoneCPDataSource datasource = (BoneCPDataSource) xmlContext.
                lookup("jdbc/bone-cp-datasource");
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setJdbcUrl(url);
        return datasource;
    }

    @Bean
    public FileQueryLoader defaultQueryLoader() {
        return new FileQueryLoader("/sql/", ".sql");
    }

    @Bean
    public FileQueryLoader userQueryLoader() {
        return new FileQueryLoader("/sql/user/", ".sql");
    }

    @Bean
    public FileQueryLoader taskQueryLoader() {
        return new FileQueryLoader("/sql/task/", ".sql");
    }

    @Bean
    public List<ServiceExceptionWrapper> exceptionWrapper() {
        LinkedList<ServiceExceptionWrapper> list = new LinkedList<>();
        list.add(new OracleExceptionWrapper());
        return list;
    }

    @Bean
    public UserService defaultUserService(
            PasswordEncoder encoder,
            JwtProcessor processor,
            UserQueries userQueries,
            ServiceExceptionWrapper wrapper) {
        return new UserService(
                encoder,
                processor,
                userQueries,
                wrapper
        );
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().
                configure(DeserializationFeature.
                        FAIL_ON_UNKNOWN_PROPERTIES, false)
                .addModule(new JavaTimeModule()).
                build();
    }

    @Autowired
    public Authorization createAdminUser(
            @Value("${adminName}") String adminName,
            @Value("${adminPassword}") String adminPassword
    ) {
        adminUser = new Authorization();
        adminUser.setName(adminName);
        adminUser.setPassword(adminPassword);
        adminUser.setEnabled(true);
        adminUser.setAuthorities(Authority.getAllAuthorities());
        return adminUser;
    }

    @PostConstruct
    public void checkDatabase() throws InterruptedException {
        boolean connectionTested = false;

        while(!connectionTested) {
            try {
                userService.getEntityQueries().getProvider().connect();
                connectionTested = true;
            } catch (SQLException e) {
                logger.info("Database is not available. " +
                        "Awaiting for database availability...");
            }
            finally {
                userService.getEntityQueries().getProvider().disconnect();
            }
            if(!connectionTested) {
                Thread.sleep(databaseWaitingTime.getValue().toMillis());
            }
        }

        try {
            UserData adminUser = new UserData();
            userService.updateUserData(this.adminUser, adminUser);
            UserData actualUser = userService.get(
                    adminUser.getName(), true, false);
            if(actualUser == null) {
                logger.debug("Admin user '" + adminUser.getName() +
                        "' not found. Creating new admin user...");
                userService.add(adminUser);
                logger.debug("Admin user '" + adminUser.getName() +
                        "' successfully created");
            } else if(actualUser.isEnabled() != actualUser.isEnabled()
                    && !actualUser.getAuthorities().equals(
                            adminUser.getAuthorities())) {
                logger.debug("Admin user '" + adminUser.getName() +
                        "' has been changed. Rolling back user " +
                        "data...");
                adminUser.setPassword(actualUser.getPassword());
                userService.update(actualUser.getId(), adminUser);
                logger.debug("Admin user '" + adminUser.getName() +
                        "' data successfully updated");
            }
        } catch (ServiceException e) {
            logger.error("Error checking admin user " +
                    "actuality. " + e.getMessage());
        }
    }

    @PostConstruct
    public void enableCleanJwtThread() {
        userService.getJwtProcessor().getSecretKeeper().
                enableCleanThread();
    }
}
