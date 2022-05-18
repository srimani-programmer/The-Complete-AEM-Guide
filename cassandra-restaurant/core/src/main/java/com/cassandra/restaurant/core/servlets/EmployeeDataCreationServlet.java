package com.cassandra.restaurant.core.servlets;

import com.cassandra.restaurant.core.beans.CassandraEmployee;
import com.cassandra.restaurant.core.beans.CassandraEmployeeData;
import com.cassandra.restaurant.core.beans.JCRData;
import com.cassandra.restaurant.core.utils.ResolverUtility;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.google.gson.GsonBuilder;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Component(service = {Servlet.class})
@SlingServletPaths("/bin/cassandra/employeedata/create")
public class EmployeeDataCreationServlet extends SlingSafeMethodsServlet {

    private final static Logger LOG = LoggerFactory.getLogger(EmployeeDataCreationServlet.class);

    private final static String JCR_PATH = "/content";

    private final static String AEM_JSON_DAM_PATH = "/content/dam/cassandra/files/employeeData.json";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        ResourceResolver resolver = null;
        InputStream inputStream   = null;
        BufferedReader reader     = null;
        StringBuilder builder     = null;

        try{
            resolver = ResolverUtility.getResourceResolver(resolverFactory);
        } catch (Exception e) {
            LOG.info("Something went wrong with the resolver: {}", e);
        }

        Resource resource = resolver.getResource(AEM_JSON_DAM_PATH);

        if(resource != null){
            Asset asset = resource.adaptTo(Asset.class);
            if(asset != null){
                Rendition rendition = asset.getOriginal();

                if(rendition != null) {
                    inputStream = rendition.getStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    builder = new StringBuilder();
                    String content = "";

                    while ((content = reader.readLine()) != null){
                        builder.append(content);
                    }
                }
            }
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.disableHtmlEscaping();
        CassandraEmployeeData data = gsonBuilder.create().fromJson(builder.toString(), CassandraEmployeeData.class);

        Session session = resolver.adaptTo(Session.class);
        String employeeDataNode = JCR_PATH + "/employeeData";

        try {
            if(!session.nodeExists(employeeDataNode)){
                Node node = session.getNode(JCR_PATH);
                node.addNode("employeeData", "nt:unstructured");
                session.save();
            }
        } catch (RepositoryException e) {
            LOG.info("Received the Repository Exception: {}", e);
        }

        List<CassandraEmployee> employeeList = data.getData();

        Session employeeSession = resolver.adaptTo(Session.class);

        Node employeeSessionNode = null;
        try {
            employeeSessionNode = employeeSession.getNode(employeeDataNode);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

        for(CassandraEmployee employee : employeeList){
            try {
                Node employeeNode = employeeSessionNode.addNode(employee.getId(), "nt:unstructured");
                employeeNode.setProperty("firstName", employee.getFirstName());
                employeeNode.setProperty("lastName", employee.getLastName());
                employeeNode.setProperty("email", employee.getEmail());
                employeeNode.setProperty("gender", employee.getGender());
                employeeNode.setProperty("city", employee.getCity());
                employeeNode.setProperty("country", employee.getCountry());
                employeeNode.setProperty("id", employee.getId());
            } catch (RepositoryException e) {
                LOG.info("Repository Exception occured: {}", e);
                response.setStatus(500);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\" : \"Something went wrong\"}");
                return;
            }
        }

        try{
            employeeSession.save();
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        } catch (LockException e) {
            throw new RuntimeException(e);
        } catch (ItemExistsException e) {
            throw new RuntimeException(e);
        } catch (ReferentialIntegrityException e) {
            throw new RuntimeException(e);
        } catch (InvalidItemStateException e) {
            throw new RuntimeException(e);
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(e);
        } catch (VersionException e) {
            throw new RuntimeException(e);
        } catch (NoSuchNodeTypeException e) {
            throw new RuntimeException(e);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\" : \"Data written succesful \"}}");
    }
}
