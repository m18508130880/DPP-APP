var app = getApp()
var md5 = require("../../libs/md5.js");
//md5.hex_md5("");
function writeStatus(status, icon){ 
  // 弹出提示框
    wx.showToast({
      title: status,
      icon: icon
    })
}
Page({
  data: {
    userInfo: {},
    userName: "",
    password: ""
  },
  // 初始化
  onLoad: function () {
    wx.clearStorage();
    wx.clearStorageSync();
    var that = this
    //调用应用实例的方法获取全局数据
    app.getUserInfo(function(userInfo){
      //更新数据
      that.setData({
        userInfo:userInfo
      })
    })
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
  Login: function() {
    var that = this;
    var status = "";
    var icon = "";
    var userName = that.data.userName;
    var password = that.data.password;
    if (userName.length <= 0){
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
      url:'https://www.cjsci-tech.com/dpp-app/Login.do',
      //url: 'http://118.31.78.234/dpp-app/Login.do',
      data: {
        Id: userName,
        StrMd5: md5.hex_md5(userName + password),
        newDate: new Date()
      },
      header: { 'Content-Type': 'application/x-www-form-urlencoded'},
      method: 'POST',
      success: function(res){
        console.log(res.data)
        if(res.data.rst == "0000") {
          status = "成功";
          icon = "success";
          wx.navigateTo({
            //url: '../user/pieChart/pieChart'
            url: '../project/project?token=' + res.data.token + '&uId=' + res.data.manage_Role
          })
        }
      }, 
      fail: function(res) {
        console.log("fail")
        console.log(res)
        status = "失败";
        icon = "loading";
      },
      complete: function() {
        writeStatus(status, icon);
      }
    })
  }
})