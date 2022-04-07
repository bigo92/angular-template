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
        "@angular/core": { eager: true, singleton: true, requiredVersion: false },
        "@angular/common": { eager: true, singleton: true, requiredVersion: false },
        "@angular/router": { eager: true, singleton: true, requiredVersion: false },
        "@angular/forms": { eager: true, singleton: true, requiredVersion: false },
        "ng-zorro-antd/tabs": { eager: true, singleton: true, requiredVersion: false },
        "ng-zorro-antd/modal": { eager: true, singleton: true, requiredVersion: false },
        "ng-zorro-antd/message": { eager: true, singleton: true, requiredVersion: false },
        "ng-zorro-antd/notification": { eager: true, singleton: true, requiredVersion: false },
        "ng-zorro-antd/icon": { eager: true, singleton: true, requiredVersion: false },
        "ng-zorro-antd/i18n": { eager: true, singleton: true, requiredVersion: false },
        "ng-zorro-antd/table": { eager: true, singleton: true, requiredVersion: false },
        "@ant-design/icons-angular": { eager: true, singleton: true, requiredVersion: false },
        "ng-zorro-antd/form" : { eager: true, singleton: true, requiredVersion: false },
      }
    })
  ],
};
