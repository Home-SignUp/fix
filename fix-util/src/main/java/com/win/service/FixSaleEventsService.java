package com.win.service;

import com.win.slots.protocol.event.sale.SaleEventType;

public interface FixSaleEventsService {

    void run(SaleEventType filter);

}
