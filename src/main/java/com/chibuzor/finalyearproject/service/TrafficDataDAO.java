package com.chibuzor.finalyearproject.service;

import com.chibuzor.finalyearproject.model.TrafficData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrafficDataDAO {
    private  SessionFactory sf;

    public TrafficDataDAO(SessionFactory sf) {
        this.sf = sf;
    }

    public boolean insertNewTrafficEntry(TrafficData newEntry) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
//        Query query = session.createQuery("from User where entryDate>=:startDate and entryDate<=:endDate");
        session.save(newEntry);
        tx.commit();
        session.close();
        return true;
    }

    public List<TrafficData> getTrafficEntries(Date startDate, Date endDate, String streetID) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(" from TrafficData where streetID=:id and entryDate>=:start and entryDate<=:endD");
        query.setParameter("id", streetID);
        query.setParameter("start", startDate);
        query.setParameter("endD", endDate);
        List<TrafficData> list =  query.list();

//        session.save(newEntry);
        tx.commit();
        session.close();
        return  list;
    }
}
