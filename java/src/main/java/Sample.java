public class Sample {
  private String name;

  public Sample(String name) {
    this.name = name;
  }

  public String getHello() {
    return "Hello, I am " + this.name + ".";
  }

  public static void main(String[] args) {
    Sample sample = new Sample("Mike");
    System.out.println(sample.getHello());
  }
}
