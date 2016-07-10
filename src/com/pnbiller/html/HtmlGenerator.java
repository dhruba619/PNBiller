/*
 * The MIT License
 *
 * Copyright 2016 dhrubajyotib.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.pnbiller.html;

import com.pnbiller.model.Invoice;
import com.pnbiller.model.Order;
import java.util.List;

/**
 *
 * @author dhrubajyotib
 */
public class HtmlGenerator {

    public static String DEFAULT_HTML = "<h2>INVOICE</h2>\n"
            + "<p>Date : #DATE_TIME</p>\n"
            + "<h2>Dine With Ria</h2>\n"
            + "<p>Hinjewadi, Pune-411057</p>\n"
            + "<p><strong>Table No:</strong> #TABLE_NUMBER</p>\n"
            + "<p><strong>Customer Name:</strong>#CNAME<br /><strong>Mobile:</strong>#CMNUMNER<br /><strong>Delivery Address:</strong> #CADDRESS</p>\n"
            + "<table style=\"height: 89px;\" width=\"291\">\n"
            + "<tbody>\n"
            + "<tr>\n"
            + "<td>Sl. No</td>\n"
            + "<td>Item</td>\n"
            + "<td>Quantity</td>\n"
            + "</tr>\n"
            + "#ADD_ITEM_ROW"
            + "</tbody>\n"
            + "</table>\n"
            + "<p>&nbsp;</p>\n"
            + "<p><strong>Total Amount: #TAMOUNT</strong></p>\n"
            + "<p><strong>Service Tax: #ST</strong></p>\n"
            + "<p><strong>Discount: #DIS</strong></p>\n"
            + "<p><strong>Net Payable: #NETA</strong></p>";

    public static final String ROWS = "<tr>#TDS</tr>";
    public static final String TDS = "<td>#ELEMENT1</td><td>#ELEMENT2</td><td>#ELEMENT3</td><td>#ELEMENT4</td>";

    public static String getHtmlInvoice(Invoice invoice) {
        /**
         * #TA = Total Amount #ST = Service Tax #DIS = Discount #NETA = Net
         * Payable Amount #ADD_ITEM_ROW = replace this to add order rows in the
         * table #CNAME = customer name #CMNUMNER = Customer Mobile Number
         * #CADDRESS = Customer Address #DATE_TIME = Current Date and Time
         * #TABLE_NUMBER = Table Number
         */
        String invoceHtml = DEFAULT_HTML;
        invoceHtml = invoceHtml.replace("#TAMOUNT", String.valueOf(invoice.getTotalAmount()));
        invoceHtml = invoceHtml.replace("#ST", String.valueOf(invoice.getServiceTax()));
        invoceHtml = invoceHtml.replace("#DIS", String.valueOf(invoice.getDiscount()));
        invoceHtml = invoceHtml.replace("#NETA", String.valueOf(invoice.getNetPayableAmount()));
        invoceHtml = invoceHtml.replace("#CNAME", invoice.getCustomer().getCustomerName());
        invoceHtml = invoceHtml.replace("#CMNUMNER", invoice.getCustomer().getContactNumber());
        invoceHtml = invoceHtml.replace("#CADDRESS", invoice.getCustomer().getAddress());
        invoceHtml = invoceHtml.replace("#TABLE_NUMBER", invoice.getTableNumner());
        invoceHtml = invoceHtml.replace("#ADD_ITEM_ROW", buildHtmlOrders(invoice.getOrderList()));
        
        return invoceHtml;

    }
    
    private static String buildHtmlOrders(List<Order> orders){
        StringBuffer rows = new StringBuffer();
        for(Order order : orders){
            if(null!=order){
            String row = ROWS;
            String tds = TDS;
            tds=tds.replace("#ELEMENT1", String.valueOf(order.getSerialNo()));
            tds=tds.replace("#ELEMENT2", order.getItemName());
            tds=tds.replace("#ELEMENT3", String.valueOf(order.getRate()));
            tds=tds.replace("#ELEMENT4", String.valueOf(order.getQuantity())); 
            row=row.replace("#TDS", tds);
            rows.append(row);
            }
        }    
        return rows.toString();
    }

}
