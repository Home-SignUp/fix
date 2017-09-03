package com.win.service.impl;

import com.win.service.FixSaleEvents;
import com.win.service.FixSaleEventsService;
import com.win.service.ItemDTO;
import com.win.util.CSVWriteUtil;
import com.win.util.OutUtil;
import com.win.slots.backend.dao.SaleEventItemDao;
import com.win.slots.backend.dao.SaleTriggerRepo;
import com.win.slots.backend.domain.sale_events.SaleEventItemEntity;
import com.win.slots.dao.EventSaleDao;
import com.win.slots.dao.EventSaleItemDao;
import com.win.slots.domain.EventSale;
import com.win.slots.domain.EventSaleItem;
import com.win.slots.protocol.event.sale.SaleEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.win.util.OutUtil.formatOutHeader;
import static com.win.util.OutUtil.formatCVSHeader;
import static com.win.util.OutUtil.formatOutSeparator;
import static com.win.util.OutUtil.formatOutFooter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FixSaleEventsServiceImpl
    extends FixSaleEvents
    implements FixSaleEventsService
{

    private static final Logger log = LoggerFactory.getLogger(FixSaleEventsServiceImpl.class);

    /*
     * Sale | sale
     * PostgreSQL >> 'EventSale(s)'
     */
    @Autowired
    private EventSaleDao saleDao;

    /*
     * SaleItem | sale_items
     * PostgreSQL >> 'EventSaleItem(s)'
     */
    @Autowired
    private EventSaleItemDao saleItemDao;

    /*
     * SaleEvent | sale_events
     * MongoDB >> 'SaleEventItemDao'
     */
    @Autowired
    private SaleEventItemDao saleEventDao;

    /*
     * ... | sale_triggers
     * MongoDB >> 'SaleTriggerRepo'
     */
    @Autowired
    private SaleTriggerRepo saleTriggerDao;


    @Override
    public void run(SaleEventType filter) {
        try {
            String fileName = "fix_statistic_" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".csv";
            int found = getData(new FileWriter(fileName), getEventSales(), getEventSaleItems(), getSaleEventDao(), filter);

            findOut(getEventSales(), getEventSaleItems(), getSaleEventDao(), filter);

            int fix = amend(getEventSales(), getEventSaleItems(), getSaleEventDao(), getSaleTriggerDao(), filter);
            log.warn("Get a list of data that is missing to postgres and mongo - finish is SUCESSFULLY!");
            log.warn("Find Out " + found + " item(s)");
            log.warn("Amend " + fix + " item(s)");
        } catch (IOException|IllegalStateException e){
            log.error("ERROR - can't get a list of data that is missing to postgres and mongo?");
            e.printStackTrace();
            System.exit(1);
            return;
        } finally {
            System.exit(0);
            return;
        }
    }


    /*
     * out to console
     */
    public int findOut(List<EventSale> eventSales,
                        List<EventSaleItem> eventSaleItems,
                        SaleEventItemDao saleEventDao,
                        SaleEventType filter) {
        int count = 0;

        System.out.println(formatOutHeader);
        System.out.println(formatOutSeparator);
        for (EventSale sale : eventSales) {
            ItemDTO view = findOut(eventSaleItems, saleEventDao, sale, filter);
            if (view==null)
                continue;
            OutUtil.outToConsole(view.isFoundMongo(), view.isFoundPostgre(), sale, view.getSaleEventType(), view.getType(), view.getName());
            count++;
        }
        System.out.println(formatOutFooter);

        return count;
    }

    /*
     * out to a file
     */
    public int getData(FileWriter file,
                        List<EventSale> eventSales,
                        List<EventSaleItem> eventSaleItems,
                        SaleEventItemDao saleEventDao,
                        SaleEventType filter)
            throws IOException {
        int count = 0;

        CSVWriteUtil.writeLine(file, Arrays.asList(formatCVSHeader));
        for (EventSale sale : eventSales) {
            ItemDTO view = findOut(eventSaleItems, saleEventDao, sale, filter);
            if (view==null)
                continue;
            OutUtil.outToCVS(file, view.isFoundMongo(), view.isFoundPostgre(), sale, view.getSaleEventType(), view.getType(), view.getName());
            count++;
        }
        file.close();

        return count;
    }

    /*
     * DELETE mistake item(s) from MongoDB
     */
    public int amend(List<EventSale> eventSales,
                        List<EventSaleItem> eventSaleItems,
                        SaleEventItemDao saleEventDao,
                        SaleTriggerRepo saleTriggerDao,
                        SaleEventType filter)
    throws IllegalStateException {
        int count = 0;

        for (EventSale sale : eventSales) {
            ItemDTO del = amend(eventSaleItems, saleEventDao, sale, filter);
            if (del==null)
                continue;
            log.warn("(sale_events) ID = " + del.getSaleEventItemEntityPK().getEventId() + ";   (sale_triggers) ID = " + sale.getTriggerId() + ";   - DELETE from MongoDB was SUCESSFULLY!");
            saleEventDao.deleteEventItem( del.getSaleEventItemEntityPK() );
            count++;
        }

        return count;
    }

    public List<EventSale> getEventSales() {
        return saleDao.getAll();
    }

    public List<EventSaleItem> getEventSaleItems() {
        return saleItemDao.getAll();
    }

    public SaleEventItemDao getSaleEventDao() {
        return saleEventDao;
    }

    public SaleTriggerRepo getSaleTriggerDao() {
        return saleTriggerDao;
    }

}
