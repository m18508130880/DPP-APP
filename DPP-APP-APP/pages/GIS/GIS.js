var app = getApp()
var template = require('../../utils/customTabBar/customTabBar.js');
var timing = null;
var gjArray;
var wjIcon = "/image/map_wj_middle.gif";
var yjIcon = "/image/map_yj_middle.gif";
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
    this.getTopo();
    this.getDataNow();
    //this.count_down();
  },
  // 获取项目数据
  getProjectInfo: function () {
    var that = this;
    var userInfo = wx.getStorageSync("userInfo");
    if (userInfo == null || userInfo == "") {
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
    if (project == null || project == "") {
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
      },
      scale: project.mapLev
    })
  },
  // 展示数据
  showTopo: function () {
    console.log("showTopo");
    var gjTopo = wx.getStorageSync("gjTopo");
    var gxTopo = wx.getStorageSync("gxTopo");
    console.log(gjTopo)
    console.log(gxTopo)
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
  getTopo: function () {
    console.log("getTopo")
    var that = this;
    var userInfo = wx.getStorageSync("userInfo");
    if (userInfo == null) {
      wx.showToast({
        title: '您必须先登录',
        icon: 'loading',
        duration: 2000
      })
      wx.reLaunch({
        url: '../user/user'
      })
    }
    var project = wx.getStorageSync("project");
    //console.log(project)
    if (project == null) {
      wx.showToast({
        title: '您必须先选择一个项目',
        icon: 'loading',
        duration: 2000
      })
      wx.reLaunch({
        url: '../user/user'
      })
    }
    gjArray = [];
    wx.request({
      url: 'https://cj.cjsci-tech.com/dpp-app/ToPo_GJ.do',
      //url: 'http://118.31.78.234/dpp-app/ToPo_GJ.do',
      data: {
        Cmd: "0",
        Token: userInfo.token,
        Project_Id: project.id
      },
      method: 'GET',
      success: function (res) {
        if (res.data.rst == "0000") {
          var gjList = res.data.cData;
          var gjTopo = new Array();
          var color = "";
          var icon = "";
          for (var i = 0; i < gjList.length; i++) {
            var obj = gjList[i];
            if (obj.id.indexOf("W") >= 0) {
              color = "#0000FF";
              icon = wjIcon;
            } else {
              color = "#FF0000";
              icon = yjIcon;
            }
            gjTopo.push({
              longitude: obj.wX_Lng,
              latitude: obj.wX_Lat,
              iconPath: icon,
              width: "6px",
              height: "6px",
              anchor: {x:0.5, y:0.5}
              //fillColor: color,
              //color: color,
              //radius: 2,
            })
            var gjObj = new Object();
            gjObj.tId = obj.id;
            gjObj.tLng = obj.wX_Lat;
            gjObj.tLat = obj.wX_Lng;
            gjArray.push(gjObj);
          }
          that.setData({
            //circles: gjTopo
            markers: gjTopo
          })
          //wx.setStorageSync("gjTopo", gjTopo);
          wx.request({
            url: 'https://cj.cjsci-tech.com/dpp-app/ToPo_GX.do',
            //url: 'http://118.31.78.234/dpp-app/ToPo_GX.do',
            data: {
              Cmd: "0",
              Token: userInfo.token,
              Project_Id: project.id
            },
            method: 'GET',
            success: function (res) {
              if (res.data.rst == "0000") {
                var gxList = res.data.cData;
                var gxTopo = new Array();
                var color = ""; var a = "";
                var count = 0;
                for (var i = 0; i < gxList.length; i++) {
                  var gjStartId = gxList[i].start_Id;
                  var gjEndId = gxList[i].end_Id;
                  var gjStart = that.gjGet(gjStartId);
                  var gjEnd = that.gjGet(gjEndId);
                  if (null == gjStart || null == gjEnd) { continue; }
                  if (gxList[i].id.indexOf("WG") > -1) {
                    color = "#FF0000DD";
                  } else {
                    color = "#0000FFDD";
                  }
                  if (gxList[i].id.indexOf("WG99") > -1 || gxList[i].id.indexOf("YG99") > -1) { continue; }
                  gxTopo[count] = {
                    points: [{
                      latitude: Number(gjStart.tLng),
                      longitude: Number(gjStart.tLat)
                    }, {
                      latitude: Number(gjEnd.tLng),
                      longitude: Number(gjEnd.tLat)
                    }],
                    color: color,
                    width: 1,
                    id: gxList[i].id
                  }
                  count++;
                }
                that.setData({
                  polyline: gxTopo
                })
                //wx.setStorageSync("gxTopo", gxTopo)
              }
            }
          })
        }
      }
    })

  },
  getDataNow: function () {
    console.log("getDataNow");
    var that = this;
    var userInfo = wx.getStorageSync("userInfo");
    if (userInfo == null || userInfo == ""){
      return;
    }
    var project = wx.getStorageSync("project");
    if (project == null || project == ""){
      return;
    }
    wx.request({
      url: 'https://cj.cjsci-tech.com/dpp-app/Real_Water_Lev.do',
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
  gjGet: function (objValue) {
    var that = this;
    //console.log(gjArray)
    for (var i = 0; i < gjArray.length; i++) {
      if ((gjArray[i].tId) == objValue) {
        return gjArray[i];
      }
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