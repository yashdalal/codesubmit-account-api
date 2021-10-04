package com.ydalal.accounts;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.ydalal.accounts.exceptions.AccountNotFoundException;
import com.ydalal.accounts.exceptions.CustomerNotFoundException;
import com.ydalal.accounts.exceptions.NotEnoughBalanceException;
import com.ydalal.accounts.handlers.Handler;
import com.ydalal.accounts.models.Transaction;
import com.ydalal.accounts.module.AppModule;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {

    public static void main( String[] args ) throws IOException {
        Injector injector = Guice.createInjector(new AppModule());
        Handler handler = injector.getInstance(Handler.class);

        // initializeDb - loading objects into memory to make lookups quicker
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/account/create", exchange -> {
            if (!exchange.getRequestMethod().equals("POST")) {
                writeOutput("", 404, exchange);
                throw new UnsupportedOperationException();
            }

            StringBuilder responseText = new StringBuilder(String.format("Create account %n"));
            int responseCode = 404;

            try {
                handler.createAccount(4, 4000);
                responseCode = 200;
                responseText.append("Account created");
            } catch (CustomerNotFoundException e) {
                responseText.append("Customer not found");
            }

            writeOutput(responseText.toString(), responseCode, exchange);
        });

        server.createContext("/account/transfer", exchange -> {
            if (!exchange.getRequestMethod().equals("POST")) {
                writeOutput("", 404, exchange);
                throw new UnsupportedOperationException();
            }

            StringBuilder responseText = new StringBuilder(String.format("Account transfer %n"));
            int responseCode = 404;

            try {
                handler.transfer(4, 1, 50);
                responseText.append("Transfer Successful");
                responseCode = 200;
            } catch (AccountNotFoundException e) {
                responseText.append("Account not found");
            } catch (NotEnoughBalanceException e) {
                responseText.append("Not enough balance");
            }

            writeOutput(responseText.toString(), responseCode, exchange);

        });

        server.createContext("/account/balance", exchange -> {
            if (!exchange.getRequestMethod().equals("GET")) {
                writeOutput("", 404, exchange);
                throw new UnsupportedOperationException();
            }

            StringBuilder responseText = new StringBuilder("Account balance");
            int responseCode = 200;

            try {
                handler.retrieveBalance(2);
            } catch (AccountNotFoundException e) {
                responseText.append("Account not found");
                responseCode = 404;
                System.out.println(responseText);
            }

            writeOutput(responseText.toString(), responseCode, exchange);
        });

        server.createContext("/account/transactions", exchange -> {
            if (!exchange.getRequestMethod().equals("GET")) {
                writeOutput("", 404, exchange);
                throw new UnsupportedOperationException();
            }

            StringBuilder responseText = new StringBuilder();
            int responseCode = 200;

            try {
                List<Transaction> transactions = handler.retrieveTransferHistory(1);
                for (Transaction transaction: transactions) {
                    responseText.append(String.format("$%d was transferred from account %d to account %d%n",
                            transaction.getTransferAmount(), transaction.getSenderId(), transaction.getReceiverId()));
                }
            } catch (AccountNotFoundException e) {
                responseText.append("Account not found");
                responseCode = 404;
            }

            if (responseText.length() == 0) {
                responseText.append("No transactions found");
            }

            writeOutput(responseText.toString(), responseCode, exchange);
        });

        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.printf("Server is now running at localhost:%d...%n", port);
    }

    private static void writeOutput(final String responseText,
                                    final int responseCode,
                                    final HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(responseCode, responseText.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(responseText.getBytes());
        output.flush();
        exchange.close();
    }
}
