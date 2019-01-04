var app = getApp()
var template = require('../../utils/customTabBar/customTabBar.js');
var timing = null;

Page({
  data: {
    select: true,
    project: [],
    circles: [],
    markers: [],
    polyline: [],
    graphCut: "none",
    realWaterLev: [],
    alert: [],
    scale: 17,
    center:[]
  },
  onLoad:function () {
    template.tabbar("tabBar", 1, this)//0表示第一个tabbar
    this.getProjectInfo();
    this.showTopo();
    this.getDataNow();
    //this.count_down();
  },
  // 展示数据
  showTopo: function () {
    console.log("showTopo");
    var gjTopo = wx.getStorageSync("gjTopo");
    var gxTopo = wx.getStorageSync("gxTopo");
    if (gjTopo != null && gjTopo.length > 0 && gxTopo != null && gxTopo.length > 0 ){
      this.setData({
        circles: gjTopo,
        polyline: gxTopo
      })
    }else {
      wx.showToast({
        title: '系统开小差了',
        icon: 'loading',
        duration: 2000
      })
      wx.reLaunch({
        url: '../user/user',
      })
    }
  },
  // 获取项目数据
  getProjectInfo: function () {
    var that = this;
    var userInfo = wx.getStorageSync("userInfo");
    if (!(userInfo != null && userInfo.length > 0)) {
      wx.reLaunch({
        url: '../user/user'
      })
      wx.showToast({
        title: '您必须先登录',
        icon: 'loading',
        duration: 2000
      })
      return;
    }
    var project = wx.getStorageSync("project");
    if (!(project != null && project.length > 0)) {
      wx.reLaunch({
        url: '../user/user'
      })
      wx.showToast({
        title: '您必须先选择一个项目',
        icon: 'loading',
        duration: 2000
      })
      return;
    }
    that.setData({
      project: {
        project_id: project.id,
        lng: project.wX_Lng,
        lat: project.wX_Lat
      },
      center: {
        lng: project.wX_Lng,
        lat: project.wX_Lat
      }
    })
  },
  getDataNow: function () {
    console.log("getDataNow");
    var that = this;
    var userInfo = wx.getStorageSync("userInfo");
    if(userInfo == null){
      return;
    }
    var project = wx.getStorageSync("project");
    if (project == null){
      return;
    }
    wx.request({
      url: 'https://www.cjscitech.cn/dpp-app/Alert_Info.do',
      //url: 'http://118.31.78.234/dpp-app/Alert_Info.do',
      data: {
        Cmd: "0",
        Token: userInfo.token,
        Project_Id: project.id
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data);
        if (res.data.rst == "0000") {
          var dataObj = res.data.cData;
          console.log(dataObj)
        }
        else if (res.data.rst == "1005") {
          wx.reLaunch({
            url: '../../index/index'
          })
        }
      },
      fail: function (res) {
        console.log(res)
      },
      complete: function () {
      }
    })
  },
  count_down: function (e) {
    var that = this;
    var my_time = new Date().getTime();
    console.log(my_time)
    wx.setStorageSync('my_time', my_time)
    if (!wx.getStorageSync('ok_time')) {
      wx.setStorageSync('ok_time', my_time + 10 * 60 * 1000)
      //this.show_img()
    } else {
      if (wx.getStorageSync('my_time') >= wx.getStorageSync('ok_time')) {
        //this.show_img()
        wx.removeStorageSync('ok_time')
      }
    }
    timing = setTimeout(function () {
      that.count_down()
    }, 60*1000)
  },
  //点击缩放按钮 
  zoom: function (e) {
    var that = this;
    var scale = that.data.scale;
    if (e.target.id === "small") {
      --scale;
    } else {
      ++scale;
    }
    if (scale > 20 || scale < 6){return}
    that.setData({
      scale: scale
    })
  }, 
  // 定位自己位置
  positioning(e) {
    var that = this;
    wx.getLocation({
      type: 'gcj02', //返回可以用于wx.openLocation的经纬度  
      success: function (res) {
        var latitude = res.latitude
        var longitude = res.longitude
        that.setData({
          center: {
            lng: longitude,//经度   
            lat: latitude,//纬度   
          }
        })
      }
    })
  },
  // 回到项目中心
  project: function () {
    var that = this;
    that.setData({
      center: {
        lng: that.data.project.lng,//经度   
        lat: that.data.project.lat,//纬度   
      }
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
    // 获取组件
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