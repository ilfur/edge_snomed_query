package com.oracle.edge.snomed;

import jakarta.enterprise.context.Dependent;

import jakarta.inject.Inject;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// Implementation based on: https://stackoverflow.com/questions/65075371/how-to-upload-file-to-oracle-db-as-blob-clob-using-helidon-mp-rest-services
//
// Other Info: https://frameworks.readthedocs.io/en/latest/rest/jerseyFileUpload.html
@Dependent
@Path("/edge/snomed/concepts")
public final class SnomedQueries {

    /**
     * Used for logging.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
            SnomedQueries.class);
    
    private DatabaseAccessHandler dbHandler = null;

    /**
     * Creates the instance.
     */
    @Inject
    public SnomedQueries(final DatabaseAccessHandler theDbHandler) {
	 dbHandler = theDbHandler;
    }

    @GET
    @Path("/disorder/children")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deseaseChildren() {
	    return genericChildren("64572001");
    }

    @GET
    @Path("/{conceptId}/children")
    @Produces(MediaType.APPLICATION_JSON)
    public Response genericChildren( @PathParam("conceptId")
            final String conceptId) {
        JsonArray resultList = dbHandler.children(conceptId).stream()
                .map((u) -> {
                    return Json.createObjectBuilder()
                            .add("conceptId", u.getConceptId())
                            .add("term", u.getTerm())
                            .add("definition", (u.getDefinition()==null?"":u.getDefinition()))
                            .build();
                })
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(resultList, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{conceptId}/parent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response genericParents( @PathParam("conceptId")
            final String conceptId) {
        JsonArray resultList = dbHandler.parents(conceptId).stream()
                .map((u) -> {
                    return Json.createObjectBuilder()
                            .add("conceptId", u.getConceptId())
                            .add("term", u.getTerm())
                            .add("definition", (u.getDefinition()==null?"":u.getDefinition()))
                            .build();
                })
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(resultList, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{conceptId}/textsearch")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchConcept( @PathParam("conceptId") final String conceptId,
		                    @QueryParam("text")  final String text) {
        JsonArray resultList = dbHandler.textsearch(conceptId, text).stream()
                .map((u) -> {
                    return Json.createObjectBuilder()
                            .add("conceptId", u.getConceptId())
                            .add("term", u.getTerm())
                            .add("definition", (u.getDefinition()==null?"":u.getDefinition()))
                            .build();
                })
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(resultList, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/textsearch")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchAll( 
		                    @QueryParam("text")  final String text) {
        JsonArray resultList = dbHandler.textsearch(text).stream()
                .map((u) -> {
                    return Json.createObjectBuilder()
                            .add("conceptId", u.getConceptId())
                            .add("term", u.getTerm())
                            .add("definition", (u.getDefinition()==null?"":u.getDefinition()))
                            .build();
                })
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(resultList, MediaType.APPLICATION_JSON).build();
    }
}

