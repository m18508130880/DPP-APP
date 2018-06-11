// pages/user/data/data.js
var app = getApp()
var token = "";
var isOK = false;
function writeStatus(status, icon) {
  // 弹出提示框
  wx.showToast({
    title: status,
    icon: icon
  })
}
Page({
  data: {
    alert: [],
    select: true,
    project: [],
    BDate: '2017-05-05',
    EDate: '2017-05-05'
  },
  onLoad: function (str) {  //初始化
    var that = this;
    var project_Id = str.project_id;
    that.setData({
      project: {
        uId: str.uId,
        project_id: project_Id,
        lng: str.lng,
        lat: str.lat
      }
    })
    token = str.token;
    console.log(token)
    wx.request({
      url: 'https://www.cjsci-tech.com/dpp-app/Alert_Info.do',
      //url: 'http://118.31.78.234/dpp-app/Alert_Info.do',
      data: {
        Cmd: "0",
        Token: token,
        Project_Id: project_Id
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data);
        if (res.data.rst == "0000") {
          var dataObj = res.data.cData;
          var alerts = new Array();
          for (var i = 0; i < dataObj.length; i++) {
            alerts[i] = {
              attr_Name: dataObj[i].attr_Name,
              cTime: dataObj[i].cTime,
              curr_Data: dataObj[i].curr_Data,
              des: dataObj[i].des,
              gJ_Id: dataObj[i].gJ_Id,
            }
          }
          console.log(alerts)
          that.setData({
            alert: alerts
          })
        }
        else if (res.data.rst == "1005") {
          wx.reLaunch({
            url: '../../index/index'
          })
          writeStatus("操作超时", "loading");
        }
      },
      fail: function (res) {
        console.log("fail")
        console.log(res)
      },
      complete: function () {
      }
    })
  },
  changeF: function (e) {
    var that = this;
    console.log(e)
    if (isOK) {
      that.setData({
        select: isOK
      })
      isOK = !isOK;
    } else {
      that.setData({
        select: isOK
      })
      isOK = !isOK;
    }
  },
  gis: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GIS/GIS?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  gjInfo: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GJInfo/GJInfo?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  gxInfo: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GXInfo/GXInfo?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  alert: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../Alert/Alert?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  changeP: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../../project/project?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },



  /**
   * 勿删
   * 时间选择器
   */
  BDateChange: function (e) { //时间选择器
    this.setData({
      BDate: e.detail.value
    })
  },
  EDateChange: function (e) { //时间选择器
    this.setData({
      EDate: e.detail.value
    })
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  }
})
