package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.data.model.Transactions;
import com.big_joe.ojemba_bank.data.model.User;
import com.big_joe.ojemba_bank.data.repository.TransactionRepository;
import com.big_joe.ojemba_bank.dto.*;
import com.big_joe.ojemba_bank.exceptions.TransactionsNotFoundException;
import com.big_joe.ojemba_bank.utils.AccountUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserService userService;

    EmailService emailService;

    private static final String FILE = "C:\\Users\\User\\Documents\\MyAccountStatement.pdf";

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        String transactionReference = generateReferenceCode();

        Transactions transaction = Transactions.builder()
                .transactionAmt(transactionDto.getTransactionAmt())
                .transactionType(transactionDto.getTransactionType())
                .senderAccountName(transactionDto.getSenderAccountName())
                .senderAccountNumber(transactionDto.getSenderAccountNumber())
                .recipientAccountName(transactionDto.getRecipientAccountName())
                .recipientAccountNumber(transactionDto.getRecipientAccountNumber())
                .transactionReference(transactionReference)
                .createdAt(LocalDateTime.now())
                .description(transactionDto.getDescription())
                .status("Successful")
                .build();

        transactionRepository.save(transaction);

        System.out.println("Transaction saved successfully");
    }

    private String generateReferenceCode() {
        StringBuilder token = new StringBuilder();
        Random random = new Random();

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int index = 0; index < 12; index++) {
            token.append(characters.charAt(random.nextInt(characters.length())));
        }
        return token.toString();
    }

    @Override
    public TransactionResponse findTransactionByReference(TransactionEnquiryReq enquiryReq) {
        Optional<Transactions> transaction = transactionRepository.findByTransactionReference(enquiryReq.getTransactionReference());

        if (transaction.isEmpty()) {
            return TransactionResponse.builder()
                    .responseCode(AccountUtil.TRANSACTION_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.TRANSACTION_NOT_EXIST_MESSAGE)
                    .transaction(null)
                    .build();
        }

        Transactions foundTransaction = transaction.get();

        return TransactionResponse.builder()
                .responseCode(AccountUtil.TRANSACTION_EXIST_CODE)
                .responseMessage(AccountUtil.TRANSACTION_EXIST_MESSAGE)
                .transaction(Transactions.builder()
                        .transactionType(foundTransaction.getTransactionType())
                        .senderAccountName(foundTransaction.getSenderAccountName())
                        .senderAccountNumber(foundTransaction.getSenderAccountNumber())
                        .recipientAccountName(foundTransaction.getRecipientAccountName())
                        .recipientAccountNumber(foundTransaction.getRecipientAccountNumber())
                        .transactionAmt(foundTransaction.getTransactionAmt())
                        .description(foundTransaction.getDescription())
                        .build())
                .build();
    }

    @Override
    public List<Transactions> findTransactionsByAccountNumber(TransactionEnquiryReq enquiryReq1) {
        List<Transactions> transaction = transactionRepository.findBySenderAccountNumber(enquiryReq1.getAccountNumber());

        if (transaction.isEmpty()) {
            throw new TransactionsNotFoundException("User with this Account number does not exist, hence tansactions not found!");
        }
        return transaction;
    }

    @Override
    public List<Transactions> allTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transactions> generateAccountStatement(TransactionEnquiryReq req) throws DocumentException, FileNotFoundException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate startDate = LocalDate.parse(req.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(req.getEndDate(), formatter);

        List<Transactions> transactionsList = transactionRepository.findAll().stream()
                .filter(transactions -> transactions.getSenderAccountNumber().equals(req.getAccountNumber()))
                .filter(transactions -> !transactions.getCreatedAt().isBefore(startDate.atStartOfDay())
                        && !transactions.getCreatedAt().isAfter(endDate.atStartOfDay().plusDays(1).minusSeconds(1)))
                .toList();

        designAccountStatement(transactionsList, startDate, endDate);

        return transactionsList;
    }

    private void designAccountStatement(List<Transactions> transactionsList, LocalDate startDate, LocalDate endDate) throws FileNotFoundException, DocumentException {
        Document document = new Document(PageSize.A4);
        log.info("Setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfo = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Ojemba Bank Plc"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);
        PdfPCell bankAddress = new PdfPCell(new Phrase("31 Adigun Street, Surulere, Lagos"));
        bankAddress.setBorder(0);

        bankInfo.addCell(bankName);
        bankInfo.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
        stopDate.setBorder(0);

        Transactions firstTransaction = transactionsList.get(0);
        Optional<User> user = userService.findByAccountNumber(firstTransaction.getSenderAccountNumber());

        PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + firstTransaction.getSenderAccountName()));
        name.setBorder(0);
        PdfPCell space = new PdfPCell();
        space.setBorder(0);
        PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.get().getAddress()));
        address.setBorder(0);

        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBorder(0);
        date.setBackgroundColor(BaseColor.BLUE);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);
        PdfPCell transactionAmt = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmt.setBackgroundColor(BaseColor.BLUE);
        transactionAmt.setBorder(0);
        PdfPCell description = new PdfPCell(new Phrase("DESCRIPTION"));
        description.setBackgroundColor(BaseColor.BLUE);
        description.setBorder(0);

        transactionsTable.addCell(date);
        transactionsTable.addCell(transactionType);
        transactionsTable.addCell(transactionAmt);
        transactionsTable.addCell(description);

        transactionsList.forEach(transaction -> {
            transactionsTable.addCell(transaction.getCreatedAt().toString());
            transactionsTable.addCell(transaction.getTransactionType());
            transactionsTable.addCell(transaction.getTransactionAmt().toString());
            transactionsTable.addCell(transaction.getDescription());
        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(stopDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfo);
        document.add(statementInfo);
        document.add(transactionsTable);

        EmailDetails emailDetails = EmailDetails.builder()
                .subject("STATEMENT OF ACCOUNT")
                .recipient(user.get().getEmail())
                .messageBody("Kindly find your account statement attached to this mail")
                .attachments(FILE)
                .build();
        emailService.sendEmailWithAttachment(emailDetails);


        document.close();
    }
}
