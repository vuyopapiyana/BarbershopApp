/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vuyopapiyana
 */
public class Barber {
    private String name;
    private String surname;
    private int amountOfCuts;
    private int barberID;
    private double[] cost = new double [20];
    private String [] haircuts = new String[20] ;
    
    //Parameterised constructor used to initialise fields of barber class
    public Barber(String n, String s, int bID){
        name = n;
        surname = s;
        barberID = bID;
        
    }
    
    //Method to display the fields of the barber object
    public String toString(){
        String cuts="";
        for (int x = 0; x< haircuts.length;x++) {
            cuts = cuts+haircuts[x]+"\tR"+cost[x]+"\t";
     
        }
        return barberID+"\t"+name+"\t"+surname+"\t"+cuts;
    }
    
      /**
     * •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
     * Series of getters and setters for all the fields of the class
     * •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
     */
    public String getName(){
        return name;
    }
    
    public String getSurname(){
        return surname;
    }
    
    public int getBarberID(){
        return barberID;
    }
    
    public void setHaircut(String h, int i){
        haircuts[i] = h;
    }
    public void setHaircuts (String []h){
        haircuts = h;
    }
    
    public String[] getHaircuts(){
        return haircuts;
    }
    
    public void setCost(double c, int i){
        cost[i] = c;
    }
    
    public String getHaircut(int index){
        return haircuts[index];
    }
    
    public double getCost(int index){
        return cost[index];
    }
    
    public void setAmountCuts(int c){
        amountOfCuts = c;
    }
    
    public int getAmountOfCuts(){
        return amountOfCuts;
    }
    
}//end of class
