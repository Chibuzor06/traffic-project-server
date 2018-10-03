package com.chibuzor.finalyearproject.action;

import com.chibuzor.finalyearproject.model.TrafficData;
import com.chibuzor.finalyearproject.service.TrafficDataDAO;
import com.opensymphony.xwork2.ActionSupport;
import com.pusher.rest.Pusher;
import com.sun.net.httpserver.Authenticator;
import org.apache.struts2.util.ServletContextAware;
import org.hibernate.SessionFactory;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TrafficDataAction extends ActionSupport implements ServletContextAware {
    private TrafficData trafficEntry;
    private String status;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setTrafficEntry(TrafficData trafficEntry) {
        this.trafficEntry = trafficEntry;
    }

    public String streetPlaceId;

    private Date startDate;
    private Date endDate;
    private Pusher pusher;

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public String getStatus() {
        return status;
    }

    public String pusherTest() {
//        Pusher pusher = new Pusher("611285", "1906c7198840d1ace56b", "a689a0147e39a331c105");
//        pusher.setCluster("eu");
//        pusher.setEncrypted(true);
        if (this.pusher == null) {
            this.pusher = (Pusher) servletContext.getAttribute("Pusher");
        }
        pusher.trigger("trafficEntry-channel", "my-event", Collections.singletonMap("message", "hello world"));
        return SUCCESS;
    }

    public String addTrafficEntry() {
        if(trafficEntry != null) {
            if(trafficEntry.getEntryDate() == null) {
                trafficEntry.setEntryDate(new Date());
            }
            SessionFactory sf = (SessionFactory) servletContext.getAttribute("SessionFactory");
            TrafficDataDAO trafficDataDAO = new TrafficDataDAO(sf);
            boolean successful = trafficDataDAO.insertNewTrafficEntry(trafficEntry);

            if (successful) {
                if (this.pusher == null) {
                    this.pusher = (Pusher) servletContext.getAttribute("Pusher");
                }
                pusher.trigger("traffic-channel", "vehicle-detected-event", Collections.singletonMap("message", "hello world"));
            }

            status = successful ? "OK": "Error";
            return successful? SUCCESS : ERROR;
        }
        status = "Bad Request";
        return INPUT;

    }

    public String filterChartData() {
        SessionFactory sf = (SessionFactory) servletContext.getAttribute("SessionFactory");
        TrafficDataDAO trafficDataDAO = new TrafficDataDAO(sf);
        List<TrafficData> entries = trafficDataDAO.getTrafficEntries(startDate, endDate, trafficEntry.getStreetID());
        return SUCCESS;
    }


}
