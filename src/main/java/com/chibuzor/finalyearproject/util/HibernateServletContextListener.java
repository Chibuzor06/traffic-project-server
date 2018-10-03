package com.chibuzor.finalyearproject.util;

import com.pusher.rest.Pusher;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.URL;
import java.util.logging.Logger;

public class HibernateServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        URL url = HibernateServletContextListener.class.getResource("/hibernate.cfg.xml");
//        Configuration config = new Configuration();
//        config.configure(url);
//        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
//                .applySettings(config.getProperties()).build();
//        SessionFactory sf = config.buildSessionFactory(serviceRegistry);
//        sce.getServletContext().setAttribute("SessionFactory", sf);

        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure(url).build();
        Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
        SessionFactory sf = metadata.getSessionFactoryBuilder().build();
        sce.getServletContext().setAttribute("SessionFactory", sf);

        Pusher pusher = new Pusher("611285", "1906c7198840d1ace56b", "a689a0147e39a331c105");
        pusher.setCluster("eu");
        pusher.setEncrypted(true);
        sce.getServletContext().setAttribute("Pusher", pusher);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SessionFactory sf = (SessionFactory) sce.getServletContext().getAttribute("SessionFactory");
        sf.close();
    }
}
