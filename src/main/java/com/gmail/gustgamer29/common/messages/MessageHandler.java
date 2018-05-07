package com.gmail.gustgamer29.common.messages;

public class MessageHandler {

    public static String replaceMessage(Message message, String... var) {
        String msg = message.getMessage();
        if (var == null) {
            return msg;
        }
        for (int i = 0; i < var.length; i++) {
            msg = msg.replaceAll(i == 0 ? "%s" : "%s" + i, var[i]);
        }
        return msg;
    }
}
