package practice4;

import java.util.ArrayList;
import java.util.List;

public class UpdateRules {
    private String name;
    private Double price;
    private Double amount;

    public UpdateRules(String name, Double price, Double amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public String getSetString(){
        String res;
        List<String> sets = new ArrayList<>();

        if(name != null){
            sets.add(" name =  '" + name + "'");
        }
        if(price != null){
            sets.add(" price =  " + price);
        }
        if(amount != null){
            sets.add(" amount = " + amount);
        }
        res = String.join(", ", sets);
        if(!sets.isEmpty()) {
            res = " SET " + res;
        }

        return res;
    }
}
