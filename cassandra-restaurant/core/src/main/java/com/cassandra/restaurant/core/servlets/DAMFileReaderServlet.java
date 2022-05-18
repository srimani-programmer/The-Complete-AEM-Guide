package com.cassandra.restaurant.core.servlets;

import com.cassandra.restaurant.core.beans.DAMResourceData;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.google.gson.Gson;
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

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.*;

@Component(service = {Servlet.class})
@SlingServletPaths("/bin/cassandra/filereader/content")
public class DAMFileReaderServlet extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DAMFileReaderServlet.class);

    private static final String FILE_DAM_PATH = "/content/dam/cassandra/files/sample.txt";

    @Reference
    private ResourceResolverFactory resolverFactory;
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        ResourceResolver resourceResolver = null;
        BufferedReader reader = null;
        StringBuilder builder = null;

        try {
            resourceResolver = request.getResourceResolver();
            Resource fileResource = resourceResolver.getResource(FILE_DAM_PATH);

            if (fileResource != null) {
                Asset assetResource = fileResource.adaptTo(Asset.class);

                if (assetResource != null) {
                    Rendition fileRendition = assetResource.getOriginal();
                    if (fileRendition != null) {
                        InputStream inputStream = fileRendition.getStream();
                        reader = new BufferedReader(new InputStreamReader(inputStream));
                        String fileContent;
                        builder = new StringBuilder();

                        while ((fileContent = reader.readLine()) != null) {
                            builder.append(fileContent);
                        }
                    }
                }
            }

            DAMResourceData resourceData = new DAMResourceData();

            resourceData.setValidRequest(true);
            resourceData.setData(builder.toString());

            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(resourceData));
        } catch (IOException e) {
            LOG.info("IO Exception Occured while handling the file data: {}", e);
        }finally {
           reader.close();
        }



    }
}
