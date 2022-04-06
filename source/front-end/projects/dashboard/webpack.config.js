const { ModuleFederationPlugin } = require('webpack').container;

module.exports = {
  output: {
    publicPath: "http://localhost:4201/",
    uniqueName: "dashboard"
  },
  optimization: {
    // Only needed to bypass a temporary bug
    runtimeChunk: false
  },
  plugins: [
    new ModuleFederationPlugin({
      name: "dashboard",
      library: { type: "var", name: "dashboard" },
      filename: "remoteEntry.js",
      exposes: {
        './PublicModule': './projects/dashboard/src/app/public/public.module.ts',
      },
      shared: {
        "@angular/core": { singleton: true, strictVersion: true },
        "@angular/common": { singleton: true, strictVersion: true },
        "@angular/router": { singleton: true, strictVersion: true },
        "ng-zorro-antd/modal": { singleton: true, strictVersion: true },
        "ng-zorro-antd/message": { singleton: true, strictVersion: true },
        "ng-zorro-antd/notification": { singleton: true, strictVersion: true }
      }
    })
  ],
};
