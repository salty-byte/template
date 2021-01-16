export default {
  clearMocks: true,
  coverageDirectory: './coverage',
  coverageProvider: 'v8',
  testEnvironment: 'node',
  testMatch: ['**/test/**/*.test.ts'],
  transform: {
    '^.+\\.ts?$': 'ts-jest',
  },
  verbose: true,
};
