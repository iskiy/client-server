package practice4;

import java.util.ArrayList;
import java.util.List;

public class ProductCriteria {
    private Integer idEqual;
    private String nameEqual;
    private String nameLike;
    private Double priceFrom;
    private Double priceTill;
    private Double amountFrom;
    private Double amountTill;

    public ProductCriteria(Integer idEqual, String nameEqual, String nameLike,
                           Double priceFrom, Double priceTill, Double amountFrom,
                           Double amountTill) {
        this.idEqual = idEqual;
        this.nameEqual = nameEqual;
        this.nameLike = nameLike;
        this.priceFrom = priceFrom;
        this.priceTill = priceTill;
        this.amountFrom = amountFrom;
        this.amountTill = amountTill;
    }


    public String getSQLCondition()
    {
        String res;
        List<String> criterias = new ArrayList<>();

        if(idEqual != null){
            criterias.add(" id = " + idEqual + " ");
        }
        if(nameEqual != null){
            criterias.add(" name = '" + nameEqual + "' ");
        }
        if(nameLike != null){
            criterias.add(" name like '%" + nameLike + "%' ");
        }
        if(priceFrom != null){
            criterias.add(" price >=" + priceFrom + " ");
        }
        if(priceTill != null){
            criterias.add(" price <=" + priceTill + " ");
        }
        if(amountFrom != null){
            criterias.add(" amount >=" + amountFrom + " ");
        }
        if(amountTill != null){
            criterias.add(" amount <=" + amountTill + " ");
        }

        res = String.join(" and ", criterias);
        if(!criterias.isEmpty()){
            res = " where" + res;
        }
        return res;
    }
}
