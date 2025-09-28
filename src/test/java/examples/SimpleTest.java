package examples;

import com.intuit.karate.junit5.Karate;

public class SimpleTest {

    @Karate.Test
    Karate testAll() {
   //     return Karate.run("src/test/resources/examples/compare.feature");

        return Karate.run("src/test/resources/examples/compare.feature");
  //      "src/test/resources/examples/compare.feature");
    }
}
