package cl.mapuescuela.app;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;


@Configuration
public class FlowableConfig {

    @Bean
    public ProcessEngine processEngine() {
        return ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:sqlserver://mapuescueladmin.database.windows.net:1433;databaseName=MapuEscuela")
                .setJdbcUsername("mapuescueladmin")
                .setJdbcPassword("Mapu1234!")
                .setJdbcDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .buildProcessEngine();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }
}

