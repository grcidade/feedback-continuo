package br.com.dbc.vimserdev.feedbackcontinuo.enums;

public enum Tags {

    JAVA_SCRIPT(1, "java_script"),
    BACKEND(2, "Backend"),
    FRONTEND(3, "Frontend"),
    AGILE(4, "Agile"),
    AMAZON_AWS(5, "Amazon_AWS"),
    ANALISE(6, "Analise"),
    ANDROID(7, "Android"),
    IOS(8, "IOS"),
    ANGULAR(9, "Angular"),
    ARQUITETURA(10, "Arquitetura"),
    AUTOMACAO(11, "Automação"),
    BANCO_DE_DADOS(12, "Banco_de_Dados"),
    PHP(13, "PHP"),
    RUBY(14, "Ruby"),
    SPRING(15, "Spring"),
    SOLID(16, "SOLID"),
    WEB(17, "Web"),
    JAVA(18, "Java");

    private final Integer id;
    private final String name;

    Tags(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Integer getId(){
        return id;
    }
}
