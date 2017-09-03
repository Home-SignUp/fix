package com.win.service.impl;


import com.win.server.config.MongoLocalTestConfiguration;
import com.win.server.config.MongoTestConfiguration;
import com.win.service.ItemDTO;
import com.win.slots.backend.dao.SaleEventItemDao;
import com.win.slots.backend.domain.sale_events.SaleEventItemEntity;
import com.win.test.execution.listener.Collection;
import com.win.test.execution.listener.DataSet;
import com.win.test.execution.listener.MongoDBExecutionListener;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MongoTestConfiguration.class)
@TestExecutionListeners({MongoDBExecutionListener.class, DependencyInjectionTestExecutionListener.class})
@DataSet(resource = "/sale_events.json")
@Collection(name = "sale_events")
public class FixSaleEventsServiceImplTest {

    /*
     * from MongoDB ('SALE_EVENTS')
     */
    @Autowired
    private  SaleEventItemDao saleEventDao;

    private static long[] SALE;
    private static long[] SALE_ITEMS;
    private static int SALE_EVENT_TYPE;

    static {
        /*
         * from PostgreSQL
         */
        SALE = new long[]{102434, 102444, 102446, 102448, 102458, 102460, 102462, 102468, 102470, 102472, 102474, 102476, 102480};
        SALE_ITEMS = new long[]{102448, 102460, 102468, 102472, 102476};
        /*
         * 'Mini Game Popup'
         */
        SALE_EVENT_TYPE = 10;
    }

    @Before
    public void init() throws Exception {
    }

    /**
     * compare with PostgreSQL & MongoDB
     */
    @Ignore
    @Test
    public void testFindOut() {
        final int expect = SALE.length;
        int actual = 0;

        for (long saleId : SALE) {
            if (findOut(SALE_ITEMS, saleEventDao, saleId, SALE_EVENT_TYPE) != null)
                actual++;
        }

        assertEquals("Find Out", expect, actual);
    }

    /**
     * to amend 'SALE_EVENTS' into MongoDB
     */
    @Ignore
    @Test
    public void testAmend() {
        final int expectAmend = SALE.length-SALE_ITEMS.length;
        int actualAmend = 0;

        for (long saleId : SALE) {
            if (amend(SALE_ITEMS, saleEventDao, saleId, SALE_EVENT_TYPE) != null)
                actualAmend++;
        }

        assertEquals("Amend", expectAmend, actualAmend);
    }

    @Ignore
    @Test
    public void test() {
        final int expectFoundOutBefore = SALE.length,
                expectFoundOutAfter = SALE_ITEMS.length;
        int actualFoundOutBefore = 0,
                actualFoundOutAfter = 0;

        // Found Out before
        for (long saleId : SALE) {
            if (findOut(SALE_ITEMS, saleEventDao, saleId, SALE_EVENT_TYPE) != null)
                actualFoundOutBefore++;
        }

        // to amend
        for (long saleId : SALE) {
            ItemDTO del = amend(SALE_ITEMS, saleEventDao, saleId, SALE_EVENT_TYPE);
            if (del!=null) {
                saleEventDao.deleteEventItem( del.getSaleEventItemEntityPK() );
            }
        }

        // Found Out after
        for (long saleId : SALE) {
            if (findOut(SALE_ITEMS, saleEventDao, saleId, SALE_EVENT_TYPE) != null)
                actualFoundOutAfter++;
        }

        assertEquals("Find Out before amend", expectFoundOutBefore, actualFoundOutBefore);
        assertEquals("Find Out after amend", expectFoundOutAfter, actualFoundOutAfter);
    }


    private ItemDTO findOut(long[] saleItems,
                            SaleEventItemDao saleEventDao,
                            long saleId,
                            int filter){
        ItemDTO data = new ItemDTO();

        // Find the item into MongoDB
        List<SaleEventItemEntity> saleEvents = saleEventDao.getEventItems(saleId);
        if( !saleEvents.isEmpty() ) {
            for (SaleEventItemEntity saleEvent: saleEvents){
                if (saleId == saleEvent.getId().getEventId()) {
                    data.setSaleEventType(saleEvent.getSaleEventType());
                    data.setType(saleEvent.getType());
                    data.setName(saleEvent.getName());
                    data.setSaleEventItemEntityPK(saleEvent.getId());
                }
            }
            data.setFoundMongo(true);
        }

        // Find the item into PostgreSQL
        for (long saleItem: saleItems) {
            if (saleId==saleItem) {
                data.setFoundPostgre(true);
                continue;
            }
        }

        if (filter==data.getSaleEventType())
            return data;return null;
    }

    private ItemDTO amend(long[] saleItems,
                          SaleEventItemDao saleEventDao,
                          long saleId,
                          int filter){
        ItemDTO data = findOut(saleItems, saleEventDao, saleId, filter);

        if (data==null){
            return null;
        } else {
            if (data.isFoundMongo() && !data.isFoundPostgre())
                return data;
        }
        return null;
    }
}
