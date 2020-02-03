package it.easytechs.difacile.communication.resources;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.ApiOperation;
import it.easytechs.difacile.common.auth.model.User;
import it.easytechs.difacile.common.core.logging.DiFacileLogger;
import it.easytechs.difacile.common.core.logging.DiFacileLoggerFactory;
import it.easytechs.difacile.communication.api.common.CreateCommunication;
import it.easytechs.difacile.communication.api.response.CommunicationList;
import it.easytechs.difacile.communication.core.CommunicationManager;
import it.easytechs.difacile.communication.core.exception.CommunicationException;
import it.easytechs.difacile.communication.db.entities.Communication;
import it.easytechs.difacile.practice.api.DiFacilePraticaErrors;
import it.easytechs.difacile.practice.api.common.ProcedureLiteItem;
import it.easytechs.difacile.practice.api.common.ProcedureMainData;
import it.easytechs.difacile.practice.api.common.ProcedureSearchRequest;
import it.easytechs.difacile.practice.api.common.ProcedureSearchResult;
import it.easytechs.difacile.practice.api.common.ProcedureStats;
import it.easytechs.difacile.practice.api.common.StatsProfile;
import it.easytechs.difacile.practice.api.procedure.*;
import it.easytechs.difacile.practice.core.procedure.DocumentManager;
import it.easytechs.difacile.practice.core.procedure.ProcedureManager;
import it.easytechs.difacile.practice.db.entities.Procedure;
import it.easytechs.difacile.user.api.common.BasicUserData;
import it.easytechs.difacile.user.api.common.UserData;
import it.easytechs.difacile.user.core.user.CasUserManager;
import it.easytechs.difacile.user.core.user.UserManager;
import it.easytechs.difacile.user.db.entities.DiFacileUser;
import it.easytexhs.difacile.practice.utils.DocxHelper;

import javax.annotation.Nonnull;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.mysql.cj.jdbc.DocsConnectionPropsHelper;

import java.util.List;

@Path("/communication")
@Produces(MediaType.APPLICATION_JSON)
public class CommunicationResource {

	private static final DiFacileLogger logger= DiFacileLoggerFactory.getLogger(CommunicationResource.class.getCanonicalName());
    private CommunicationManager communicationManager;
    private CasUserManager casUserManager;
    private UserManager userManager;
    
	public CommunicationResource(CommunicationManager communicationManager, CasUserManager casUserManager, UserManager userManager) {
		this.communicationManager=communicationManager;
		this.casUserManager=casUserManager;
		this.userManager=userManager;
	}

	/*@GET
	@Path("/list")
	@ApiOperation(value = "List of user communication", response = CommunicationList.class)
	public Response communicationsList(){
		return Response.ok("").build();
	}*/
	
	
	
	@GET
	@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
	@Path("/list")
	@ApiOperation(value = "List of user communication", response = CommunicationList.class)
	public Response communicationsList(@Auth User user){
		logger.info(" communicationsList--> start user: " + user);

		DiFacileUser myCompany = userManager.getProfile(user);
		
		List<Communication> communications = this.communicationManager.getCommunications(new Long(myCompany.getCompanyName().getVatNumber()));
		
		for (Communication entity: communications) {
			
			it.easytechs.difacile.user.api.registration.CompanyName customer = null;
			try {
				if (entity.getFromUserId() != new Long(myCompany.getCompanyName().getVatNumber())) {
					customer = userManager.getCompanyName(entity.getFromUserId());
					entity.setCustomerName(customer.getBusinessName());
				} else if (entity.getToUserId() != new Long(myCompany.getCompanyName().getVatNumber())) {
					customer = userManager.getCompanyName(entity.getToUserId());
					entity.setCustomerName(customer.getBusinessName());
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		CommunicationList output = new CommunicationList();
		output.getList().addAll(communications);
		
		logger.info("communicationsList --> end");

		return Response.ok(output).build();
	}
	
	@POST
	@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
	@Path("/new")
	@ApiOperation(value = "Create new communication from user to user")
	public Response createCommunication(@Auth User user, CreateCommunication createCommunication){
		logger.info(" createCommunication--> start user: " + user);

		try {
		
			DiFacileUser difacileUser = userManager.getProfile(user);
			createCommunication.setFromUserId(new Long(difacileUser.getCompanyName().getVatNumber()));
			this.communicationManager.createCommunication(createCommunication);

			logger.info("createCommunication --> end");
			return Response.ok().build();
			
		} catch (CommunicationException ce) {
			logger.error(ce.getMessage(), ce);
			return Response.serverError().build();
		}
		

	}

}