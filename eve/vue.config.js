const path = require('path');//引入path模块

function resolve(dir){
  return path.join(__dirname,dir)//path.join(__dirname)设置绝对路径
}

module.exports={
  publicPath: process.env.NODE_ENV === 'production' ? '././' : '/',
  outputDir: 'dist',
  assetsDir: 'static',
  chainWebpack:(config)=>{
    config.resolve.alias
      //set第一个参数：设置的别名，第二个参数：设置的路径
      .set('@',resolve('./src'))
      .set('components',resolve('./src/components'))
      .set('assets',resolve('./src/assets'))
      .set('pages',resolve('./src/pages'))
      .set('img', resolve('public/img'))
    //注意 store 和 router 没必要配置
  },
}
