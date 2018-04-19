import * as echarts from '../../../ec-canvas/echarts';
var app = getApp()
var token = "";
var iconWJ = "/image/map_wj_middle.gif";
var iconYJ = "/image/map_yj_middle.gif";
var gjArray;
var isOK = false;
var d = new Date();
var today = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
function writeStatus(status, icon) {
  // 弹出提示框
  wx.showToast({
    title: status,
    icon: icon
  })
}
Page({
  data: {
    select: true,
    project: [],
    markers: [],
    polyline: [],
    graphCut: "none",
    gjArray: [],
    realWaterLev: []
  },
  onLoad: function (str) {
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
    gjArray = [];
    wx.request({
      url: 'http://118.31.78.234/dpp-app/ToPo_GJ.do',
      data: {
        Cmd: "0",
        Token: token,
        Project_Id: project_Id
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data)
        if (res.data.rst == "0000") {
          var gjList = res.data.cData;
          var markerArray = new Array();
          var color = "";
          for (var i = 0; i < gjList.length; i++) {
            if (gjList[i].id.indexOf("WJ") > -1) {
              color = iconWJ;
            } else {
              color = iconYJ;
            }
            markerArray.push( {
              id: gjList[i].id,
              latitude: gjList[i].wX_Lat,
              longitude: gjList[i].wX_Lng,
              iconPath: color
            })
            var gjObj = new Object();
            gjObj.tId = gjList[i].id;
            gjObj.tLng = gjList[i].wX_Lat;
            gjObj.tLat = gjList[i].wX_Lng;
            gjArray.push(gjObj);
            if(gjList[i].equip_Id.length == 20) {
              var WL = gjList[i].top_Height * 1 - gjList[i].equip_Height * 1 + gjList[i].curr_Data * 1
              markerArray.push({
                id: gjList[i].id,
                latitude: gjList[i].wX_Lat,
                longitude: gjList[i].wX_Lng,
                label: {
                  content: WL.toFixed(3),
                  x: -20,
                  bgColor: "#FFFF00",
                  fontSize: 16
                }
              })
            }
          }
          wx.request({
            url: 'http://118.31.78.234/dpp-app/ToPo_GX.do',
            data: {
              Cmd: "0",
              Token: token,
              Project_Id: project_Id
            },
            method: 'GET',
            success: function (res) {
              console.log(res.data)
              if (res.data.rst == "0000") {
                var gxList = res.data.cData;
                var polylineArray = new Array();
                var color = "";var a = "";
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
                  polylineArray[count] = {
                    points: [{
                      latitude: Number(gjStart.tLng),
                      longitude: Number(gjStart.tLat)
                    }, {
                      latitude: Number(gjEnd.tLng),
                      longitude: Number(gjEnd.tLat)
                    }],
                    color: color,
                    width: 2,
                    id: gxList[i].id
                  }
                  count ++;
                }
                console.log(a)
                console.log(polylineArray)
                that.setData({
                  polyline: polylineArray
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
          that.setData({
            markers: markerArray
          })
          console.log(that.data)
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
  gjGet: function (objValue) {
    var that = this;
    //console.log(gjArray)
    for (var i = 0; i < gjArray.length; i ++) {
      if ((gjArray[i].tId) == objValue) {
        return gjArray[i];
      }
    }
  },
  graphCutNo: function () {
    var that = this;
    that.setData({
      graphCut: "none"
    })
  },
  markertap: function (e) {
    console.log(e)
    var gjId = e.markerId;
    console.log(gjId)
    var that = this;
    wx.request({
      url: 'http://118.31.78.234/dpp-app/GJ_Info.do',
      data: {
        Cmd: "2",
        Project_Id: that.data.project.project_id,
        Id: gjId,
        Token: token
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data)
        if (res.data.rst == "0000") {
          var gj = res.data.cData;
          var baseWater = gj[0].top_Height - gj[0].equip_Height;
          console.log(baseWater + "=" + gj[0].top_Height + "-" + gj[0].equip_Height)
          wx.request({
            url: 'http://118.31.78.234/dpp-app/Real_Water_Lev.do',
            data: {
              Cmd: "0",
              Project_Id: that.data.project.project_id,
              GJ_Id: gjId,
              BDate: today,
              EDate: today,
              Token: token
            },
            method: 'GET',
            success: function (res) {
              console.log(res.data)
              if (res.data.rst == "0000") {
                var dataList = res.data.cData;
                var gjList = new Array();
                for (var i = 0; i < dataList.length; i ++) {
                  var value = baseWater + dataList[i].value * 1;
                  var gjObj = {
                    id: dataList[i].gJ_Id,
                    value: value.toFixed(3),
                    ctime: dataList[i].cTime,
                    maxCTime: dataList[i].max_CTime,
                    minCTime: dataList[i].min_CTime,
                  }
                  gjList.push(gjObj);
                }
                that.ecComponent.init((canvas) => {
                  // 获取组件的 canvas、width、height 后的回调函数
                  // 在这里初始化图表
                  const chart = echarts.init(canvas, null, {
                    width: 600,
                    height: 300
                  });
                  that.setData({
                    graphCut: ""
                  })
                  setOption(chart, gjList, gj[0]);
                  // 将图表实例绑定到 this 上，可以在其他成员函数（如 dispose）中访问
                  that.chart = chart;
                  // 注意这里一定要返回 chart 实例，否则会影响事件处理等
                  return chart;
                });
              }
              else if (res.data.rst = "1005") {
                wx.reLaunch({
                  url: "../../index/index"
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
        }
        else if (res.data.rst = "1005") {
          wx.reLaunch({
            url: "../../index/index"
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
  realWL: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../RealWL/RealWL?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  gjPos: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GJPos/GJPos?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
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
    // 获取组件
    this.ecComponent = this.selectComponent('#mychart-dom-bar');
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

function setOption(chart, gjList, gj) {
  var lineA = new Array();
  var xAx = 0.0;
  for (var i = 0; i < gjList.length; i ++) {
    xAx = (gjList[i].ctime).substring(11,13);
    lineA.push([xAx, gjList[i].value]);
  }
  console.log(lineA)
  const option = {
    xAxis: {
      type: "value",
      maxInterval: 1
    },
    yAxis: {
      splitLine: { show: false },
      min: gj.base_Height,
      max: gj.top_Height
    },
    animationDurationUpdate: 1200,
    series: [{
      type: 'line',
      lineStyle: { color: "#000000" },
      itemStyle: { color: "#000000" },
      symbol: 'circle',
      label: {
        show: true,
      },
      data: lineA
    }]
  }
  chart.setOption(option);
}