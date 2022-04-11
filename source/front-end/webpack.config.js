const { ModuleFederationPlugin } = require('webpack').container;

module.exports = {
  output: {
    publicPath: "http://localhost:4200/",
    uniqueName: "shell"
  },
  optimization: {
    // Only needed to bypass a temporary bug
    runtimeChunk: false
  },
  plugins: [
    new ModuleFederationPlugin({
      remotes: {
        'dashboard': "dashboard@http://localhost:4201/remoteEntry.js",
        'sso': "sso@http://localhost:4202/remoteEntry.js",
        'example': "example@http://localhost:4203/remoteEntry.js",
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
