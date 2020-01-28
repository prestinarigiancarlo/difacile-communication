package it.easytechs.difacile.communication.resources;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.ApiOperation;
import it.easytechs.difacile.common.auth.model.User;
import it.easytechs.difacile.common.core.logging.DiFacileLogger;
import it.easytechs.difacile.common.core.logging.DiFacileLoggerFactory;
import it.easytechs.difacile.communication.api.response.CommunicationList;
import it.easytechs.difacile.communication.core.CommunicationManager;
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
import it.easytexhs.difacile.practice.utils.DocxHelper;

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
    private ProcedureManager procedureManager;
    private CommunicationManager communicationManager;
    
	public CommunicationResource(ProcedureManager procedureManager, CommunicationManager communicationManager) {
		this.procedureManager=procedureManager;
		this.communicationManager=communicationManager;
	}

	@GET
	@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
	@Path("/list")
	@ApiOperation(value = "List of user communication", response = CommunicationList.class)
	public Response communicationsList(@Auth User user){
		logger.info(" communicationsList--> start");

		List<Communication> communications = this.communicationManager.getCommunications(user);
		
		CommunicationList output = new CommunicationList();
		output.getList().addAll(communications);
		
		logger.info("communicationsList --> end");

		return Response.ok(output).build();
	}

}