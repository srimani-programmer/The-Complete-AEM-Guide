package com.cassandra.restaurant.core.servlets;

import com.cassandra.restaurant.core.beans.AgifyCustomerData;
import com.cassandra.restaurant.core.utils.ResolverUtility;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Component(service = {Servlet.class})
@SlingServletPaths("/bin/cassandra/customer/v1/namedatastore")
public class DAMAssetCreatorServlet extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DAMAssetCreatorServlet.class);

    private static final String AGIFY_API_ENDPOINT = "https://api.agify.io/?name=";

    private static final String DAM_PATH = "/content/dam/cassandra/employeerecords/";

    private static HttpURLConnection connection;

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)  {
        String name = request.getParameter("customerName");
        AgifyCustomerData customerData = null;
        if(!StringUtils.isBlank(name)){
            final String finalEndPoint = AGIFY_API_ENDPOINT + name;
            System.out.println(finalEndPoint);
            URL url = null;
            InputStream inputStream = null;
            BufferedReader bufferedReader = null;
            StringBuilder builder = null;
            String output;

            try {
                url = new URL(finalEndPoint);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int statusCode = connection.getResponseCode();

                if(statusCode == 200){
                    inputStream = connection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    builder = new StringBuilder();

                    while ((output = bufferedReader.readLine()) != null){
                        builder.append(output);
                    }

                    String jsonResponse = builder.toString();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.disableHtmlEscaping();
                    customerData =  gsonBuilder.create().fromJson(jsonResponse, AgifyCustomerData.class);
                    System.out.println(customerData);
                    InputStream is = new ByteArrayInputStream(jsonResponse.getBytes());

                    ResourceResolver resolver = ResolverUtility.getResourceResolver(resolverFactory);
                    AssetManager assetManager = resolver.adaptTo(AssetManager.class);
                    Asset asset = assetManager.createAsset(DAM_PATH + (int) (Math.random() * 10000000) + "-" + customerData.getName() + ".json", is, "application/json", true);

                    response.setStatus(200);
                    response.setContentType("application/json");
                    response.getWriter().write(jsonResponse);
                }else{
                    response.setStatus(500);
                    response.setContentType("application/json");
                    response.getWriter().write("Something Went Wrong");
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }  catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                if(url != null) {
                    url = null;
                }
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
