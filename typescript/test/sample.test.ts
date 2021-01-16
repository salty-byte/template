import { Sample } from '../src/sample';

describe('Sample class test', () => {
  describe('hello test', () => {
    test('returns hello message', () => {
      const sample = new Sample('Mike');
      expect(sample.hello()).toBe('Hello, I am Mike.');
    });
  });
});
