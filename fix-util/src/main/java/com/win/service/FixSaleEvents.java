package com.win.service;


import com.win.slots.backend.dao.SaleEventItemDao;
import com.win.slots.backend.dao.SaleTriggerRepo;
import com.win.slots.backend.domain.sale_events.SaleEventItemEntity;
import com.win.slots.domain.EventSale;
import com.win.slots.domain.EventSaleItem;
import com.win.slots.protocol.event.sale.SaleEventType;

import java.util.List;

public abstract class FixSaleEvents {

    public final ItemDTO findOut(List<EventSaleItem> eventSaleItems,
                           SaleEventItemDao saleEventDao,
                           EventSale sale,
                           SaleEventType filter){
        ItemDTO data = new ItemDTO();

        // Find the item into MongoDB
        List<SaleEventItemEntity> sale_events = saleEventDao.getEventItems(sale.getId());
        if( !sale_events.isEmpty() ) {
            for (SaleEventItemEntity sale_event : sale_events) {
                if (sale.getId() == sale_event.getId().getEventId()) {
                    data.setSaleEventType(sale_event.getSaleEventType());
                    data.setType(sale_event.getType());
                    data.setName(sale_event.getName());
                    data.setSaleEventItemEntityPK(sale_event.getId());
                }
            }
            data.setFoundMongo(true);
        }

        // Find the item into PostgreSQL
        List<EventSaleItem> sale_items = eventSaleItems;
        for (EventSaleItem sale_item : sale_items) {
            if (sale.getId()==sale_item.getPk().getEvent().getId()) {
                data.setFoundPostgre(true);
                continue;
            }
        }

        if (filter!=null
                && filter.getType()!= data.getSaleEventType())
            return null;
        return data;
    }

    public final ItemDTO amend(List<EventSaleItem> eventSaleItems,
                           SaleEventItemDao saleEventDao,
                           EventSale sale,
                           SaleEventType filter){
        ItemDTO data = findOut(eventSaleItems, saleEventDao, sale, filter);

        if (data==null){
            return null;
        } else {
            if (data.isFoundMongo() && !data.isFoundPostgre()) {
                return data;
            }
        }
        return null;
    }


    public abstract List<EventSale> getEventSales();

    public abstract List<EventSaleItem> getEventSaleItems();

    public abstract SaleEventItemDao getSaleEventDao();

    public abstract SaleTriggerRepo getSaleTriggerDao();
}
