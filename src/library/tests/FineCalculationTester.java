package library.tests;

import library.entities.*;
import library.entities.helpers.BookHelper;
import library.entities.helpers.LibraryFileHelper;
import library.entities.helpers.LoanHelper;
import library.entities.helpers.PatronHelper;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FineCalculationTester {
    ILoan loan;
    LibraryFileHelper libraryHelper;
    ILibrary library;

    public static void main(String[] args) {
        FineCalculationTester tester = new FineCalculationTester();
        tester.setUp("20/10/2020");

        System.out.print("Single Day fine test: ");
        System.out.println(tester.testSingleDayFine() ? "pass" : "fail");
        tester.setUp("12/10/2020");

        System.out.print("Multi Day fine test: ");
        System.out.println(tester.testSingleDayFine() ? "pass" : "fail");

    }

    public void setUp(String sDate1) {
        libraryHelper = new LibraryFileHelper(new BookHelper(), new PatronHelper(), new LoanHelper());
        library = libraryHelper.loadLibrary();
        loan = library.issueLoan(library.getBookById(2), library.getPatronById(1));
        Date dueDate = new Date();
        try {
            dueDate = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
        } catch (ParseException exc) {
            System.out.println("Unable to Parse Date");
        }
        loan.commit(1, dueDate);
    }

    public boolean testSingleDayFine() {
        loan.updateOverDueStatus(Calendar.getInstance().getDate());
        double fine = library.calculateOverDueFine(loan);
        return fine == 1.0;
    }


    public boolean testMultiDayFine(int days) {
        loan.updateOverDueStatus(Calendar.getInstance().getDate());
        double fine = library.calculateOverDueFine(loan);
        return fine == (days * 1.0);
    }


}
