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
    userInfo: {}
  },
  // 初始化
  onLoad: function () {
    var that = this
    //调用应用实例的方法获取全局数据
    app.getUserInfo(function(userInfo){
      //更新数据
      that.setData({
        userInfo:userInfo
      })
    })
  },
  // 登录处理函数
  Login: function() {
    var that = this;
    var status = "";
    var icon = "";
    wx.request({
      url: 'http://118.31.78.234/dpp-app/Login.do',
      data: {
        Id: "user",
        StrMd5: md5.hex_md5("user111111"),
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
        console.log(res.data)
        status = "失败";
        icon = "loading";
      },
      complete: function() {
        writeStatus(status, icon);
      }
    })
  }
})