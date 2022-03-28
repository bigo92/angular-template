const { ModuleFederationPlugin } = require('webpack').container;

module.exports = {
  output: {
    publicPath: "http://localhost:4203/",
    uniqueName: "example"
  },
  optimization: {
    // Only needed to bypass a temporary bug
    runtimeChunk: false
  },
  plugins: [
    new ModuleFederationPlugin({
      name: "example",
      library: { type: "var", name: "example" },
      filename: "remoteEntry.js",
      exposes: {
        './PublicModule': './projects/example/src/app/public/public.module.ts',
      },
      shared: ["@angular/core", "@angular/common", "@angular/router"]
    })
  ],
};
