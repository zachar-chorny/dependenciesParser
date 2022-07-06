package com.example.parse.config;

import com.example.parse.model.RepositoriesDto;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.jboss.shrinkwrap.resolver.impl.maven.bootstrap.MavenRepositorySystem;
import org.jboss.shrinkwrap.resolver.impl.maven.internal.MavenModelResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class AppConfig {
    private static final String TARGET_LOCAL_REPOSITORY = "target/local-repository";
    private static final String REMOTE_REPOSITORY_URL = "https://repo1.maven.org/maven2/";

    @Bean
    public RepositorySystem getRepositorySystem() {
        DefaultServiceLocator serviceLocator = MavenRepositorySystemUtils.newServiceLocator();
        serviceLocator
                .addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        serviceLocator.addService(TransporterFactory.class, FileTransporterFactory.class);
        serviceLocator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        return serviceLocator.getService(RepositorySystem.class);
    }

    @Bean
    public DefaultRepositorySystemSession getRepositorySystemSession(RepositorySystem system) {
        DefaultRepositorySystemSession repositorySystemSession = MavenRepositorySystemUtils
                .newSession();
        LocalRepository localRepository = new LocalRepository(TARGET_LOCAL_REPOSITORY);
        repositorySystemSession.setLocalRepositoryManager(
                system.newLocalRepositoryManager(repositorySystemSession, localRepository));
        return repositorySystemSession;
    }

    @Bean
    public MavenXpp3Reader getMavenReader() {
        return new MavenXpp3Reader();
    }

    @Bean
    public RepositoriesDto getRepositories() {
        return new RepositoriesDto(Arrays.asList(getCentralMavenRepository()));
    }

    @Bean
    public ModelResolver getResolver(RepositoriesDto repositoriesDto, RepositorySystemSession session) {
        return new MavenModelResolver(new MavenRepositorySystem(), session,
                repositoriesDto.getRepositories());
    }

    @Bean
    public ModelBuildingRequest getModelBuildingRequest(ModelResolver modelResolver) {
        return new DefaultModelBuildingRequest()
                .setProcessPlugins(false).setSystemProperties(System.getProperties())
                .setModelResolver(modelResolver);
    }

    @Bean
    public DefaultModelBuilderFactory getModelBuilder(){
        return new DefaultModelBuilderFactory();
    }

    private RemoteRepository getCentralMavenRepository() {
        return new RemoteRepository.Builder("central", "default", REMOTE_REPOSITORY_URL)
                .build();
    }
}
