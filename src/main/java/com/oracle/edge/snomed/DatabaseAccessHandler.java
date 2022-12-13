package com.oracle.edge.snomed;

import com.oracle.edge.pushData.SnomedElements;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class DatabaseAccessHandler {
    
    private final EntityManagerFactory entityManagerFactory;
    private static String childrenQuery = 
	    "SELECT conceptId , term, definition \n" +
            "FROM TABLE(SEM_MATCH(' \n" +
            "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
            "PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX owl:    <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#> \n" +
            "PREFIX skos:   <http://www.w3.org/2004/02/skos/core#> \n" +
            "PREFIX :       <http://snomed.info/id/> \n" +
            "SELECT * \n" +
            "WHERE \n" +
            "{ \n" +
                "VALUES ?root { :INSERTCONCEPTIDHERE } \n" + // conceptid 64572001 stands for deseases
                "?conceptId (rdfs:subClassOf) ?root . \n" +
                "?conceptId rdfs:label ?term .  \n" +
		"OPTIONAL { \n" +
                "    ?conceptId (skos:definition) ?definition \n" +
                "} \n" +
            "} \n" +
            "ORDER BY ?term \n" +
            "LIMIT 200 ', \n" +
            "SEM_Models('SNOMED'), \n" +
            "null, \n" +
            "null, \n" +
            "null, \n" +
            "null, \n" +
            "'REWRITE=F ', \n" +
            "null,  \n" +
            "null, \n" +
            "'MDSYS', \n" +
            "'MDSYS'))";
    private static String parentsQuery = 
            "SELECT conceptId , term, definition \n" +
            "FROM TABLE(SEM_MATCH(' \n" +
            "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
            "PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX owl:    <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#> \n" +
            "PREFIX skos:   <http://www.w3.org/2004/02/skos/core#> \n" +
            "PREFIX :       <http://snomed.info/id/> \n" +
            "SELECT * \n" +
            "WHERE \n" +
            "{ \n" +
	        ":INSERTCONCEPTIDHERE (rdfs:subClassOf) ?conceptId . \n"+
                "?conceptId (rdfs:label) ?term . \n"+
		"OPTIONAL { \n" +
                "    ?conceptId (skos:definition) ?definition \n" +
                "} \n" +
            "} \n" +
            "ORDER BY ?term \n" +
            "LIMIT 200 ', \n" +
            "SEM_Models('SNOMED'), \n" +
            "null, \n" +
            "null, \n" +
            "null, \n" +
            "null, \n" +
            "'REWRITE=F ', \n" +
            "null,  \n" +
            "null, \n" +
            "'MDSYS', \n" +
            "'MDSYS'))";
    private static String textQueryFull = 
            "SELECT conceptId , term, definition \n" +
            "FROM TABLE(SEM_MATCH(' \n" +
            "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
            "PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX owl:    <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#> \n" +
            "PREFIX skos:   <http://www.w3.org/2004/02/skos/core#> \n" +
            "PREFIX :       <http://snomed.info/id/> \n" +
            "SELECT * \n" +
            "WHERE \n" +
            "{ \n" +
                "?conceptId rdfs:label ?term .  \n" +
		"OPTIONAL { \n" +
                "    ?conceptId (skos:definition) ?definition \n" +
                "} . \n" +
		"FILTER (orardf:textContains (?term, \"%INSERTTEXTHERE%\")) \n"+
            "} \n" +
            "ORDER BY ?term \n" +
            "LIMIT 200 ', \n" +
            "SEM_Models('SNOMED'), \n" +
            "null, \n" +
            "null, \n" +
            "null, \n" +
            "null, \n" +
            "'REWRITE=F ', \n" +
            "null,  \n" +
            "null, \n" +
            "'MDSYS', \n" +
            "'MDSYS'))";

    private static String textQueryChildren = 
            "SELECT conceptId , term, definition \n" +
            "FROM TABLE(SEM_MATCH(' \n" +
            "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
            "PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX owl:    <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#> \n" +
            "PREFIX skos:   <http://www.w3.org/2004/02/skos/core#> \n" +
            "PREFIX :       <http://snomed.info/id/> \n" +
            "SELECT * \n" +
            "WHERE \n" +
            "{ \n" +
                "VALUES ?root { :INSERTCONCEPTIDHERE } \n" + // conceptid 64572001 stands for deseases
                "?conceptId (rdfs:subClassOf) ?root . \n" +
                "?conceptId rdfs:label ?term .  \n" +
		"OPTIONAL { \n" +
                "    ?conceptId (skos:definition) ?definition \n" +
                "} . \n" +
		"FILTER (orardf:textContains (?term, \"%INSERTTEXTHERE%\")) \n"+
            "} \n" +
            "ORDER BY ?term \n" +
            "LIMIT 200 ', \n" +
            "SEM_Models('SNOMED'), \n" +
            "null, \n" +
            "null, \n" +
            "null, \n" +
            "null, \n" +
            "'REWRITE=F ', \n" +
            "null,  \n" +
            "null, \n" +
            "'MDSYS', \n" +
            "'MDSYS'))";


    
    DatabaseAccessHandler(
            final EntityManagerFactory theEntityManagerFactory) {
        entityManagerFactory = theEntityManagerFactory;
    }

    public List<SnomedElements> children(String conceptId) {
	String myQuery = childrenQuery.replace("INSERTCONCEPTIDHERE", conceptId);
        return entityManagerFactory
                .createEntityManager()
                .createNativeQuery(
                        myQuery,
                        SnomedElements.class).getResultList();
    }
    
    public List<SnomedElements> parents(String conceptId) {
	String myQuery = parentsQuery.replace("INSERTCONCEPTIDHERE", conceptId);
        return entityManagerFactory
                .createEntityManager()
                .createNativeQuery(
                        myQuery,
                        SnomedElements.class).getResultList();
    }
    
    public List<SnomedElements> textsearch(String text) {
	String myQuery = textQueryFull.replace("INSERTTEXTHERE", text);
        return entityManagerFactory
                .createEntityManager()
                .createNativeQuery(
                        myQuery,
                        SnomedElements.class).getResultList();
    } 

    public List<SnomedElements> textsearch(String conceptId, String text) {
	String myQuery = textQueryChildren.replace("INSERTCONCEPTIDHERE", conceptId)
		                  .replace("INSERTTEXTHERE", text);
        return entityManagerFactory
                .createEntityManager()
                .createNativeQuery(
                        myQuery,
                        SnomedElements.class).getResultList();
    }
}
