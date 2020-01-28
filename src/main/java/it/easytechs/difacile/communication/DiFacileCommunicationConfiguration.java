package it.easytechs.difacile.communication;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilderSpec;

import io.dropwizard.Configuration;
import it.easytechs.difacile.common.db.repository.mongodb.MongoDbFactory;
import it.easytechs.difacile.common.email.EmailSettings;
import it.easytechs.difacile.common.jwt.JwtSettings;
import it.easytechs.difacile.common.payment.braintree.BraintreeSettings;
import it.easytechs.difacile.common.payment.paypal.PayPalSettings;
import it.easytechs.difacile.user.auth.IdpSettings;


public class DiFacileCommunicationConfiguration extends Configuration {

	//Fluentd Settings
	private String fluentdApp;
	private String fluentdHost;
    private int fluentdPort;
    
    public String getFluentdApp() {
		return fluentdApp;
	}
	@JsonProperty
	public void setFluentdApp(String fluentdApp) {
		this.fluentdApp = fluentdApp;
	}
	public String getFluentdHost() {
		return fluentdHost;
	}
	@JsonProperty
	public void setFluentdHost(String fluentdHost) {
		this.fluentdHost = fluentdHost;
	}

	public int getFluentdPort() {
		return fluentdPort;
	}
	@JsonProperty
	public void setFluentdPort(int fluentdPort) {
		this.fluentdPort = fluentdPort;
	}
	
	//JWT
	private JwtSettings jwtSettings;
	@JsonProperty("jwt")
	public JwtSettings getJwtSettings() {
		return jwtSettings;
	}
	@JsonProperty("jwt")
	public void setJwtSettings(JwtSettings jwtSettings) {
		this.jwtSettings = jwtSettings;
	}
	
	//Auth
	private CacheBuilderSpec authenticationCachePolicy;
	

	@JsonProperty("authenticationCachePolicy")
	public CacheBuilderSpec getAuthenticationCachePolicy(){
		return this.authenticationCachePolicy;
	}
	@JsonProperty("authenticationCachePolicy")
	public void setAuthenticationCachePolicy(CacheBuilderSpec authenticationCachePolicy){
		this.authenticationCachePolicy=authenticationCachePolicy;
	}


	//Email Settings
	private EmailSettings emailSettings;
	@JsonProperty("email")
	public EmailSettings getEmailSettings() {
		return emailSettings;
	}

	@JsonProperty("email")
	public void setEmailSettings(EmailSettings emailSettings) {
		this.emailSettings = emailSettings;
	}

	
	private IdpSettings idpSettings;
	@JsonProperty("idp")
	public IdpSettings getIdpSettings() {
		return idpSettings;
	}
	@JsonProperty("idp")
	public void setIdpSettings(IdpSettings idpSettings) {
		this.idpSettings = idpSettings;
	}

	//MongoDB Settings
	private MongoDbFactory mongoDbFactory;

	@JsonProperty("mongo")
	public MongoDbFactory getMongoDbFactory() {
		return mongoDbFactory;
	}
	@JsonProperty("mongo")
	public void setMongoDbFactory(MongoDbFactory mongoDbFactory) {
		this.mongoDbFactory = mongoDbFactory;
	}

	//MongoDB Settings
	private MongoDbFactory casMongoDbFactory;

	@JsonProperty("casMongo")
	public MongoDbFactory getCasMongoDbFactory() {
		return casMongoDbFactory;
	}
	@JsonProperty("casMongo")
	public void setCasMongoDbFactory(MongoDbFactory casMongoDbFactory) {
		this.casMongoDbFactory = casMongoDbFactory;
	}
	
}