package br.com.dbc.vimserdev.feedbackcontinuo.enums;

public enum Tags {

    JAVA_SCRIPT("JavaScript"),
    BACKEND("Backend"),
    FRONTEND( "Frontend"),
    AGILE("Agile"),
    AMAZON_AWS("Amazon AWS"),
    ANALISE( "Analise"),
    ANDROID("Android"),
    IOS("IOS"),
    ANGULAR("Angular"),
    ARQUITETURA("Arquitetura"),
    AUTOMACAO("Automação"),
    BANCO_DE_DADOS("Banco de Dados"),
    PHP("PHP"),
    RUBY("Ruby"),
    SPRING("Spring"),
    SOLID("SOLID"),
    WEB("Web"),
    JAVA("Java"),
    CSS("CSS");

    private final String name;

    Tags(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
