package entity;

import enums.typeTransaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public record  Transaction (String id , Date date , double montant , String idCompte , typeTransaction type  , String lieu  ){
};
