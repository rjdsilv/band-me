package ca.me.band.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * This configuration class will configure the hibernated based on a HikariCP connection pool and also the
 * hibernate transaction manager in order to enable auto session creation.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@EnableTransactionManagement
@PropertySource({
		"classpath:connection-pool.properties",
		"classpath:hibernate.properties"
})
public class HibernateConfig {
	/**
	 * Configures a HikariCP connection pool data source bean.
	 *
	 * @param env The spring environment to deal with properties files.
	 * @return The created data source.
	 */
	@Bean(destroyMethod = "close")
	@Autowired
	public HikariDataSource dataSource(Environment env) {
		final HikariDataSource dataSource = new HikariDataSource();
		final int poolSize = Integer.valueOf(env.getProperty("pool.max.size"));

		dataSource.setMinimumIdle(poolSize);
		dataSource.setMaximumPoolSize(poolSize);
		dataSource.setJdbcUrl(env.getProperty("pool.jdbcUrl"));
		dataSource.setDriverClassName(env.getProperty("hibernate.connection.driver_class"));
		dataSource.addDataSourceProperty("user", env.getProperty("pool.user"));
		dataSource.addDataSourceProperty("password", env.getProperty("pool.password"));
		dataSource.addDataSourceProperty("cachePrepStmts", true);
		dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
		dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
		dataSource.addDataSourceProperty("useLocalSessionState", true);
		dataSource.addDataSourceProperty("useLocalTransactionState", true);
		dataSource.addDataSourceProperty("rewriteBatchedStatements", true);
		dataSource.addDataSourceProperty("cacheResultSetMetadata", true);
		dataSource.addDataSourceProperty("cacheServerConfiguration", true);
		dataSource.addDataSourceProperty("elideSetAutoCommits", true);
		dataSource.addDataSourceProperty("maintainTimeStats", false);
		dataSource.setAutoCommit(false);

		return dataSource;
	}

	@Bean
	public Properties hibernateProperties(Environment env) {
		final Properties properties = new Properties();

		properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		properties.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
		properties.setProperty("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
		properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));

		return properties;
	}

	/**
	 * Configures a hibernate session factory bean.
	 *
	 * @param dataSource          The data source to be used by the session factory.
	 * @param hibernateProperties The hibernate properties to be set.
	 * @return The created session factory.
	 */
	@Bean
	@Autowired
	public LocalSessionFactoryBean sessionFactory(HikariDataSource dataSource, Properties hibernateProperties) {
		final LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();

		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setHibernateProperties(hibernateProperties);
		sessionFactoryBean.setPackagesToScan("ca.me.band.model");

		return sessionFactoryBean;
	}

	/**
	 * Enables spring transaction manager for hibernate. This will enable the @Transactional annotation.
	 *
	 * @param sessionFactory The spring session factory manager to be managed.
	 * @return The created transaction manager.
	 */
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(LocalSessionFactoryBean sessionFactory) {
		final HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory.getObject());
		return txManager;
	}
}
