package com.sb.services.common.config;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sb.services.common.interceptor.HibernateInterceptor;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author: Priti
 * @version $Id$
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Value("${jdbc.driver}")
    private String driverClass;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.user}")
    private String user;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.acquireIncrement}")
    private int acquireIncrement;
    @Value("${jdbc.initialPoolSize}")
    private int initialPoolSize;
    @Value("${jdbc.minPoolSize}")
    private int minPoolSize;
    @Value("${jdbc.maxPoolSize}")
    private int maxPoolSize;
    @Value("${jdbc.maxStatementsPerConnection}")
    private int maxStatementsPerConnection;
    @Value("${jdbc.maxIdleTime}")
    private int maxIdleTime;
    @Value("${jdbc.acquireRetryAttempts}")
    private int acquireRetryAttempts;
    @Value("${jdbc.acquireRetryDelay}")
    private int acquireRetryDelay;
    @Value("${jdbc.breakAfterAcquireFailure}")
    private boolean breakAfterAcquireFailure;
    @Value("${jdbc.numHelperThreads}")
    private int numHelperThreads;
    @Value("${jdbc.maxAdministrativeTaskTime}")
    private int maxAdministrativeTaskTime;
    @Value("${jdbc.checkoutTimeout}")
    private int checkoutTimeout;
    @Value("${jdbc.idleConnectionTestPeriod}")
    private int idleConnectionTestPeriod;
    @Value("${jdbc.debugUnreturnedConnectionStackTraces}")
    private boolean debugUnreturnedConnectionStackTraces;
    @Value("${jdbc.unreturnedConnectionTimeout}")
    private int unreturnedConnectionTimeout;
    @Value("${jdbc.preferredTestQuery}")
    private String preferredTestQuery;
    @Value("${jdbc.dialect}")
    private String jdbcDialect;
    @Value("${jdbc.checkIfTablesExist}")
    private boolean checkIfTablesExist;
    @Value("${jdbc.connReleaseModeToAfterTransaction}")
    private boolean connReleaseModeToAfterTransaction;
    @Value("${jdbc.packages.to.scan}")
    private String[] packagesToScan;

    @Bean
    @Primary
    public DataSource dataSource () {
        try {
            ComboPooledDataSource ds = new ComboPooledDataSource();
            ds.setDriverClass(driverClass);
            
            final String DB_HOST = System.getenv("DB_HOST");
            if(DB_HOST !=null) { 
              jdbcUrl = jdbcUrl.replaceFirst("//[a-z0-9-.]*", "//" + DB_HOST); 
            }
            
            ds.setJdbcUrl(jdbcUrl);
            ds.setUser(user);
            ds.setPassword(password);
            ds.setAcquireIncrement(acquireIncrement);
            ds.setInitialPoolSize(initialPoolSize);
            ds.setMinPoolSize(minPoolSize);
            ds.setMaxPoolSize(maxPoolSize);
            ds.setMaxStatementsPerConnection(maxStatementsPerConnection);
            ds.setMaxIdleTime(maxIdleTime);
            ds.setAcquireRetryAttempts(acquireRetryAttempts);
            ds.setAcquireRetryDelay(acquireRetryDelay);
            ds.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
            ds.setNumHelperThreads(numHelperThreads);
            ds.setMaxAdministrativeTaskTime(maxAdministrativeTaskTime);
            ds.setCheckoutTimeout(checkoutTimeout);
            ds.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
            ds.setDebugUnreturnedConnectionStackTraces(debugUnreturnedConnectionStackTraces);
            ds.setUnreturnedConnectionTimeout(unreturnedConnectionTimeout);
            ds.setPreferredTestQuery(preferredTestQuery);
            return ds;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Bean
    @Primary
    @Autowired
    public LocalSessionFactoryBean sessionFactory (DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan(packagesToScan);
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setEntityInterceptor(new HibernateInterceptor());
        return sessionFactory;
    }

    @Bean
    @Primary
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        txManager.setNestedTransactionAllowed(true);

        return txManager;
    }

    @Bean
    @Primary
    @Autowired
    public HibernateTemplate hibernateTemplate(SessionFactory sessionFactory){
        HibernateTemplate hibernateTemplate = new HibernateTemplate();
        hibernateTemplate.setSessionFactory(sessionFactory);
        return hibernateTemplate;
    }

    public Properties hibernateProperties(){
        return new Properties(){
            {
                setProperty("hibernate.jdbc.batch_size","30");
                setProperty("hibernate.query.substitutions","true 'Y', false 'N'");
                setProperty("hibernate.cache.provider_class","org.hibernate.cache.EhCacheProvider");
                setProperty("hibernate.order_updates","true");
                setProperty("hibernate.generate_statistics","false");
                setProperty("hibernate.jdbc.use_scrollable_resultset","true");
                setProperty("hibernate.dialect",jdbcDialect);
            }
        };
    }

    /**
     * PersistenceExceptionTranslationPostProcessor is a bean post processor
     * which adds an advisor to any bean annotated with Repository so that any
     * platform-specific exceptions are caught and then rethrown as one
     * Spring's unchecked data access exceptions (i.e. a subclass of
     * DataAccessException).
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public HibernateInterceptor hibernateInterceptor(){
        return new HibernateInterceptor();
    }
}

