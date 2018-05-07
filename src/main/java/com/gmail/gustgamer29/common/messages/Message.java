package com.gmail.gustgamer29.common.messages;

public enum Message {

    MISSING_PLUGIN("&aUm plugin necessário para a funcionalidade correta do plugin não foi encontrado no servidor! desligando plugin. \n&aCertifique-se de que estes plugins estejam presentes na pasta plugins&7:&f %s"),

    PLUGIN_FOUNDED("&e%s &afoi encontrado no servidor! versão&7:&e %s1"),

    SERVER_VERSION_RUNNING("&bO servidor está rodando na versão &a%s"),

    PLUGIN_ACTVATED("&aPlugin &e%s ativado! build&7:&e %s1");


    private String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
