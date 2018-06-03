package ecnu.compiling.compilingmate.syntax.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParsingTable {
   TableUnit[][] table;
   int tSize; //终止符个数

    public List<String> getSymbolList() {
        return symbolList;
    }

    List<String> symbolList=new ArrayList<>();
   boolean conflicted;

   public ParsingTable(int symbolNum, int statesNum,List<String> tList,List<String> nList){
       conflicted=false;
       this.tSize=tList.size();
       table=new TableUnit[tList.size() + nList.size()][statesNum];
       for(int i=0;i<symbolNum;i++)
           for(int j=0;j<statesNum;j++)
                table[i][j]=new TableUnit();
       this.symbolList.addAll(tList);
       this.symbolList.addAll(nList);
   }

   public void setTable(int number,int type,int state,String terminal){
       if(table[symbolList.indexOf(terminal)][state].type!=-1){
           System.out.println("table has a conflict at "+terminal+state);
           System.out.println("current value: type"+table[symbolList.indexOf(terminal)][state].type+" number"+table[symbolList.indexOf(terminal)][state].number);
           System.out.println("new value: type"+type+" number"+number);
       }
       table[symbolList.indexOf(terminal)][state].number = number;
       table[symbolList.indexOf(terminal)][state].type = type;
   }


   public TableUnit getUnit(String symbol,int state){
       return table[symbolList.indexOf(symbol)][state];
   }

   public void printTable(){  // 0:actionTable, 1:gotoTable
                System.out.println("-----------parsing table-----------");
        for(String token:symbolList){
            String s = String.format("%-5s", token);
            System.out.print(s);
        }
       System.out.println();
        //action table
        for(int i=0;i<table[0].length;i++) {
            for(int j=0;j<tSize;j++) {
                String result="";
                switch (table[j][i].type){
                    case -1:
                        result="";
                        break;
                    case 0:
                        result="r"+table[j][i].number;
                        break;
                    case 1:
                        result="s"+table[j][i].number;
                        break;
                    case 2:
                        result="acc";
                        break;
                }
                String s = String.format("%-5s", result);
                System.out.print(s);
            }
            //goto table
            for(int j=tSize;j<table.length;j++){
                String output=(table[j][i].type==1)?String.valueOf(table[j][i].number):"";
                String s = String.format("%-5s", output);
                System.out.print(s);
            }
            System.out.println();
        }
   }

   public String[][] getTableStrs(){
       String[][] results=new String[table[0].length][table.length];
       for(int i=0;i<table[0].length;i++) {
           for(int j=0;j<tSize;j++) {
               String result="";
               switch (table[j][i].type){
                   case -1:
                       result="";
                       break;
                   case 0:
                       result="r"+table[j][i].number;
                       break;
                   case 1:
                       result="s"+table[j][i].number;
                       break;
                   case 2:
                       result="acc";
                       break;
               }
               results[i][j]=result;
           }
           //goto table
           for(int j=tSize;j<table.length;j++){
               String output=(table[j][i].type==1)?String.valueOf(table[j][i].number):"";
               results[i][j]=output;
           }
       }
       return results;
   }

   public void conflicted(){
       conflicted=true;
       System.out.println("conflicted");
   }
}
