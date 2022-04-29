package br.com.dbc.vimserdev.feedbackcontinuo.enums;

public enum Tags {

    JAVASCRIPT("JavaScript"),
    BACKEND("Backend"),
    FRONTEND( "Frontend"),
    AGILE("Agile"),
    AWS("AWS"),
    ANALISE( "Analise"),
    ANDROID("Android"),
    IOS("IOS"),
    ANGULAR("Angular"),
    ARQUITETURA("Arquitetura"),
    DATABASE("Database"),
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
