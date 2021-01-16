export class Sample {
  private name: string;

  constructor(name: string) {
    this.name = name;
  }

  hello(): string {
    return `Hello, I am ${this.name}.`;
  }
}
