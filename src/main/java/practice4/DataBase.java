package practice4;

import practice4.Entities.Product;
import practice4.Entities.User;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private Connection con;

    public void initDataBase(String name){
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + name);
            deleteAll();
            PreparedStatement pst = con.prepareStatement("create table if not exists 'product' (" +
                    "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "'name' text," +
                    "'price' double," +
                    "'amount' double" +
                    ");"
            );
            pst.executeUpdate();
            pst = con.prepareStatement("create table if not exists 'users'" +
                    "( 'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "'login' text unique," +
                    "'password' text);");
            pst.executeUpdate();


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

    public User insertUser(User user){
        try{
            PreparedStatement statement = con.prepareStatement("INSERT INTO users(login, password) VALUES (?, ?)");

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();
            ResultSet resSet = statement.getGeneratedKeys();
            user.setId(resSet.getInt("last_insert_rowid()"));
            statement.close();
            return user;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Problem with insert user", e);
        }
    }

    public User getUserByLogin(String login){
        try{
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM users where login = '" + login + "'");
            if(res.next())
                return new User(res.getInt("id"), res.getString("login"), res.getString("password"));
        } catch(SQLException e) {
            throw new RuntimeException("Can`t get user", e);
        }
        return null;
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

    public Product FindProductById(int id){
        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM product WHERE id = " + id + ";");

            if(res.next()){
                return getProductFromResultSet(res);
            } else return null;
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Problems with SQL query for select product by id", e);
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

    public void updateProductById(Integer id, UpdateRules rules){
        String set = rules.getSetString();
        try{
            Statement st = con.createStatement();
            st.executeUpdate("UPDATE product" + set + " where id = " + id + ";");
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
            st = con.prepareStatement("DELETE FROM users");
            st.executeUpdate();
            st = con.prepareStatement("UPDATE 'sqlite_sequence' SET 'seq' = 0 WHERE name = 'users'");
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
