package model.entity;

import model.enums.typeCompte;
import model.enums.typeTransaction;

import java.util.Date;

public record  Transaction (String id , Date date , double montant , String idCompte , typeTransaction type  , String lieu  ){
};
