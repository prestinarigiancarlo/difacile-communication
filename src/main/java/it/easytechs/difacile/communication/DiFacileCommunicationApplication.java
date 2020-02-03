package it.easytechs.difacile.communication;


import java.net.UnknownHostException;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.codahale.metrics.MetricRegistry;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import it.easytechs.difacile.common.auth.DiFacileAuthenticator;
import it.easytechs.difacile.common.auth.DiFacileAuthorizer;
import it.easytechs.difacile.common.auth.model.User;
import it.easytechs.difacile.common.core.CacheManager;
import it.easytechs.difacile.common.core.InMemoryCache;
import it.easytechs.difacile.common.core.logging.DiFacileFluentLogger;
import it.easytechs.difacile.common.core.logging.DiFacileFluentLoggerConfig;
import it.easytechs.difacile.common.core.logging.DiFacileLoggerFactory;
import it.easytechs.difacile.common.core.logging.DiFacileLoggerStandard;
import it.easytechs.difacile.common.db.repository.Repository;
import it.easytechs.difacile.common.db.repository.mongodb.MongoDbConnection;
import it.easytechs.difacile.common.email.EmailSender;
import it.easytechs.difacile.common.email.impl.LoggerEmailSender;
import it.easytechs.difacile.common.email.impl.SendGridEmailSender;
import it.easytechs.difacile.common.payment.DiFacilePayment;
import it.easytechs.difacile.common.payment.braintree.BraintreePayment;
import it.easytechs.difacile.common.payment.paypal.PayPalPayment;
import it.easytechs.difacile.communication.core.CommunicationManager;
import it.easytechs.difacile.communication.core.CommunicationManagerImpl;
import it.easytechs.difacile.communication.resources.CommunicationResource;
import it.easytechs.difacile.practice.core.procedure.ProcedureManager;
import it.easytechs.difacile.practice.core.procedure.ProcedureManagerImpl;
import it.easytechs.difacile.practice.db.repository.mongodb.ProcedureRepository;
import it.easytechs.difacile.user.DiFacileUserConfiguration;
import it.easytechs.difacile.user.DiFacileUserFactory;
import it.easytechs.difacile.user.core.registration.RegistrationManager;
import it.easytechs.difacile.user.core.registration.RegistrationManagerImpl;
import it.easytechs.difacile.user.core.user.CasUserManager;
import it.easytechs.difacile.user.core.user.CasUserManagerImpl;
import it.easytechs.difacile.user.core.user.UserManager;
import it.easytechs.difacile.user.core.user.UserManagerImpl;
import it.easytechs.difacile.user.db.repository.mongodb.CasUserRepository;
import it.easytechs.difacile.user.db.repository.mongodb.RegisteredUsersRepository;
import it.easytechs.difacile.user.db.repository.mongodb.UserRepository;
import it.easytechs.difacile.user.health.CacheManagerHealthCheck;
import it.easytechs.difacile.user.resources.HelloWorldResource;
import it.easytechs.difacile.user.resources.PaymentResource;
import it.easytechs.difacile.user.resources.PrivateResource;
import it.easytechs.difacile.user.resources.UserResource;



public class DiFacileCommunicationApplication  extends Application<DiFacileCommunicationConfiguration> {

	public static void main(final String[] args) throws Exception {
        new DiFacileCommunicationApplication().run(args);
    }

	@Override
    public String getName() {
        return "DiFacile - Communication";
    }

    @Override
    public void initialize(final Bootstrap<DiFacileCommunicationConfiguration> bootstrap) {
    }

    @Override
    public void run(final DiFacileCommunicationConfiguration configuration,
                    final Environment environment) throws UnknownHostException {

        MetricRegistry metricRegistry = new MetricRegistry();

        //Authentication init
        environment.jersey().register(new AuthDynamicFeature(
            new OAuthCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new DiFacileAuthenticator(configuration.getJwtSettings()))
                .setAuthorizer(new DiFacileAuthorizer())
                .setPrefix("Bearer")
                .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        //If you want to use @Auth to inject a custom Principal type into your resource
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        

        //Fluentd and Logging
        if(configuration.getFluentdApp()!=null && configuration.getFluentdHost()!=null && configuration.getFluentdPort()>0) {
            DiFacileFluentLogger diFacileFluentLogger=null;
            DiFacileFluentLoggerConfig config=new DiFacileFluentLoggerConfig(configuration.getFluentdApp(), configuration.getFluentdHost(), configuration.getFluentdPort());
            diFacileFluentLogger = new DiFacileFluentLogger(config);
            if(diFacileFluentLogger.test()){
                DiFacileLoggerFactory.initialize(diFacileFluentLogger,config);
            }else{
                DiFacileLoggerFactory.initialize(new DiFacileLoggerStandard("DiFacileCommunication"),null);
            }
        }else{
            DiFacileLoggerFactory.initialize(new DiFacileLoggerStandard("DiFacileCommunication"),null);
        }

        //MongoDB
        //TODO: togliere il throws a livello di application
        MongoDbConnection mongoDbConnection = new MongoDbConnection(configuration.getMongoDbFactory().getUrl(),
                configuration.getMongoDbFactory().getDatabase(),
                configuration.getMongoDbFactory().getUser(),
                configuration.getMongoDbFactory().getPassword());
        
        MongoDbConnection casMongoDbConnection = new MongoDbConnection(configuration.getCasMongoDbFactory().getUrl(),
                configuration.getCasMongoDbFactory().getDatabase(),
                configuration.getCasMongoDbFactory().getUser(),
                configuration.getCasMongoDbFactory().getPassword());
        
        //Email settings
        EmailSender emailSender = null;
        if(configuration.getEmailSettings()!=null){
            emailSender=new SendGridEmailSender(configuration.getEmailSettings());
        }else{
            emailSender = new LoggerEmailSender();
        }

        //Factory
        CacheManager cacheManager = new InMemoryCache();
        Repository registeredUserRepository = new RegisteredUsersRepository(mongoDbConnection,"registeredUsers");
        Repository casUserRepository = new CasUserRepository(casMongoDbConnection,"users");
        Repository userRepository = new UserRepository(mongoDbConnection,"users");

        DiFacileUserFactory diFacileUserFactory = new DiFacileUserFactory(
                cacheManager
        );
        RegistrationManager registrationmanager = new RegistrationManagerImpl(cacheManager, registeredUserRepository);
        CasUserManager casUserManager = new CasUserManagerImpl(casUserRepository, emailSender, configuration.getIdpSettings());
        UserManager userManager = new UserManagerImpl(userRepository, casUserRepository);

        final UserResource userResource = new UserResource(
                diFacileUserFactory,
                registrationmanager,
                casUserManager,
                userManager);
        environment.jersey().register(userResource);

        final PrivateResource privateResource = new PrivateResource(
                userManager,
                casUserManager);
        environment.jersey().register(privateResource);

        /*final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        jdbi.installPlugin(new SqlObjectPlugin());
        environment.jersey().register(new UserDAO(jdbi));*/

        /*ProcedureRepository procedureRepository = new ProcedureRepository(mongoDbConnection,"procedures");        
        ProcedureManager procedureManager = new ProcedureManagerImpl(procedureRepository);*/
        
        CommunicationManager communicationManager = new CommunicationManagerImpl();
        CommunicationResource communicationResource = new CommunicationResource(
        		communicationManager, casUserManager, userManager);

        environment.jersey().register(communicationResource);
        
        //Healt check

        final CacheManagerHealthCheck cacheManagerHealthCheck=
                new CacheManagerHealthCheck(diFacileUserFactory);
        environment.healthChecks().register("cacheManager", cacheManagerHealthCheck);
        
    }

}
