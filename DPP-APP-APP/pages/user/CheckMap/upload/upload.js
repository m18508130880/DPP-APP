// pages/user/data/data.js
var app = getApp()
var token = "";
var isOK = false; 
var images = new Array();
function writeStatus(status, icon) {
  // 弹出提示框
  wx.showToast({
    title: status,
    icon: icon
  })
}
Page({
  data: {
    images: [],
    select: true,
    project: [],
    gjArray: [],
    gjIndex: 0,
    gxArray: [],
    gxIndex: 0,
    gjId:"",
    gxId:"",
    gjStatus: ["选择问题", "位置不明", "井盖打不开", "底高无数据", "井室无数据", "顶高无数据", "其他"],
    gjS: 0,
    gxStatus: ["选择问题", "位置不明", "顶高不明", "管径不明", "水位过高", "其他"],
    gxS: 0,
    SN:"",
    des:""
  },
  onLoad: function (str) {  //初始化
    var that = this;
    var project_Id = str.project_id;
    var SN = str.SN;
    console.log("sn="+SN)
    that.setData({
      project: {
        uId: str.uId,
        project_id: project_Id,
        lng: str.lng,
        lat: str.lat
      },
      SN: SN
    })
    token = str.token;
    wx.request({
      url: 'https://www.cjsci-tech.com/dpp-app/Check_GJ.do',
      //url: 'http://118.31.78.234/dpp-app/ToPo_GJ.do',
      data: {
        Cmd: "1",
        Token: token,
        CT_SN: SN
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data)
        if (res.data.rst == "0000") {
          var gjList = res.data.cData;
          var gjArray = new Array();
          gjArray.push("选择管井");
          for (var i = 0; i < gjList.length; i++) {
            if (gjList[i].status == "0") {
              gjArray.push(gjList[i].id);
            }
          }
          that.setData({
            gjArray: gjArray
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
    wx.request({
      url: 'https://www.cjsci-tech.com/dpp-app/Check_GX.do',
      //url: 'http://118.31.78.234/dpp-app/ToPo_GX.do',
      data: {
        Cmd: "1",
        Token: token,
        CT_SN: SN
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data)
        if (res.data.rst == "0000") {
          var gxList = res.data.cData;
          var gxArray = new Array();
          gxArray.push("选择管线");
          for (var i = 0; i < gxList.length; i++) {
            if (gxList[i].status == "0") {
              gxArray.push(gxList[i].id);
            }
          }
          that.setData({
            gxArray: gxArray
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
  des: function (e) {
    this.setData({
      des: e.detail.value
    })
  },
  camera: function () {
    var that = this;
    if (that.data.gjIndex == 0 && that.data.gxIndex == 0) {
      var status = "请先选择编号";
      var icon = "loading";
      writeStatus(status, icon);
      return;
    }
    var paths = new Array();
    wx.chooseImage({
      count:3,
      sizeType: ["compressed"],
      success: function (res) {
        console.log(res)
        paths = res.tempFilePaths;
      },
      fail: function (res) {
        console.log(res);
      },
      complete: function (res) {
        for (var i = 0; i < paths.length; i++) {
          that.uploadImg(paths[i], i)
        }
      }
    })
  },
  uploadImg: function (path, i) {
    var that = this;
    var SN = that.data.SN;
    var gjId = that.data.gjId;
    var gxId = that.data.gxId;
    var Id = "";
    if (gjId.indexOf("J") >= 0){
      Id = gjId;
    }else {
      Id = gxId;
    }
    const uploadTask = wx.uploadFile({
      url: 'https://www.cjsci-tech.com/dpp-app/uploadImg.do',
      filePath: path,//图片路径，如tempFilePaths[0]
      name: 'image',
      header: {
        "Content-Type": "multipart/form-data"
      },
      formData:
      {
        Id: Id + "_" + i,
        CT_SN: SN
      },
      success: function (res) {
        console.log(res.data);
        images[i] = res.data;
        that.setData({
          images: images
        })
      },
      fail: function (res) {
        console.log(res);
      },
      complete: function (res) {

      }
    })
    uploadTask.onProgressUpdate((res) => {
      console.log('上传进度', res.progress)
      console.log('已经上传的数据长度', res.totalBytesSent)
      console.log('预期需要上传的数据总长度', res.totalBytesExpectedToSend)
    })
    //uploadTask.abort() // 取消上传任务
  },
  gotoGX: function () {
    this.setData({
      select: !this.data.select,
      images: [], 
      gjIndex: 0,
      gxIndex: 0,
      gjId: "",
      gxId: "",
      des: ""
    })
  },
  bindGJ: function (e) {
    var gjArray = this.data.gjArray;
    this.setData({
      gjIndex: e.detail.value,
      gjId: gjArray[e.detail.value]
    })
  },
  bindGX: function (e) {
    var gxArray = this.data.gxArray;
    this.setData({
      gxIndex: e.detail.value,
      gxId: gxArray[e.detail.value]
    })
  },
  bindGJS: function (e) {
    this.setData({
      gjS: e.detail.value,
    })
  },
  bindGXS: function (e) {
    this.setData({
      gxS: e.detail.value,
    })
  },
  submitGJ: function () {
    var that = this;
    var SN = that.data.SN;
    var Id = that.data.gjId;
    if (that.data.gjIndex == 0) {
      var status = "请选择管井";
      var icon = "loading";
      writeStatus(status, icon);
      return;
    }
    var suatus = that.data.gjS;
    if (suatus == 0) {
      var status = "请选择问题";
      var icon = "loading";
      writeStatus(status, icon);
      return;
    }
    var images = that.data.images;
    var image = "";
    for (var i = 0; i < images.length; i ++) {
      image += images[i] + ",";
    }
    var des = that.data.des;
    console.log("SN[" + SN + "]Id[" + Id + "]suatus[" + suatus+"]image[" + image+"]des["+des+"]")
  },
  submitGX: function () {
    var that = this;
    var SN = that.data.SN;
    var Id = that.data.gxId;
    if (that.data.gxIndex == 0) {
      var status = "请选择管线";
      var icon = "loading";
      writeStatus(status, icon);
      return;
    }
    var suatus = that.data.gxS;
    if (suatus == 0) {
      var status = "请选择问题";
      var icon = "loading";
      writeStatus(status, icon);
      return;
    }
    var images = that.data.images;
    var image = "";
    for (var i = 0; i < images.length; i++) {
      image += images[i] + ",";
    }
    var des = that.data.des;
    console.log("SN[" + SN + "]Id[" + Id + "]suatus[" + suatus+"]image[" + image + "]des[" + des + "]")
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
