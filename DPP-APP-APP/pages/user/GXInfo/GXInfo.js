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
    gxName: [],
    gxIndex: 0,
    gxSum: 0,
    gxAll: [],
    gxList: [],
    project: [],
    select: true,
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
    wx.request({
      url: 'https://www.cjsci-tech.com/dpp-app/GX_Info.do',
      //url: 'http://118.31.78.234/dpp-app/GX_Info.do',
      data: {
        Cmd: "0",
        Project_Id: project_Id,
        Token: token
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data)
        if (res.data.rst == "0000") {
          var dataObj = res.data.cData;
          //console.log(dataObj);
          var gxNames = new Array();
          var gxLists = new Array();
          var names = "";
          gxNames[0] = "全部系统";
          var m = 1;
          for (var i = 0; i < dataObj.length; i++) {
            var name = dataObj[i].id.substr(0, 5);
            if (names.indexOf(name) < 0 && (name.substr(0, 3) == "YG0" || name.substr(0, 3) == "WG0")) {
              names += name;
              gxNames[m] = name;
              m++;
            }
            var slope = (dataObj[i].start_Height - dataObj[i].end_Height) / dataObj[i].length;
            if (dataObj[i].length <= 0) { slope = 0 }
            gxLists[i] = {
              id: dataObj[i].id,
              sub_Id: dataObj[i].id.substr(5),
              road: dataObj[i].road,
              end_Height: dataObj[i].end_Height,
              start_Height: dataObj[i].start_Height,
              diameter: dataObj[i].diameter,
              length: dataObj[i].length,
              slope: Math.round(parseFloat(slope) * 10000) / 10,
              material: dataObj[i].material
            }
          }
          console.log(gxLists)
          that.setData({
            gxName: gxNames,
            gxAll: gxLists,
            gxList: gxLists,
            gxSum: dataObj.length
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
  alert: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GIS/GIS?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  gjPos: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GIS/GIS?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  changeP: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../../project/project?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  
  bindCasPickerChange: function (e) {
    var that = this;
    var gxN = that.data.gxName[e.detail.value];
    var gxA = that.data.gxAll;
    var count = 0;
    var gxL = [];
    for (var i = 0; i < gxA.length; i++) {
      if (gxA[i].id.indexOf(gxN) > -1) {
        gxL[count] = gxA[i];
        count++;
      }
    }
    this.setData({
      gxIndex: e.detail.value,
      gxList: gxL,
      gxSum: count
    })
    if (count == 0) {
      this.setData({
        gxIndex: e.detail.value,
        gxList: gxA,
        gxSum: gxA.length
      })
    }
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
