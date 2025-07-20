package ru.practicum.shareit.config;


//@EnableJpaRepositories(basePackages = "ru.practicum.shareit")
//@Configuration
//@RequiredArgsConstructor
public class PersistenceConfig {
//    private final Environment environment; // внедряем экземпляр Environment
//
//    // здесь будут определения бинов
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
//        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
//        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
//        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
//        return dataSource;
//    }
//
//    private Properties hibernateProperties() {
//        Properties properties = new Properties();
//        properties.put("hibernate.jdbc.time_zone",
//                environment.getRequiredProperty("hibernate.jdbc.time_zone"));
//        properties.put("hibernate.show_sql",
//                environment.getProperty("hibernate.show_sql", "false"));
//        return properties;
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//        final HibernateJpaVendorAdapter vendorAdapter =
//                new HibernateJpaVendorAdapter();
//
//        final LocalContainerEntityManagerFactoryBean emf =
//                new LocalContainerEntityManagerFactoryBean();
//
//        emf.setDataSource(dataSource);
//        emf.setJpaVendorAdapter(vendorAdapter);
//        emf.setPackagesToScan("ru.practicum.shareit");
//        emf.setJpaProperties(hibernateProperties());
//
//        return emf;
//    }
//
//    @Bean
//    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory);
//        return transactionManager;
//    }
}