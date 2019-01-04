var app = getApp();
var imgPath = app.config.imgPath;
var template = require('../../utils/customTabBar/customTabBar.js');
function writeStatus(status, icon){ 
  // 弹出提示框
    wx.showToast({
      title: status,
      icon: icon
    })
};
Page({
  data: {
    topImgUrl: [
        imgPath+'top_1.png',
        imgPath +'top_2.png'
      ],
    news: [],
    use: []
  },
  // 初始化
  onLoad: function () {
    template.tabbar("tabBar", 0, this)//0表示第一个tabbar
    app.Login();
    var userInfo = wx.getStorageSync('userInfo');
    this.getNewsList();
    this.getUseList();
  },
  // 生命周期监听
  onShow: function () {
    this.getNewsList();
    this.getUseList();
  },
  // 获取新闻信息
  getNewsList: function () {
    var that = this;
    wx.request({
      url: 'https://www.cjscitech.cn/dpp-app/getNewsList.do',
      //url: 'http://118.31.78.234/dpp-app/Login.do',
      data: {
        Cmd: 0,
        newDate: new Date()
      },
      header: { 'Content-Type': 'application/x-www-form-urlencoded' },
      method: 'POST',
      success: function (res) {
        if (res.data.rst == "0000") {
          var obj = res.data.cData;
          that.setData({
            news: obj
          })
        }
      }
    })
  },
  getUseList: function () {
    var that = this;
    wx.request({
      url: 'https://www.cjscitech.cn/dpp-app/getUseList.do',
      //url: 'http://118.31.78.234/dpp-app/Login.do',
      data: {
        Cmd: 0,
        newDate: new Date()
      },
      header: { 'Content-Type': 'application/x-www-form-urlencoded' },
      method: 'POST',
      success: function (res) {
        if (res.data.rst == "0000") {
          var obj = res.data.cData;
          that.setData({
            use: obj
          })
        }
      }
    })
  },
  gotoNews: function (e) {
    var sn = e.target.id;
    console.log(sn)
    wx.navigateTo({
      //url: 'http://www.cjsci-tech.com/bd/shownews.php?id=31'
      url: 'news/news?sn='+sn,
    })

  },
  gotoUse: function (e) {
    var sn = e.target.id;
    console.log(sn)
    wx.navigateTo({
      //url: 'http://www.cjsci-tech.com/bd/shownews.php?id=31'
      url: 'use/use?sn=' + sn,
    })

  }
})