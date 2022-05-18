package com.cassandra.restaurant.core.utils;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class ResolverUtility {
    private static final Logger LOG = LoggerFactory.getLogger(ResolverUtility.class);

    private static final String AEM_SERVICE_USER = "cassandraServiceUser";

    public static ResourceResolver getResourceResolver(ResourceResolverFactory resolverFactory){
        ResourceResolver resourceResolver = null;

        Map<String, Object> params = new HashMap<>();
        params.put(resolverFactory.SUBSERVICE, AEM_SERVICE_USER);

        try{
            resourceResolver = resolverFactory.getServiceResourceResolver(params);
        }catch (LoginException e){
            LOG.info("Exception Occured while creating the resource resolver: {}", e);
        }

        return resourceResolver;
    }
}
