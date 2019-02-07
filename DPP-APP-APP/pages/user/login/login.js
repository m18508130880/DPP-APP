var app = getApp()
var md5 = require("../../../libs/md5.js");
Page({
  data: {
    userInfo: {},
    userName: "",
    password: ""
  },
  // 初始化
  onLoad: function () {
    var that = this;
    //that.Login();
  },
  userName: function (e) {
    this.setData({
      userName: e.detail.value
    })
  },
  password: function (e) {
    this.setData({
      password: e.detail.value
    })
  },
  // 登录处理函数
  Login: function () {
    var that = this;
    var status = "";
    var icon = "";
    var userName = that.data.userName;
    var password = that.data.password;
    wx.setStorageSync('userName', userName);
    wx.setStorageSync('password', password);
    if (userName.length <= 0) {
      status = "请输入用户名";
      icon = "loading";
      writeStatus(status, icon);
      return;
    }
    if (password.length <= 0) {
      status = "请输入密码";
      icon = "loading";
      writeStatus(status, icon);
      return;
    }
    wx.request({
      //url: 'https://www.cjscitech.cn/dpp-app/Login.do',
      //url: 'http://118.31.78.234/dpp-app/Login.do',
      url: 'https://cj.cjsci-tech.com/dpp-app/Login.do',
      data: {
        Id: userName,
        StrMd5: md5.hex_md5(userName + password),
        newDate: new Date()
      },
      header: { 'Content-Type': 'application/x-www-form-urlencoded' },
      method: 'POST',
      success: function (res) {
        console.log(res.data)
        if (res.data.rst == "0000") {
          wx.setStorageSync('userInfo', res.data);
          //wx.switchTab({
          wx.reLaunch({
            url: '../../user/user'
          })
        }else {
          wx.showToast({
            title: '账号或密码错误',
            icon: 'loading',
            duration: 2000
          })
          return;
        }
      }
    })
  }
})