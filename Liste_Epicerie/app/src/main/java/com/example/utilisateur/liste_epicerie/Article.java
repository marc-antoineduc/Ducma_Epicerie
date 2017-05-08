package com.example.utilisateur.liste_epicerie;

/**
 * Created by utilisateur on 11/04/2017.
 */
public class Article {
     private String IdCategorie ="",NomCategorie="",IdArticle="",NomArticle="";

    public Article(){

    }
    public void setIdCategorie(String idCategorie){
        IdCategorie = idCategorie;
    }
    public String getIdCategorie(){
        return IdCategorie ;
    }
    public void setNomCategorie (String nomCategorie){
        NomCategorie = nomCategorie;
    }
    public String getNomCategorie(){
        return NomCategorie ;
    }
    public void setIdArticle(String idArticle){
        IdArticle = idArticle;
    }
    public String getIdArticle(){
        return IdArticle ;
    }
    public void setNomArticle(String nomArticle){
        NomArticle = nomArticle;
    }
    public String getNomArticle(){
        return NomArticle ;
    }
}
