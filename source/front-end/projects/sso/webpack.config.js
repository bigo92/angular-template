const { ModuleFederationPlugin } = require('webpack').container;

module.exports = {
  output: {
    publicPath: "http://localhost:4202/",
    uniqueName: "sso"
  },
  optimization: {
    // Only needed to bypass a temporary bug
    runtimeChunk: false
  },
  plugins: [
    new ModuleFederationPlugin({
      name: "sso",
      library: { type: "var", name: "sso" },
      filename: "remoteEntry.js",
      exposes: {
        './PublicModule': './projects/sso/src/app/public/public.module.ts',
      },
      shared: {
        "@angular/core": {eager: true, singleton: true},
        "@angular/common": {eager: true, singleton: true},
        "@angular/router": {eager: true, singleton: true}
      }
    })
  ],
};
