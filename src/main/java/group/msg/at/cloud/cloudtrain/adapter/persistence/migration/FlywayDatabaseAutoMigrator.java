package group.msg.at.cloud.cloudtrain.adapter.persistence.migration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

/**
 * Flyway database auto-migrator that performs database migrations on application startup.
 * <p>
 * All migration statements must be located at {@code classpath:db/migration} which might be either
 * packaged with this application or with a utility JAR of this application.
 * </p>
 */
@Startup
@Singleton
public class FlywayDatabaseAutoMigrator {

    @Resource(lookup = "java:global/cnj-postgres-datasource")
    private DataSource dataSource;

    @PostConstruct
    public void migrateDatabase() {
        Flyway flyway = Flyway.configure().dataSource(this.dataSource).load();
        flyway.migrate();
    }
}
