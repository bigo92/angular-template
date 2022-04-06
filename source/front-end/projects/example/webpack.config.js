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
      shared: {
        "@angular/core": { singleton: true, strictVersion: true },
        "@angular/common": { singleton: true, strictVersion: true },
        "@angular/router": { singleton: true, strictVersion: true },
        "ng-zorro-antd/modal": { singleton: true, strictVersion: true },
        "ng-zorro-antd/message": { singleton: true, strictVersion: true },
        "ng-zorro-antd/notification": { singleton: true, strictVersion: true },
        "ng-zorro-antd/icon": { singleton: true, strictVersion: true },
        "@ant-design/icons-angular": { singleton: true, strictVersion: true },
      }
    })
  ],
};
