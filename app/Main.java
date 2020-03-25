package app;

import java.util.*;
import java.io.*;

// Assumptions
//  - Invoice numbers should be unique (i.e notify user of duplicates)
//  - Invoice numbers should correspond to the same amount
//  - We should have the same invoice numbers as the accountant
//  - Our values don't contain duplicates
//  - The CSV file passed in will always have the same format
//  - Invoices are not in non-decreaseing order

public final class Main
{
    private Main() {}

    public static void main(String[] args) throws Exception
    {
        List<Invoice> myInvoices = getMyInvoiceList();
        List<Invoice> accountantsInvoices = getAccountantsInvoiceList();
        Map<String, Integer> accInvHashed = new HashMap<String, Integer>();

        // Hash the invoices from the accountant so we can cross-refeence quickly
        for (Invoice accInv : accountantsInvoices)
        {
            // If we don't have the value already insert it
            if (accInvHashed.get(accInv.getInvoiceNumber()) == null)
            {
                accInvHashed.put(accInv.getInvoiceNumber(), accInv.getInvoiceAmount());
            }
            else
            {
                System.out.println("Accountant Duplicate: " + accInv.toString());
                System.out.println();
            }
        }

        for (Invoice myInv : myInvoices)
        {
            String invoiceNumber = myInv.getInvoiceNumber();
            Integer myInvoiceAmount = myInv.getInvoiceAmount();
            Integer accInvoiceAmount = accInvHashed.get(invoiceNumber);

            if (accInvoiceAmount == null)
            {
                System.out.println("Accountant Missing: " + myInv.toString());
                System.out.println();
            }
            else
            {
                if (myInvoiceAmount != accInvoiceAmount)
                {
                    System.out.println("Invoice #" + invoiceNumber + " Amount Differs!");
                    System.out.println("    My Invoice: $" + myInvoiceAmount);
                    System.out.println("    Accountant Invoice: $" + accInvoiceAmount);
                    System.out.println();
                }
            }

            // Remove the key so we can see if we have any additional keys at the end
            accInvHashed.remove(invoiceNumber);
        }

        // We know all of the accountant's duplicates and missing entries at
        // this point but we don't know if the accountant has any value we don't
        for (String key : accInvHashed.keySet())
        {
            System.out.println("We Are Missing: " + "Invoice #" + key + ": $" + accInvHashed.get(key));
            System.out.println();
        }
    }

    private static List<Invoice> getMyInvoiceList()
    {
        // Usually the data will be stored in the database,
        // however we are just returning a static list to simplify the problem.
        return Arrays.asList(
          new Invoice("100", 10),
          new Invoice("101", 13),
          new Invoice("102", 17),
          new Invoice("103", 19),
          new Invoice("104", 16),
          new Invoice("105", 11),
          new Invoice("106", 10),
          new Invoice("107", 19),
          new Invoice("109", 16),
          new Invoice("110", 19),
          new Invoice("111", 14),
          new Invoice("112", 10),
          new Invoice("113", 16),
          new Invoice("114", 11),
          new Invoice("115", 19),
          new Invoice("117", 15),
          new Invoice("119", 11),
          new Invoice("120", 12),
          new Invoice("121", 16),
          new Invoice("122", 18),
          new Invoice("123", 19),
          new Invoice("124", 14)
        );
    }

    // This could be done with opencsv which has a more robust parser.
    // I stuck with the built-in java functions because it was not specified
    // if external api's were allowed.
    private static List<Invoice> getAccountantsInvoiceList() throws IOException
    {
        // TODO: Read invoices from accountants_invoices.csv.
        // The file has a header row and a row for each invoice.
        List<Invoice> invoices = new ArrayList<Invoice>();
        String invoiceFile = "app/accountants_invoices.csv";
        Scanner invoiceCSV = new Scanner(new File(invoiceFile));

        // Read the headers, do nothing with them currently
        if (invoiceCSV.hasNextLine())
            invoiceCSV.nextLine();

        // Scanner.useDelimiter() would also be an option here
        while (invoiceCSV.hasNextLine())
        {
            String newLine = invoiceCSV.nextLine();
            String invoiceNumber = newLine.split(",")[0];
            int invoiceAmount = Integer.parseInt(newLine.split(",")[1].trim());
            invoices.add(new Invoice(invoiceNumber, invoiceAmount));
        }

        invoiceCSV.close();

        return invoices;
    }
}
