var app = getApp();
var template = require('../../utils/customTabBar/customTabBar.js');
var gjArray;
Page({
  data: {
    projects: [{cName:"选择项目(请登录后操作)"}],
    index:0,
    userInfo: {cName:'登  录',phont:'_'}
  },
  onLoad: function () {
    template.tabbar("tabBar", 3, this)//0表示第一个tabbar
    this.init();
  },
  init: function () {
    var that = this;
    var userInfo = wx.getStorageSync('userInfo');
    console.log(userInfo)
    if (userInfo != null && userInfo.length > 0) {
      var user = {
        cName: userInfo.cName,
        phont:''
      }
      that.setData({
        userInfo: user
      })
      var project = wx.getStorageSync("project");
      wx.request({
        url: 'https://www.cjscitech.cn/dpp-app/Manage_Role.do',
        //url: 'http://118.31.78.234/dpp-app/Manage_Role.do',
        data: {
          Cmd: "0",
          Id: userInfo.manage_Role,
          Token: userInfo.token
        },
        method: 'GET',
        success: function (res) {
          //console.log(res.data)
          if (res.data.rst == "0000") {
            var dataObj = res.data.cData;
            var projects = new Array();
            var index = 0;
            for (var i = 0; i < dataObj.length; i++) {
              projects.push(dataObj[i]);
              if (project != null){
                if (project.id == dataObj[i].id){
                  index = i;
                }
              }
            }
            that.setData({
              projects: projects,
              index: index
            })
          }
          else if (res.data.rst == "1005") {
            wx.showToast({
              title: '您还没有登录',
              icon: 'loading',
              duration: 2000
            })
          }
        }
      })
    }
  },
  // 头像页面
  gotoLogin: function (e) {
    var userInfo = wx.getStorageSync('userInfo');
    var url = "";
    if (userInfo != null && userInfo.length > 0){
      url = "userInfo/userInfo";
    }else{
      url = "login/login";
    }
    wx.navigateTo({
      url: url
    })
  },
  // 选择项目
  bindPickerChange(e) {
    var index =  e.detail.value;
    this.setData({
      index: index
    })
    var projects = this.data.projects;
    var project = projects[index];
    wx.setStorageSync("project", project);
    this.getTopo();
  },
  // 切换用户
  changeUser: function () {
    wx.navigateTo({
      url: "login/login"
    })
  },
  // 问题反馈
  gotoFeed: function () {
    var userInfo = wx.getStorageSync("userInfo");
    if(userInfo != null && userInfo.length > 0){
      wx.navigateTo({
        url: "feed/feed"
      })
    }else {
      wx.showToast({
        title: '您还没有登录',
        icon: 'loading',
        duration: 2000
      })
    }
  },
  // 技术力量
  gotoTeam: function () {
    wx.navigateTo({
      url: "team/team"
    })
  },
  // 关于
  gotoAbout: function () {
    wx.navigateTo({
      url: "about/about"
    })
  },
  // 切换项目后加载地图数据
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
    //console.log(userInfo)
    var project = wx.getStorageSync("project");
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
      url: 'https://www.cjscitech.cn/dpp-app/ToPo_GJ.do',
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
          for(var i = 0; i < gjList.length; i ++){
            var obj = gjList[i];
            if (obj.id.indexOf("W")) {
              color = "#0000FF";
            } else {
              color = "#FF0000";
            }
            gjTopo.push({
              longitude: obj.wX_Lng,
              latitude: obj.wX_Lat,
              fillColor: color,
              radius: 3,
              strokeWidth: 0
            })
            var gjObj = new Object();
            gjObj.tId = obj.id;
            gjObj.tLng = obj.wX_Lat;
            gjObj.tLat = obj.wX_Lng;
            gjArray.push(gjObj);
          }
          wx.setStorageSync("gjTopo", gjTopo);
          wx.request({
            url: 'https://www.cjscitech.cn/dpp-app/ToPo_GX.do',
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
                wx.setStorageSync("gxTopo", gxTopo)
              }
            }
          })
        }
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
})
