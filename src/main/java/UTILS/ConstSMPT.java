
package UTILS;

/**
 *
 * @author angela
 */
public class ConstSMPT {
    public static String HELO = "HELO ";
    public static String FINLINE = "\r\n";
    public static String MAIL_FROM = "MAIL FROM: ";
    public static String RCPT_TO = "RCPT TO: ";
    public static String DATA = "DATA" + ConstSMPT.FINLINE;
    public static String SUBJECT = "SUBJECT: ";
    public static String FINSUBJECT = ".\r\n";
    public static String QUIT = "QUIT" + ConstSMPT.FINLINE;
    public static String Subject = "Subject: ";
    public static String RETURNPATH = "Return-Path: ";
}
