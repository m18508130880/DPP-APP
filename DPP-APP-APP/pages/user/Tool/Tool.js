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
    des: ''
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
  },
  des: function (e) {
    this.setData({
      des: e.detail.value
    })
  },
  camera: function () {
    var that = this;
    var paths = new Array();
    wx.chooseImage({
      count: 3,
      sizeType: ["compressed"],
      success: function (res) {
        console.log(res)
        images.length = 0;
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
    const uploadTask = wx.uploadFile({
      url: 'https://www.cjsci-tech.com/dpp-app/uploadImg.do',
      filePath: path,//图片路径，如tempFilePaths[0]
      name: 'image',
      header: {
        "Content-Type": "multipart/form-data"
      },
      formData:
      {
        Id: new Date().getTime()+"_"+i,
      },
      success: function (res) {
        images[i] = res.data;
        that.setData({
          images: images
        })
        //console.log(that.data.images)
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
  submit: function () {
    var that = this;
    var des = that.data.des;
    if (des.length <= 0) {
      var status = "请输入问题描述";
      var icon = "loading";
      writeStatus(status, icon);
      return;
    }
    var images = that.data.images;
    var image = "";
    if (images.length > 0) {
      for (var i = 0; i < images.length; i ++) {
        image += images[i] + ",";
      }
    }
    console.log("image[" + image+"]des["+des+"]")
    wx.request({
      url: 'https://www.cjsci-tech.com/dpp-app/Login.do',
      data: {
        image: image,
        des: des,
        token: token
      },
      header: { 'Content-Type': 'application/x-www-form-urlencoded' },
      method: 'POST',
      success: function (res) {
        console.log(res.data)
        if (res.data.substring(0, 4) == "0000") {
          status = "成功";
          icon = "success";
          that.setData({
            images: [],
            des: ""
          })
        }
      },
      fail: function (res) {
        console.log("fail")
        console.log(res)
        status = "失败";
        icon = "loading";
      },
      complete: function () {
        writeStatus(status, icon);
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
