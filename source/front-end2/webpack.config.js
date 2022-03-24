const { ModuleFederationPlugin } = require('webpack').container;

module.exports = {
  output: {
    publicPath: "http://localhost:4200/",
    uniqueName: "front-end"
  },
  optimization: {
    // Only needed to bypass a temporary bug
    runtimeChunk: false
  },
  plugins: [
    new ModuleFederationPlugin({
      remotes: {
        'dashboard': "dashboard@http://localhost:4201/remoteEntry.js",
        'sso': "sso@http://localhost:4202/remoteEntry.js"
      },
      shared: ["@angular/core", "@angular/common", "@angular/router"]
    })
  ],
};
