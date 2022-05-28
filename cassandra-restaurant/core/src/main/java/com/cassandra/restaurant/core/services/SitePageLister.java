package com.cassandra.restaurant.core.services;

import com.cassandra.restaurant.core.utils.ResolverUtility;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import java.util.Iterator;


@Component(service = SitePageLister.class, immediate = true)
public class SitePageLister {

    @Activate
    public void activate(){
        System.out.println("OSGI Bundle Activated");
    }

    @Deactivate
    public void deactivate(){
        System.out.println("OSGI Bundle Deactivated");
    }
    @Reference
    private ResourceResolverFactory resolverFactory;

    public Iterator<Page> getPages(String path) {
//        System.out.println(path);
        ResourceResolver resolver = ResolverUtility.getResourceResolver(resolverFactory);
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        Iterator<Page> pages = pageManager.getPage(path).listChildren();
        return pages;
    }
}
