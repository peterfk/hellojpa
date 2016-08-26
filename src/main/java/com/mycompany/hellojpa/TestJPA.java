/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hellojpa;

import com.mycompany.hellojpa.sakila.domain.Customer;
import com.mycompany.hellojpa.sakila.domain.Payment;
import com.mycompany.hellojpa.sakila.domain.Rental;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author pkipping
 */
public class TestJPA {

    private static final Logger LOG = Logger.getLogger(TestJPA.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("testing JPA");
        TestJPA main = new TestJPA();
        main.run();

    }

    public void run() {
        EntityManagerFactory factory = null;
        EntityManager em = null;
        try {
            factory = Persistence.createEntityManagerFactory("sakila");
            em = factory.createEntityManager();

            //do queries
            String ql = "select c from Customer c "
                    + "join fetch c.rentalSet r "
                    + "join fetch c.paymentSet p "
                    + "where c.lastName = :lastName";
            List<Customer> list = em.createQuery(ql).setParameter("lastName", "Vest")
                    .setMaxResults(10).getResultList();

            for (Customer c : list) {
                LOG.log(Level.INFO, "Customer Name {0}", c.getLastName());
                int i = 0;
                for (Rental r: c.getRentalSet()) {
                    LOG.log(Level.INFO, "Rental ID: {0} rental date {1}", new Object[]{r.getRentalId(), r.getRentalDate()});
                    ++i;
                    LOG.info("Int i: " + i);
                    if (i>4) break;
                }
                for (Payment p : c.getPaymentSet()) {
                    LOG.info("Payment id: " + p.getPaymentId() + " Amount: " + p.getAmount());
                }
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
            if (factory != null) {
                factory.close();
            }
        }
    }

}
