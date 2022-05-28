package com.cassandra.restaurant.core.servlets;

import com.cassandra.restaurant.core.beans.PageListBean;
import com.cassandra.restaurant.core.services.SitePageLister;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/services/cassandra/sitepagelist")
public class OSGIServiceTriggerServlet extends SlingAllMethodsServlet {

    @Reference
    SitePageLister pageLister;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        Iterator<Page> pages = pageLister.getPages(path);
        List<String> names = new ArrayList<>();

        while (pages.hasNext()){
            names.add(pages.next().getName());
        }

        PageListBean bean = new PageListBean();
        bean.setValidRequest(true);
        bean.setPages(names);

        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(bean));
    }
}
