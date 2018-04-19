
import * as echarts from '../../../ec-canvas/echarts';
var app = getApp()
var token = "";
var isOK = false;
var project_Id = "";
function writeStatus(status, icon) {
  // 弹出提示框
  wx.showToast({
    title: status,
    icon: icon
  })
}
Page({
  data: {
    gjName: [],
    gjIndex: 0,
    gjSum: 0,
    gjAll: [],
    gjList: [],
    gjIdList: [],
    startId: "",
    endtId: "",
    project: [],
    select: true, 
    graphCut: "none",
    graphWidth: 0, 
    ec: {
      // 将 lazyLoad 设为 true 后，需要手动初始化图表
      lazyLoad: true
    },
    BDate: '2017-05-05',
    EDate: '2017-05-05'
  },
  onLoad: function (str) {  //初始化
    var that = this;
    project_Id = str.project_id;
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
      url: 'http://118.31.78.234/dpp-app/GJ_Info.do',
      data: {
        Cmd: "0",
        Project_Id: project_Id,
        Token: token
      },
      method: 'GET',
      success: function (res) {
        if (res.data.rst == "0000") {
          var dataObj = res.data.cData;
          //console.log(dataObj)
          var gjNames = new Array();
          var gjLists = new Array();
          var names = "";
          gjNames[0] = "全部系统";
          var m = 1;
          for (var i = 0; i < dataObj.length; i++) {
            var name = dataObj[i].id.substr(0, 5);
            if (names.indexOf(name) < 0 && (name.substr(0, 3) == "YJ0" || name.substr(0, 3) == "WJ0")) {
              names += name;
              gjNames[m] = name;
              m ++;
            }
            gjLists[i] = {
              id: dataObj[i].id,
              sub_Id: dataObj[i].id.substr(5),
              road: dataObj[i].road,
              top_Height: dataObj[i].top_Height,
              base_Height: dataObj[i].base_Height,
              size: dataObj[i].size,
              material: dataObj[i].material
            }
          }
          console.log(gjLists)
          that.setData({
            gjName: gjNames,
            gjAll: gjLists,
            gjList: gjLists,
            gjSum: dataObj.length
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
  graphCut: function (id) {
    var that = this;
    console.log(id)
    var gjObj;
    wx.request({
      url: 'http://118.31.78.234/dpp-app/GJ_Info.do',
      data: {
        Cmd: "1",
        Project_Id: project_Id,
        Subsys_Id: id,
        Token: token
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data)
        if (res.data.rst == "0000") {
          gjObj = res.data.cData;
          if (gjObj != null) {
            wx.request({
              url: 'http://118.31.78.234/dpp-app/GX_Info.do',
              data: {
                Cmd: "1",
                Project_Id: project_Id,
                Subsys_Id: id,
                Token: token
              },
              method: 'GET',
              success: function (res) {
                if (res.data.rst = "0000") {
                  var gxObj = res.data.cData;
                  that.doGraphCut(gjObj, gxObj)
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
        }
        else if (res.data.rst == "1005") {
          wx.reLaunch({
            url: '../../index/index'
          })
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
  gxInfo: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GIS/GIS?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
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
    var gjN = that.data.gjName[e.detail.value];
    var gjA = that.data.gjAll;
    var count = 0;
    var gjL = [];
    var gjI = [];
    gjI[count] = "请选择开始管井号";
    for (var i = 0; i < gjA.length; i ++) {
      if (gjA[i].id.indexOf(gjN) > -1) {
        gjL[count] = gjA[i];
        gjI[count+1] = gjA[i].id;
        count ++;
      }
    }
    this.setData({
      gjIndex: e.detail.value,
      gjList: gjL,
      gjSum: count,
      gjIdList: gjI
    })
    if (count == 0) {
      this.setData({
        gjIndex: e.detail.value,
        gjList: gjA,
        gjSum: gjA.length,
        gjIdList: gjI
      })
    }
  },
  bindCasPickerChange1: function (e) {
    var that = this;
    if (that.data.gjName[e.detail.value] == "全部系统") {return;}
    console.log(e.detail.value)
    var id = that.data.gjIdList[e.detail.value];
    console.log(id);
    that.graphCut(id)
  },
  graphCutNo: function () {
    var that = this;
    that.setData({
      graphCut: "none"
    })
  },
//剖面图
  doGraphCut: function (gjObj, gxObj) {
    var that = this;
    var isOut = false;
    var width = 0;
    for (var i = 0; i < gjObj.length; i++) {
      if (gjObj[i].flag == 6) {
        isOut = true;
      }
    }
    var gjList = new Array();
    var gxList = new Array();
    for (var i = 0; i < gjObj.length; i ++) {
      gjList.push(gjObj[i]);
      if ((gjObj[i].flag == 2 && isOut == false) || gjObj[i].flag == 6) {
        break;
      }
      for (var j = 0; j < gxObj.length; j ++) {
        if (gjObj[i].out_Id == gxObj[j].id) {
          gxList.push(gxObj[j]);
          width += parseFloat(gxObj[j].length);
        }
      }
    }
    width = parseInt(width)*5;
    if (width < 400) { width = 400; }
    that.setData ({
      graphWidth: width
    })
    console.log(width)
    console.log(gjList)
    console.log(gxList)
    that.ecComponent.init((canvas) => {
      // 获取组件的 canvas、width、height 后的回调函数
      // 在这里初始化图表
      const chart = echarts.init(canvas, null, {
        width: width,
        height: 300
      });
      setOption(chart, gjList, gxList);
      that.setData({
        graphCut: "",
        startId: gjList[0].id.substr(5, 8),
        endId: gjList[gjList.length-1].id.substr(5, 8)

      })
      // 将图表实例绑定到 this 上，可以在其他成员函数（如 dispose）中访问
      that.chart = chart;

      // 注意这里一定要返回 chart 实例，否则会影响事件处理等
      return chart;
    });
  },
  

  /**
   * 勿删
   * 时间选择器
   */
  BDateChange: function(e) { //时间选择器
    this.setData({
      BDate: e.detail.value
    })
  },
  EDateChange: function(e) { //时间选择器
    this.setData({
      EDate: e.detail.value
    })
  },
  onReady:function(){
    // 页面渲染完成
    // 获取组件
    this.ecComponent = this.selectComponent('#mychart-dom-bar');
  },
  onShow:function(){
    // 页面显示
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  }
})


function setOption(chart, gjList, gxList) {
  var xAxArray = new Array();
  var xAxName = new Array();
  var dataLineA = new Array();    // 井顶部-路面				虚线
  var dataLineB = new Array();	  // 管线顶部					实线
  var dataLineC = new Array();	  // 管线底部					实线
  var dataBarA = new Array();     // 管井-路面到坐标轴
  var dataBarB = new Array();     // 管井-底高到坐标轴
  var _dataBarA = new Array();     // 管井-路面到坐标轴 低于海拔高度
  var _dataBarB = new Array();     // 管井-底高到坐标轴 低于海拔高度
  var xAx = 20; //横坐标
  var minHeight = 1000.0; //最小坐标值
  for (var i = 0; i < gjList.length; i ++) {
    var gj = gjList[i];     // 获得当前管井
    var gx;      
    if (gj.top_Height * 1 > 0) {
      dataBarA.push([xAx, gj.top_Height]);
    } else {
      _dataBarA.push([xAx, gj.top_Height]);
    }
    if (gj.base_Height > 0) {
      dataBarB.push([xAx, gj.base_Height]);
    } else {
      _dataBarB.push([xAx, gj.base_Height]);
    }
    dataLineA.push([xAx, gj.top_Height]); //获得管井顶高-地面
    var xAxObj = new Object();
    xAxObj.xAx = xAx;
    xAxObj.tId = gj.id;
    xAxArray.push(xAxObj);
    for (var j = 0; j < gxList.length; j ++) {
      if (gj.out_Id == gxList[j].id) {
        gx = gxList[j];     // 获得当前管线
        dataLineB.push([xAx, gx.start_Height*1 + gx.diameter / 1000]);  //获得管线起点顶高
        dataLineC.push([xAx, gx.start_Height]); //获得管线起点底高
        xAx += gx.length*1;
        if (i < gjList.length) { //最后一个管线没有终点
          dataLineB.push([xAx, gx.end_Height * 1 + gx.diameter / 1000]);  //获得管线终点顶高
          dataLineC.push([xAx, gx.end_Height]); //获得管线终点底高
        }
        if (minHeight - gj.base_Height > 0) { minHeight = gj.base_Height }
      }
    }
  }
 
  minHeight = parseInt(minHeight);
  const option = {
    xAxis: {
      type: "value"
    },
    yAxis: {
      splitLine: { show: false },
      min: minHeight
    },
    animationDurationUpdate: 1200,
    series: [{
      type: 'line',
      lineStyle: { color: "#778899" },
      itemStyle: { color: "#778899" },
      symbol: 'circle',
      label: {
        show: true,
        formatter: function (params) {
          for (var i = 0; i < xAxArray.length; i ++) {
            if (xAxArray[i].xAx == params.data[0]) {
              return xAxArray[i].tId.substr(5, 8)
            }
          }
        }
      },
      data: dataLineA
    }, {
      type: 'line',
      lineStyle: { color: "#778899" },
      itemStyle: { color: "#778899" },
      symbol: 'circle',
      data: dataLineB
    }, {
      type: 'line',
      lineStyle: { color: "#778899" },
      itemStyle: { color: "#778899" },
      symbol: 'circle',
      data: dataLineC
    }, {
      type: 'bar',
      itemStyle: { color: "#778899"},
      silent: true,
      barWidth: 20,
      data: dataBarA
    }, {
      type: 'bar',
      itemStyle: { color: "#FFFFFF" },
      silent: true,
      barWidth: 20,
      barGap: "-100%",
      borderWidth: 0,
      data: dataBarB
    }, {
      type: 'bar',
      itemStyle: { color: "#FFFFFF" },
      silent: true,
      barWidth: 20,
      barGap: "-100%",
      borderWidth: 0,
      data: _dataBarA
    }, {
      type: 'bar',
      itemStyle: { color: "#778899" },
      silent: true,
      barWidth: 20,
      barGap: "-100%",
      borderWidth: 0,
      data: _dataBarB
    }]
  };
  chart.setOption(option);
}

// doGraphCut: function (gjObj, gxObj) {
  //   var that = this;
  //   const ctx = wx.createCanvasContext('graphCut')
  //   if(gjObj == null || gxObj == null) {
  //     return;
  //   }
  //   console.log(gjObj);
  //   console.log(gxObj);
  //   that.setData({
  //     graphCut: ""
  //   })
  //   var maxTop = 0.0;
  //   for (var i = 0; i < gjObj.length; i ++) { //取到最高的管井
  //     var top_Height = gjObj[i].top_Height;
  //     if (top_Height > maxTop) { maxTop = top_Height}
  //   }
  //   var x = 30.0;
  //   var gjWidth = 10.0;
  //   var gjHeight = 0.0;
  //   var i = 0;
  //   while (true) {
  //     if (gjObj[i].flag == 2) {
  //       break;
  //     }
  //     var gx;
  //     for (var j = 0; j < gxObj.length; j ++) { //取到当前管线
  //       if (gjObj[i].out_Id == gxObj[j].id) {
  //         gx = gxObj[j];
  //       }
  //     }
  //     if (i > 0) { x += parseFloat(gx.length)*5}; //横坐标=管线长度累积，第一个不计入
  //     var y = (parseFloat(maxTop) - parseFloat(gjObj[i].top_Height))*30 +20;
  //     gjHeight = (parseFloat(gjObj[i].top_Height) - parseFloat(gjObj[i].base_Height))*30;
  //     //画管井
  //     console.log(gjObj[i].id);
  //     console.log("gj..x[" + x + "],y[" + y + "],w[" + gjWidth + "],h[" + gjHeight+"]");
  //     ctx.fillRect(x, y, gjWidth, gjHeight);
  //     //画管井编号
  //     //ctx.setFontSize(20)
  //     ctx.fillText(gjObj[i].id.substr(5), x, y);
  //     if (gjObj[i].flag != 2) { //不是终点
  //       var nextGJ = gjObj[i+1]; //获得下一个管井
  //       //画地面
  //       var mDmX = x;
  //       var mDmY = y;
  //       var lDmX = parseFloat(x) + parseFloat(gx.length) * 5;
  //       var lDmY = (parseFloat(maxTop) - parseFloat(nextGJ.top_Height))* 30 + 20;
  //       ctx.beginPath();
  //       console.log("mDmX[" + mDmX + "]mDmY[" + mDmY + "]lDmX[" + lDmX + "]lDmY[" + lDmY+"]")
  //       ctx.moveTo(mDmX, mDmY);
  //       ctx.lineTo(lDmX, lDmY);
  //       ctx.stroke();
  //       //画管线 - 底
  //       var mGdX = x;
  //       var mGdY = parseFloat(y) + (parseFloat(gjObj[i].top_Height) - parseFloat(gx.start_Height)) * 30;
  //       var lGdX = parseFloat(x) + parseFloat(gx.length) * 5;
  //       var lGdY = parseFloat(y) + (parseFloat(nextGJ.top_Height) - parseFloat(gx.start_Height)) * 30;
  //       console.log("mGdX[" + mGdX + "]mGdY[" + mGdY + "]lGdX[" + lGdX + "]lGdY[" + lGdY + "]")
  //       ctx.beginPath()
  //       ctx.moveTo(mGdX, mGdY)
  //       ctx.lineTo(lGdX, lGdY)
  //       ctx.stroke()
  //       //画管线 - 顶
  //       var mGtX = mGdX;
  //       var mGtY = parseFloat(y) - parseFloat(gx.diameter)/10 + (parseFloat(gjObj[i].top_Height) - parseFloat(gx.start_Height)) * 30;
  //       var lGtX = lGdX;
  //       var lGtY = parseFloat(y) - parseFloat(gx.diameter) / 10 + (parseFloat(nextGJ.top_Height) - parseFloat(gx.start_Height)) * 30;
  //       console.log("mGtX[" + mGtX + "]mGtY[" + mGtY + "]lGtX[" + lGtX + "]lGtY[" + lGtY + "]")
  //       ctx.beginPath()
  //       ctx.moveTo(mGtX, mGtY)
  //       ctx.lineTo(lGtX, lGtY)
  //       ctx.stroke()
  //     }
  //     i ++;
  //   }
  //   // ctx.setFillStyle('red')
  //   // ctx.fillRect(10, 10, 150, 75)

  //   // ctx.beginPath()
  //   // ctx.moveTo(10, 100)
  //   // ctx.lineTo(150, 100)
  //   // ctx.setStrokeStyle('red')
  //   // ctx.stroke()

  //   // ctx.setFontSize(20)
  //   // ctx.setFillStyle('orange')
  //   // ctx.fillText('canvas ', 10, 150)

  //   ctx.draw()
  //   console.log(x)
  //   that.setData ({
  //     graphWidth: x + 50
  //   })
  // },