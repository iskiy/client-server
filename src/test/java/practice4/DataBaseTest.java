package practice4;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class DataBaseTest {
    private static final DataBase db = new DataBase();

    @BeforeAll
    static void initDataBase(){
        db.initDataBase(":memory:");
    }

    @BeforeEach
    void fillDataBase(){
        db.insertProduct(new Product("buckwheat", 10, 500));
        db.insertProduct(new Product("bread", 20, 100));
        db.insertProduct(new Product("water", 16, 1000));
        db.insertProduct(new Product("milk", 30, 100.5));
        db.insertProduct(new Product("milk1", 40, 200.5));
        db.insertProduct(new Product("milk2", 50, 300.5));
    }

    @AfterEach
    void clean(){
        db.deleteAll();
    }

    @Test
    void testGetAllProduct(){
        List<Product> products = db.getAllProducts();
        assertThat(products)
            .extracting(Product::getName)
            .containsExactly("buckwheat", "bread", "water", "milk", "milk1", "milk2");
    }

    @Test
    void testInsertProduct(){
        Product table = new Product("table", 1000, 50);
        db.insertProduct(table);
        List<Product> products = db.getAllProducts();
        assertThat(products).contains(table);
    }

    @ParameterizedTest
    @MethodSource("deleteProductProvider")
    void testDeleteProduct(Product pr){
        db.deleteProduct(pr);
        List<Product> products = db.getAllProducts();
        assertThat(products).doesNotContain(pr);
    }

    @ParameterizedTest
    @MethodSource("deleteByCriteriaProvider")
    void deleteProductByCriteria(ProductCriteria criteria, List<Product> notExpected){
        db.deleteProductByCriteria(criteria);
        List<Product> products = db.getAllProducts();
        assertThat(products).doesNotContainAnyElementsOf(notExpected);
    }

    @ParameterizedTest
    @MethodSource("filterArgumentProvider")
    void testGetAllByCriteria(ProductCriteria criteria, List<Product> expected){
        List<Product> products = db.getAllByCriteria(criteria);
        assertThat(products)
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @ParameterizedTest
    @MethodSource("updateArgumentProvider")
    void testUpdate(ProductCriteria criteria,UpdateRules updateRules, List<Product> expected){
        db.updateProduct(updateRules, criteria);
        List<Product> products = db.getAllProducts();
        assertThat(products)
                .containsAnyElementsOf(expected);
    }

    private static Stream<Arguments> deleteProductProvider(){
        return Stream.of(
                Arguments.of(new Product("buckwheat", 10, 500)),
                Arguments.of(new Product(2, "water", 16, 1000)),
                Arguments.of(new Product("milk2", 50, 300.5))
        );
    }

    private static Stream<Arguments> deleteByCriteriaProvider(){
        return Stream.of(
                Arguments.of(
                        new ProductCriteria(null, null, "milk", 20.0,
                                null, null, null),
                        List.of(
                                List.of(
                                        new Product(4,"milk", 30, 100.5),
                                        new Product(5,"milk1", 40, 200.5),
                                        new Product(6,"milk2", 50, 300.5))
                        )
                ),
                Arguments.of(
                        new ProductCriteria(null, null, null, 5.0,
                                25.0, null, null),
                        List.of(
                                new Product(1,"buckwheat", 15, 100.0),
                                new Product(2,"bread", 20, 100),
                                new Product(3,"water", 16, 1000)
                        )
                )
        );

    }
    private static Stream<Arguments> updateArgumentProvider(){
        return Stream.of(
                Arguments.of(
                        new ProductCriteria(null, null, "milk", 40.0,
                                null, null, null),
                        new UpdateRules("super-milk", 100.0, 1000.0),
                        List.of(
                                new Product(6,"super-milk", 100.0, 1000.0)
                        )
                ),
                Arguments.of(
                        new ProductCriteria(null, null, null, 5.0,
                                25.0, null, null),
                        new UpdateRules(null, 15.0, 100.0),
                        List.of(
                                new Product(1,"buckwheat", 15, 100.0)
                        )
                ),
                Arguments.of(
                        new ProductCriteria(null, "water", null, 10.0,
                                30.0, 600.0, null),
                        new UpdateRules(null, 50.0, null),
                        List.of(
                                new Product(3, "water", 50.0, 1000.0)
                        )
                )
        );
    }

    private static Stream<Arguments> filterArgumentProvider(){
        return Stream.of(
                Arguments.of(
                        new ProductCriteria(null, null, "milk", null,
                                null, null, null),
                        List.of(
                                new Product(4,"milk", 30, 100.5),
                                new Product(5,"milk1", 40, 200.5),
                                new Product(6,"milk2", 50, 300.5))
                ),
                Arguments.of(
                        new ProductCriteria(null, null, null, 28.0,
                                41.0, null, null),
                        List.of(
                                new Product(4,"milk", 30, 100.5),
                                new Product(5,"milk1", 40, 200.5)
                )),
                Arguments.of(
                        new ProductCriteria(1, null, null, 5.0,
                                30.0, null, null),
                        List.of(
                                new Product(1,"buckwheat", 10, 500)
                            ))
                );

    }

}