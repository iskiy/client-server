package practice5;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import com.sun.net.httpserver.HttpServer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Strings;
import practice4.DataBase;
import practice4.Entities.Product;
import practice4.Entities.User;
import practice4.UpdateRules;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyServer {
    private static final byte[] API_KEY_SECRET_BYTES = "super-mega-secret-key-4347t5jsd3q75wd,9423894d2q7d84ynq8734dnqy9gf7283f5gf235fny195".getBytes(StandardCharsets.UTF_8);
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final Key SIGNING_KEY = new SecretKeySpec(API_KEY_SECRET_BYTES, SIGNATURE_ALGORITHM.getJcaName());
    private static final String API_GOOD_PATH = "/api/good";


    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(6060), 0);
        DataBase db = new DataBase();
        db.initDataBase("warehouse");
        db.insertProduct(new Product("bread", 10, 500));
        db.insertProduct(new Product("buckwheat", 10, 100));

        db.insertUser(new User("login1", "password1"));
        db.insertUser(new User("login2", "password2"));


        server.start();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        server.createContext("/", exchange -> {
            byte[] response = "{\"status\": \"ok\" }".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });

        server.createContext("/login", exchange -> {
            if (exchange.getRequestMethod().equals("POST")) {
                User user = objectMapper.readValue(exchange.getRequestBody(), User.class);
                User userFromDb = db.getUserByLogin(user.getLogin());
                if(userFromDb != null)
                {
                    if(userFromDb.getPassword().equals(user.getPassword()))
                    {
                        String jwt = createJWTFromLogin(userFromDb.getLogin());
                        System.out.println(getUserLoginFromJWT(jwt));
                        exchange.getResponseHeaders().set("Authorization", jwt);
                        exchange.sendResponseHeaders(200, 0);
                    }
                    else {
                        exchange.sendResponseHeaders(401, 0);
                    }
                }
                else {
                    exchange.sendResponseHeaders(401, 0);
                }
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        });

        Authenticator authenticator = new Authenticator() {
            @Override
            public Result authenticate(HttpExchange exchange) {
                String jwt= exchange.getRequestHeaders().getFirst("Authorization");
                if(jwt!=null) {
                    String login = getUserLoginFromJWT(jwt);
                    User user = db.getUserByLogin(login);
                    if(user!=null){
                        return new Success(new HttpPrincipal(login,"admin"));
                    }
                }
                return new Failure(403);
            }
        };

        server.createContext(API_GOOD_PATH, exchange -> {
            if(exchange.getRequestMethod().equals("PUT")){
                Product product = objectMapper.readValue(exchange.getRequestBody(), Product.class);
                if(product != null) {
                    if(!product.isValidProduct()){
                        exchange.sendResponseHeaders(409, 0);
                    } else {
                        product = db.insertProduct(product);
                        byte[] response = new String("Created with " + product.getId() + " of created good").getBytes(StandardCharsets.UTF_8);
                        exchange.sendResponseHeaders(201, response.length);
                        exchange.getResponseBody().write(response);
                    }
                }
            } else exchange.sendResponseHeaders(405, 0);

        }).setAuthenticator(authenticator);

        server.createContext(API_GOOD_PATH + "/", exchange -> {
            Integer productId = null;
            String possibleId = exchange.getRequestURI().getPath().substring(API_GOOD_PATH.length() + 1);
            try {
                 productId = Integer.parseInt(possibleId);
            } catch (NumberFormatException e) {
                exchange.sendResponseHeaders(404, 0);
                exchange.close();
            }
            Product product = db.FindProductById(productId);
            if(product == null) {
                exchange.sendResponseHeaders(404, 0);
                exchange.getResponseBody().write("Not found".getBytes(StandardCharsets.UTF_8));
            }
            if(exchange.getRequestMethod().equals("POST")) {
                // some error, found no other way to fix
                String test = new String(exchange.getRequestBody().readAllBytes());
                test = Strings.replace(test, "\r\n", "");
                Product pr = objectMapper.readValue(test, Product.class);
                UpdateRules updateRules = new UpdateRules(pr.getName(), pr.getPrice(), pr.getAmount());
                if (!updateRules.isValid()) {
                    exchange.sendResponseHeaders(409, 0);
                } else {
                    db.updateProductById(productId, updateRules);
                    System.out.println(db.FindProductById(productId));
                    exchange.sendResponseHeaders(204, 0);
                }
            } else if (exchange.getRequestMethod().equals("GET")) {
                byte[] response = objectMapper.writeValueAsBytes(product);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length);
                exchange.getResponseBody().write(response);
            } else if(exchange.getRequestMethod().equals("DELETE")) {
                db.deleteProduct(product);
                exchange.sendResponseHeaders(204, 0);
            } else exchange.sendResponseHeaders(405, 0);
            exchange.close();
        })
                .setAuthenticator(authenticator);

    }

    private static String createJWTFromLogin(String login) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TimeUnit.HOURS.toMillis(24)))
                .setSubject(login)
                .signWith(SIGNING_KEY, SIGNATURE_ALGORITHM)
                .compact();
    }

    private static String getUserLoginFromJWT(String jwt) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(jwt).getBody();
            return claims.getSubject();
        } catch(RuntimeException e) {
            return null;
        }
    }

}
