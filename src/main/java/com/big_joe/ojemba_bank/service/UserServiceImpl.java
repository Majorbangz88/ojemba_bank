package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.data.model.AccountInfo;
import com.big_joe.ojemba_bank.data.model.Transactions;
import com.big_joe.ojemba_bank.data.model.User;
import com.big_joe.ojemba_bank.data.repository.UserRepository;
import com.big_joe.ojemba_bank.dto.*;
import com.big_joe.ojemba_bank.utils.AccountUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final String FILE = "C:\\Users\\User\\Documents\\MyAccountStatement.pdf";

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public BankResponse createAccount(UserRegRequest regRequest) {
        Optional<User> foundUser = userRepository.findByEmail(regRequest.getEmail());

        if (foundUser.isPresent()) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User newUser = User.builder()
                .firstName(regRequest.getFirstName())
                .otherName(regRequest.getOtherName())
                .lastName(regRequest.getLastName())
                .gender(regRequest.getGender())
                .address(regRequest.getAddress())
                .stateOfOrigin(regRequest.getStateOfOrigin())
                .accountNumber(AccountUtil.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(regRequest.getEmail())
                .password(passwordEncoder.encode(regRequest.getPassword()))
                .phoneNumber(regRequest.getPhoneNumber())
                .alternativePhoneNumber(regRequest.getPhoneNumber())
                .build();

        User savedUser = userRepository.save(newUser);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Welcome To Ojemba Bank Plc.")
                .messageBody("Hello " + savedUser.getFirstName() + " " + savedUser.getOtherName() + " " + savedUser.getLastName() + ",\n" +
                        "We want to specially thank you for choosing Ojemba. Indeed, we stand true to our name as we promise you a jolly ride. \n" +
                        "Kindly find your account details below: \n" +
                        "Account Number: " + savedUser.getAccountNumber() + ",\n" +
                        "Account Balance: NGN" + savedUser.getAccountBalance() + ".00\n" +
                        "PLEASE FEEL FREE TO REACH OUT TO US FOR ANY ENQUIRIES THROUGH ANY OF OUR CHANNELS.\n" +
                        "\n Kind regards,\n" +
                        "Chukwu Joel Chimaobi\n" +
                        "Head, Customer Success Dept!")
                .build();

        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtil.ACCOUNT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(regRequest.getFirstName() + " " + regRequest.getOtherName() + " " + regRequest.getLastName())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public Long count() {
        return userRepository.count();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        Optional<User> foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        if (foundUser.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtil.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.get().getAccountBalance())
                        .accountNumber(foundUser.get().getAccountNumber())
                        .accountName(foundUser.get().getFirstName() + " " + foundUser.get().getOtherName() + " " + foundUser.get().getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        Optional<User> foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return foundUser.map(user -> user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName()).orElse(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE);

    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        Optional<User> foundUser = userRepository.findByAccountNumber(request.getAccountNumber());

        if (foundUser.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToBeCredited = foundUser.get();

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.INVALID_AMOUNT_CODE)
                    .responseMessage(AccountUtil.INVALID_AMOUNT_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        userToBeCredited.setAccountBalance(userToBeCredited.getAccountBalance().add(request.getAmount()));

        saveUser(request, userToBeCredited, "CR");

        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_CREDIT_SUCCESS_CODE)
                .responseMessage(AccountUtil.ACCOUNT_CREDIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToBeCredited.getFirstName() + " " + userToBeCredited.getOtherName() + " " + userToBeCredited)
                        .accountNumber(userToBeCredited.getAccountNumber())
                        .accountBalance(userToBeCredited.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        Optional<User> foundUser = userRepository.findByAccountNumber(request.getAccountNumber());

        if (foundUser.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToBeDebited = foundUser.get();

        if (validateAmount(request, userToBeDebited)) return BankResponse.builder()
                .responseCode(AccountUtil.INSUFFICIENT_BALANCE_CODE)
                .responseMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE)
                .accountInfo(null)
                .build();

        userToBeDebited.setAccountBalance(userToBeDebited.getAccountBalance().subtract(request.getAmount()));

        saveUser(request, userToBeDebited, "DR");

        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_DEBIT_SUCCESS_CODE)
                .responseMessage(AccountUtil.ACCOUNT_DEBIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToBeDebited.getFirstName() + " " + userToBeDebited.getOtherName() + " " + userToBeDebited.getLastName())
                        .accountNumber(userToBeDebited.getAccountNumber())
                        .accountBalance(userToBeDebited.getAccountBalance())
                        .build())
                .build();
    }

    private void saveUser(CreditDebitRequest request, User userToBeDebited, String transactionType) {
        userRepository.save(userToBeDebited);

        String debitCustomerName = userToBeDebited.getFirstName() + " " + userToBeDebited.getOtherName() + " " + userToBeDebited.getLastName();

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType(transactionType)
                .senderAccountName(debitCustomerName)
                .senderAccountNumber(userToBeDebited.getAccountNumber())
                .recipientAccountName(null)
                .recipientAccountNumber(null)
                .transactionAmt(request.getAmount())
                .description(null)
                .build();
        transactionService.saveTransaction(transactionDto);
    }

    private static boolean validateAmount(CreditDebitRequest request, User userToBeDebited) {
        return request.getAmount().compareTo(userToBeDebited.getAccountBalance()) > 0;
    }

    @Override
    public BankResponse fundsTransfer(TransferRequest transferRequest) {
        Optional<User> sender = userRepository.findByAccountNumber(transferRequest.getSourceAccount());
        Optional<User> receiver = userRepository.findByAccountNumber(transferRequest.getDestinationAccount());

        if (sender.isEmpty() || receiver.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User sourceAccount = sender.get();
        User recipientAccount = receiver.get();

        if (sourceAccount.getAccountBalance().compareTo(transferRequest.getTransferAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User updatedSender = getUser(transferRequest, sourceAccount, recipientAccount);

        return BankResponse.builder()
                .responseCode(AccountUtil.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtil.TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(updatedSender.getFirstName() + " " + sourceAccount.getOtherName() + " " + sourceAccount.getLastName())
                        .accountNumber(updatedSender.getAccountNumber())
                        .accountBalance(updatedSender.getAccountBalance())
                        .build())
                .build();
    }

    private User getUser(TransferRequest transferRequest, User sourceAccount, User recipientAccount) {
        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(transferRequest.getTransferAmount()));
        userRepository.save(sourceAccount);

        recipientAccount.setAccountBalance(recipientAccount.getAccountBalance().add(transferRequest.getTransferAmount()));
        userRepository.save(recipientAccount);

        User updatedSender = userRepository.findByAccountNumber(sourceAccount.getAccountNumber()).get();

        String senderName = sourceAccount.getFirstName() + " " + sourceAccount.getOtherName() + " " + sourceAccount.getLastName();
        String recipientName = recipientAccount.getFirstName() + " " + recipientAccount.getOtherName() + " " + recipientAccount.getLastName();

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("TFR")
                .senderAccountName(senderName)
                .senderAccountNumber(sourceAccount.getAccountNumber())
                .recipientAccountName(recipientName)
                .recipientAccountNumber(recipientAccount.getAccountNumber())
                .transactionAmt(transferRequest.getTransferAmount())
                .description(transferRequest.getDescription())
                .build();
        transactionService.saveTransaction(transactionDto);
        return updatedSender;
    }

    @Override
    public Optional<User> findByAccountNumber(String senderAccountNumber) {
        return userRepository.findByAccountNumber(senderAccountNumber);
    }

    public List<Transactions> generateAccountStatement(TransactionEnquiryReq req) throws DocumentException, FileNotFoundException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate startDate = LocalDate.parse(req.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(req.getEndDate(), formatter);

        List<Transactions> transactionsList = transactionService.findAll().stream()
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
        Optional<User> user = findByAccountNumber(firstTransaction.getSenderAccountNumber());

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
