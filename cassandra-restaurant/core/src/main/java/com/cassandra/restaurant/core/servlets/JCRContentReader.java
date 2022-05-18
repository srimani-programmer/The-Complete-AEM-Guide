package com.cassandra.restaurant.core.servlets;

import com.cassandra.restaurant.core.beans.JCRData;
import com.cassandra.restaurant.core.utils.ResolverUtility;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = {Servlet.class})
@SlingServletPaths("/bin/cassandra/jcr/contentreader")
public class JCRContentReader extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(JCRContentReader.class);

    @Reference
    private ResourceResolverFactory resolverFactory;


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String jcrPath = request.getParameter("jcrpath");

        ResourceResolver resolver = ResolverUtility.getResourceResolver(resolverFactory);

        ValueMap valueMap = resolver.getResource(jcrPath).getValueMap();

        String lastModified = valueMap.get("cq:lastModified", String.class);
        String lastModifiedBy = valueMap.get("cq:lastModifiedBy", String.class);
        String template = valueMap.get("cq:template", String.class);
        String created = valueMap.get("jcr:created", String.class);
        String createdBy = valueMap.get("jcr:createdBy", String.class);
        String primaryType = valueMap.get("jcr:primaryType", String.class);
        String title = valueMap.get("jcr:title", String.class);
        String slingResourceType = valueMap.get("sling:resourceType", String.class);

        JCRData data = new JCRData();

        data.setCreated(created);
        data.setCreatedBy(createdBy);
        data.setLastModified(lastModified);
        data.setLastModifiedBy(lastModifiedBy);
        data.setTemplate(template);
        data.setTitle(title);
        data.setPrimaryType(primaryType);
        data.setSlingResoucreType(slingResourceType);

        if(data != null){
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(data));
        }else{
            response.setStatus(404);
            response.getWriter().write("Something went wrong");

        }
    }
}
