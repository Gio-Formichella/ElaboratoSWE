//define a class for the DomainModel package
package main.DomainModel;

class Visitor{
    private String name;
    private String surname;
    private String emailAddress;
    private boolean NLSubscriber;

    public Visitor(String name, String surname, String emailAddress, boolean NLSubscriber){
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.NLSubscriber = NLSubscriber;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getSurname(){
        return surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }

    public boolean isNLSubscriber(){
        return NLSubscriber;
    }

    public void setNLSubscriber(boolean NLSubscriber){
        this.NLSubscriber = NLSubscriber;
    }
}