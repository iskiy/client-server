package practice4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private Connection con;

    public void initDataBase(String name){
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + name);
            PreparedStatement createProductTable = con.prepareStatement("create table if not exists 'product' (" +
                    "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "'name' text," +
                    "'price' double," +
                    "'amount' double" +
                    ");"
            );
            createProductTable.executeUpdate();
        }catch(ClassNotFoundException e){
            System.out.println("Can`t find driver JDBC");
            e.printStackTrace();
            System.exit(0);
        }catch (SQLException e){
            System.out.println("SQR query exception");
            e.printStackTrace();
        }
    }

    public Product insertProduct(Product product){
        try{
            PreparedStatement statement = con.prepareStatement("INSERT INTO product(name, price, amount) VALUES (?, ?, ?)");

            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setDouble(3, product.getAmount());
            statement.executeUpdate();
            ResultSet resSet = statement.getGeneratedKeys();
            product.setId(resSet.getInt("last_insert_rowid()"));
            statement.close();
            return product;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Problem with insert product", e);
        }
    }

    public List<Product> getAllProducts(){
        try{
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM product");
            List<Product> products = new ArrayList<>();
            while (res.next()) {
                products.add(getProductFromResultSet(res));
            }
            res.close();
            return products;
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Problems with SQL query for select products", e);
        }
    }

    public List<Product> getAllByCriteria(ProductCriteria criteria){
        String condition = criteria.getSQLCondition();
        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM product" + condition);
            List<Product> products = new ArrayList<>();
            while(res.next()){
                products.add(getProductFromResultSet(res));
            }
            return products;
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Problems with SQL query for select products by criteria", e);
        }
    }

    private static Product getProductFromResultSet(ResultSet res) throws SQLException {
        return new Product(res.getInt("id"),
                            res.getString("name"),
                            res.getDouble("price"),
                            res.getDouble("amount"));
    }


    public void updateProduct(UpdateRules rules, ProductCriteria criteria){
        String set = rules.getSetString();
        String condition = criteria.getSQLCondition();
        if(set == ""){
            return;
        }
        try{
            Statement st = con.createStatement();
            st.executeUpdate("UPDATE product" + set + condition);
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Problem with UPDATE product", e);
        }
    }

    public void deleteProductByCriteria(ProductCriteria criteria){
        String condition = criteria.getSQLCondition();
        try{
            Statement st = con.createStatement();
            st.executeUpdate("DELETE FROM product" + condition);
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Problem with DELETE from product", e);
        }
    }

    public void deleteProduct(Product product){
        try{
            String condition = " WHERE ";
            if(product.getId() != null) {
                condition+= "id = " + product.getId();
            }else {
                condition += "name = '" + product.getName() + "' AND price = " + product.getPrice() + " AND amount = " +
                        product.getAmount();
            }
            Statement st = con.createStatement();
            st.executeUpdate("DELETE FROM product" + condition);
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Problem with DELETE from product", e);
        }
    }

    public void deleteAll() {
        try {
            PreparedStatement st = con.prepareStatement("DELETE FROM product");
            st.executeUpdate();
            st = con.prepareStatement("UPDATE 'sqlite_sequence' SET 'seq' = 0 WHERE name = 'product'");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Problem with DELETE from product", e);
        }
    }


    public static void main(String[] args) {
        DataBase db = new DataBase();
        db.initDataBase("warehouse");
        db.insertProduct(new Product("bread", 10, 500));
        db.insertProduct(new Product("buckwheat", 10, 100));
        db.getAllProducts();
        ProductCriteria pc = new ProductCriteria(null, null, "milk",
                null, null, null, null);
        List<Product> res = db.getAllByCriteria(pc);
        for(Product pr : res){
            System.out.println(pr);
        }
        UpdateRules ur = new UpdateRules(null, 25.0, null);
        db.updateProduct(ur, pc);
        db.deleteProduct(new Product("bread", 25, 500));
        res = db.getAllProducts();
        System.out.println("All");
        for(Product pr : res){
            System.out.println(pr);
        }
        db.deleteAll();

    }
}
