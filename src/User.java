/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vuyopapiyana
 */
public class User {
    private String username;
    private String password;
    private String name;
    private String surname;
    private int cellPhone;
    private String email;
    private int userID;
     
    //Parameterised Constructor used to initialise the fields of the class.
    public User(int uID ,String n, String s, String u, String p, int cell, String e){
        username = u;
        password = p;
        name = n;
        surname = s;
        cellPhone = cell;
        email = e;
        userID = uID;
                
                
    } 
    
    //Displays the fields of the class
    @Override
    public String toString(){
        return userID+"\t"+name+"\t"+surname+"\t"+username+"\t"+cellPhone+"\t"+email;
    }
    
    /**
     * •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
     * Series of getters and setters for all the fields of the class
     * •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
     */
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getName(){
        return name;
    }
    
    public String getSurname(){
        return surname;
    }
    
    public int getCell(){
        return cellPhone;
    }
    
    public String getEmail(){
        return email;
    }
    
    public int getUserID(){
        return userID;
    }
    
    //method used when updating all fields of the class.
    public void setAllFields(String n, String s,String u, String p, String e, int c){
        name = n;
        surname = s;
        password = p;
        email = e;
        cellPhone = c;
    }
  
}// class
