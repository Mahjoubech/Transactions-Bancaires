package entity;

import enums.typeCompte;

public sealed class Compte  permits  CompteCourant, CompteEpargne{
    protected String id ;
    protected String num;
    protected String idClient;
    protected double solde;
    protected typeCompte typeCompte;

    public Compte(String id, String num, String idClient, double solde, typeCompte typeCompte) {
        this.id = id;
        this.num = num;
        this.idClient = idClient;
        this.solde = solde;
        this.typeCompte = typeCompte;
    }
    public String getId() {
        return id;
    }
    public String getNum() {
        return num;
    }
    public String getIdClient() {
        return idClient;
    }
    public double getSolde() {
        return solde;
    }
    public typeCompte getTypeCompte() {
        return typeCompte;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setNum(String num) {
        this.num = num;
    }
    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }
    public void setSolde(double solde) {
        this.solde = solde;
    }
    public void setTypeCompte(typeCompte typeCompte) {
        this.typeCompte = typeCompte;
    }
    @Override
    public String toString() {
        return "Compte{" +
                "id='" + id + '\'' +
                ", num='" + num + '\'' +
                ", solde=" + solde +
                ", typeCompte=" + typeCompte +
                '}';
    }


}