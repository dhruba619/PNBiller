/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pnbiller.services;

import com.google.gson.Gson;
import com.pnbiller.model.Customer;
import com.pnbiller.model.Invoice;
import com.pnbiller.model.Order;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

/**
 *
 * @author piyalin
 */
public class BillerServices {

    private static BillerServices billerServiceInstance = new BillerServices();

    private BillerServices() {
    }

    public static BillerServices getInstance() {
        return billerServiceInstance;
    }

    public Invoice generateInvoice(Customer customer, List<Order> orders, double discount) {
        double totalAmount = 0.0;
        double netPayableAmount = 0.0;

        for (Order order : orders) {
            totalAmount += order.getQuantity() * order.getRate();
        }

        //CALCULATION of TAX
        // for now we have hard coded the service tax, it wil lbe taken from properties file.
        double serviceTax = 14.3; // later get it from properties.
        double taxAmount = (serviceTax / 100) * totalAmount;

        //CALCULATION of DISCOUNT
        double discountAmount = (discount / 100) * totalAmount;

        //CALCULATION OF NET PAYABLE AMOUNT
        netPayableAmount = totalAmount + taxAmount - discountAmount;

        //Creating the invoice
        Invoice invoice = new Invoice();
        invoice.setTotalAmount(totalAmount);
        invoice.setServiceTax(taxAmount);
        invoice.setDiscount(discountAmount);
        invoice.setNetPayableAmount(netPayableAmount);
        invoice.setCustomer(customer);
        invoice.setOrderList(orders);
        return invoice;

    }
    
    
    /**
     * 
     * @param object
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void fileWriterService(Object object) throws FileNotFoundException, IOException {
        String content = new Gson().toJson(object);
        Path filePath = Paths.get("invoices.json");// the file path will be taken from properties file.
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
        Files.write(filePath, (content).getBytes(), StandardOpenOption.APPEND);
    }
    
    
    /**
     * 
     * @param object
     * @throws PrintException
     * @throws IOException 
     */
    public void printInvoice(Object object) throws PrintException, IOException {
        String defaultPrinter
                = PrintServiceLookup.lookupDefaultPrintService().getName();
        System.out.println("Default printer: " + defaultPrinter);

        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        String content = new Gson().toJson(object);
        InputStream in = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(1));

        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc doc = new SimpleDoc(in, flavor, null);

        DocPrintJob job = service.createPrintJob();
        PrintJobWatcher pjw = new PrintJobWatcher(job);
        job.print(doc, pras);
        pjw.waitForDone();
        in.close();

        // send FF to eject the page
        InputStream ff = new ByteArrayInputStream("\f".getBytes());
        Doc docff = new SimpleDoc(ff, flavor, null);
        DocPrintJob jobff = service.createPrintJob();
        pjw = new PrintJobWatcher(jobff);
        jobff.print(docff, null);
        pjw.waitForDone();

    }
    
    
    /**
     * 
     */
    class PrintJobWatcher {

        boolean done = false;

        PrintJobWatcher(DocPrintJob job) {
            job.addPrintJobListener(new PrintJobAdapter() {
                public void printJobCanceled(PrintJobEvent pje) {
                    allDone();
                }

                public void printJobCompleted(PrintJobEvent pje) {
                    allDone();
                }

                public void printJobFailed(PrintJobEvent pje) {
                    allDone();
                }

                public void printJobNoMoreEvents(PrintJobEvent pje) {
                    allDone();
                }

                void allDone() {
                    synchronized (PrintJobWatcher.this) {
                        done = true;
                        System.out.println("Printing done ...");
                        PrintJobWatcher.this.notify();
                    }
                }
            });
        }

        public synchronized void waitForDone() {
            try {
                while (!done) {
                    wait();
                }
            } catch (InterruptedException e) {
            }
        }

    }
}
