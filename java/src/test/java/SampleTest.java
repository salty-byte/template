import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SampleTest {

  @Test
  public void helloTest() {
    Sample sample = new Sample("Mike");
    assertEquals("Hello, I am Mike.", sample.getHello());
  }
}
