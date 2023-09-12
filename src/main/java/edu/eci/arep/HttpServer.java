package edu.eci.arep;

import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HttpServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        boolean running = true;
        // String inputLine, outputLine;
        // PrintWriter out = ;
        // BufferedReader in;
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            String uriS = null;
            boolean fline = true;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (fline) {
                    uriS = inputLine.split(" ")[1];
                    System.out.println(uriS);
                    fline = false;
                }
                if (!in.ready()) {
                    break;
                }
            }
            if (uriS.startsWith("/consulta")) {
                outputLine = getResponse(uriS);
            } else if (uriS.startsWith("/consultaPost")) {
                outputLine = getResponse(uriS);
            } else {
                outputLine = homeIndex();
            }
            out.println(outputLine);
            out.close();
            in.close();
        }
        clientSocket.close();
        serverSocket.close();
    }

    public static String getResponse(String uriS) {
        String command = uriS.split("=")[1];
        String output = getCommand(command);
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <body>\n" +
                "        " + output + "\n" +
                "    </body>\n" +
                "</html>";
    }

    public static String getCommand(String command) {
        String output;
        if (command.startsWith("Class")) {
            output = classTypeInput(command);
        } else if (command.startsWith("invoke")) {
            output = invokeTypeInput(command);
        } else if (command.startsWith("unaryInvoke")) {
            output = unaryTypeInput(command);
        } else if (command.startsWith("binaryInvoke")) {
            output = binaryInvokeType(command);
        }else{
            output = "Querry Not Supported";
        }
        return output;
    }

    public static String classTypeInput(String command) {
        String className = command.substring(6, command.length() - 1);
        String output = "Declared Methods : ";
        Method[] declaredMethods = null;
        try {
            declaredMethods = Class.forName(className).getDeclaredMethods();
            for(Method m : declaredMethods){
                output += ", " + m + "\r\n"; 
            }
        } catch (SecurityException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Field[] declaredField = null;
        output += " \n Declared Fields: ";
        try {
            declaredField =Class.forName(className).getDeclaredFields();
            for(Field m : declaredField){
                output+= ", " + m;
            }
        } catch (SecurityException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return output;
    }

    public static String invokeTypeInput(String command) {
        
        String brute = command.substring(7, command.length() - 1);
        String className = brute.split(",")[0];
        System.out.println(className);
        return "aaaaaaaaaaaaa";
    }

    public static String unaryTypeInput(String command) {
        String brute = command.split("(")[1];
        String className = brute.substring(1, brute.length() - 1);
        System.out.println(className);
        return "aaaaaaaaaaaaa";
    }

    public static String binaryInvokeType(String command) {
        String brute = command.split("(")[1];
        String className = brute.substring(1, brute.length() - 1);
        System.out.println(className);
        return "aaaaaaaaaaaaa";
    }

    public static String homeIndex() {
        String output = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>Form Example</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>Form with GET</h1>\n"
                + "        <form action=\"/hello\">\n"
                + "            <label for=\"name\">Name:</label><br>\n"
                + "            <input type=\"text\" id=\"name\" name=\"comando\" value=\"John\"><br><br>\n"
                + "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n"
                + "        </form> \n"
                + "        <div id=\"getrespmsg\"></div>\n"
                + "\n"
                + "        <script>\n"
                + "            function loadGetMsg() {\n"
                + "                let nameVar = document.getElementById(\"name\").value;\n"
                + "                const xhttp = new XMLHttpRequest();\n"
                + "                xhttp.onload = function() {\n"
                + "                    document.getElementById(\"getrespmsg\").innerHTML =\n"
                + "                    this.responseText;\n"
                + "                }\n"
                + "                xhttp.open(\"GET\", \"/consulta?comando=\"+nameVar);\n"
                + "                xhttp.send();\n"
                + "            }\n"
                + "        </script>\n"
                + "\n"
                + "    </body>\n"
                + "</html>";
        return output;
    }

}
