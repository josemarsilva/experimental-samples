package org.farrukh.experiments;

import org.farrukh.experiments.model.Book;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:base-app-context.xml")
public class AppTest {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Before
    public void setUp() throws Exception {
        assertNotNull(hibernateTemplate);
    }

    @Test
    public void shouldBatchInsertUsingHibernateTemplateSaveMethod() throws Exception {
        for (int i = 0; i < 10000; i++) {
            Book book = new Book();
            book.setName(String.valueOf(i));
            hibernateTemplate.save(book);
        }
    }

    @Test
    public void shouldBatchInsertUsingHibernateTemplateSaveOrUpdateAllMethod() throws Exception {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 10000; i++) {
            Book book = new Book();
            book.setName(String.valueOf(i));
            books.add(book);
        }
        hibernateTemplate.saveOrUpdateAll(books);
    }

    @Test
    public void shouldBatchInsertUsingSessionSaveWithManualFlushAndClearMethods() throws Exception {
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < 10000; i++) {
            Book book = new Book();
            book.setName(String.valueOf(i));
            session.save(book);
            if(i % 100 == 0) {
                session.flush();
                session.clear();
            }
        }
        transaction.commit();
        session.close();
    }

}
