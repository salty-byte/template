import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SampleTest {

  @Test
  void helloTest() {
    Sample sample = new Sample("Mike");
    assertEquals("Hello, I am Mike.", sample.getHello());
  }
}
