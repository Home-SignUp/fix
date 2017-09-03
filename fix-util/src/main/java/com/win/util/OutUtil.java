package com.win.util;


import com.win.slots.domain.EventSale;
import com.win.slots.protocol.event.sale.SaleEventType;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class OutUtil {

    private static final String formatOutData = "| %1$-21s | %2$-8s | %3$-6s | %4$-17s | %5$-6s | %6$-37s | %7$-110s |\n";
    public static final String[] formatCVSHeader = {"DATABASE", "ID", "HIDDEN", "SALE_EVENT_TYPE", "TYPE", "TRIGGER_ID", "NAME"};
    public static final String formatOutHeader = "\n\r\n\r+-----------------------+----------+--------+-------------------+--------+---------------------------------------+----------------------------------------------------------------------------------------------------------------+"
            + "\n\r|        DATABASE       |    ID    | HIDDEN |  SALE_EVENT_TYPE  |  TYPE  |               TRIGGER_ID              |                                                   NAME                                                         |";
    public static final String formatOutFooter = "+-----------------------+----------+--------+-------------------+--------+---------------------------------------+----------------------------------------------------------------------------------------------------------------+\n\r\n\r";
    public static final String formatOutSeparator = "+-----------------------+----------+--------+-------------------+--------+---------------------------------------+----------------------------------------------------------------------------------------------------------------+";

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_GRAY_BACKGROUND = "\u001B[47m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void outToConsole(boolean isFoundMongo,
                                    boolean isFoundPostgre,
                                    EventSale sale,
                                    int iSaleEventType,
                                    String type,
                                    String name) {
        String[] out = null;
        if (!isFoundMongo && !isFoundPostgre) {
            out = new String[]{
                    "....... ..........",
                    String.valueOf(sale.getId()),
                    ((sale.getHidden() == null) ? "" : "TRUE"),
                    "",
                    "",
                    sale.getTriggerId(),
                    ""
            };
        } else if(!isFoundMongo) {
            out = new String[]{
                    "....... PostgreSQL",
                    String.valueOf(sale.getId()),
                    ((sale.getHidden() == null) ? "" : "TRUE"),
                    "",
                    "",
                    sale.getTriggerId(),
                    ""
            };
        } else if(!isFoundPostgre) {
            out = new String[]{
                    "MongoDB ..........",
                    String.valueOf(sale.getId()),
                    ((sale.getHidden() == null) ? "" : "TRUE"),
                    SaleEventType.fromType(iSaleEventType).toString(),
                    type,
                    sale.getTriggerId(),
                    name
            };
        } else {
            out = new String[]{
                    "MongoDB PostgreSQL",
                    String.valueOf(sale.getId()),
                    ((sale.getHidden() == null) ? "" : "TRUE"),
                    SaleEventType.fromType(iSaleEventType).toString(),
                    type,
                    sale.getTriggerId(),
                    name
            };
        }

        System.out.format(formatOutData, out);
    }

    public static void outToCVS(FileWriter file,
                                boolean isFoundMongo,
                                boolean isFoundPostgre,
                                EventSale sale,
                                int iSaleEventType,
                                String type,
                                String name)
            throws IOException {
            if (!isFoundMongo && !isFoundPostgre) {
                CSVWriteUtil.writeLine(file,
                        Arrays.asList(" ....... .......... ",
                                String.valueOf(sale.getId()),
                                String.valueOf(((sale.getHidden() == null) ? "" : "true")),
                                " ",
                                " ",
                                String.valueOf(sale.getTriggerId()),
                                " "));
            } else if(!isFoundMongo) {
                CSVWriteUtil.writeLine(file,
                        Arrays.asList(" ....... PostgreSQL ",
                                String.valueOf(sale.getId()),
                                String.valueOf(((sale.getHidden() == null) ? "" : "true")),
                                " ",
                                " ",
                                String.valueOf(sale.getTriggerId()),
                                " "));
            } else if(!isFoundPostgre) {
                CSVWriteUtil.writeLine(file,
                        Arrays.asList(" MongoDB .......... ",
                                String.valueOf(sale.getId()),
                                String.valueOf(((sale.getHidden() == null) ? "" : "true")),
                                SaleEventType.fromType(iSaleEventType).toString(),
                                type,
                                String.valueOf(sale.getTriggerId()),
                                name));
            } else {
                CSVWriteUtil.writeLine(file,
                        Arrays.asList(" MongoDB PostgreSQL ",
                                String.valueOf(sale.getId()),
                                String.valueOf(((sale.getHidden() == null) ? "" : "true")),
                                SaleEventType.fromType(iSaleEventType).toString(),
                                type,
                                String.valueOf(sale.getTriggerId()),
                                name));
            }
        file.flush();
    }

    public static int saleEventType(String val) {
        for (SaleEventType type : SaleEventType.values()) {
            if (type.toString().equals(val)) {
                return type.getType();
            }
        }
        return -1;
    }
}
