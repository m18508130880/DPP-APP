var app = getApp()
function writeStatus(status, icon) {
  // 弹出提示框
  wx.showToast({
    title: status,
    icon: icon
  })
};
Page({
  data: {
    advice:'',
    input_length: '0/400',
    photo:[],
    photo_demo: false,
    phone:'',
    selected: false
  },
  // 初始化
  onLoad: function () {
    var that = this;
    var userInfo = wx.getStorageSync("userInfo");
    if(userInfo == null){
      wx.navigateTo({
        url: "../login/login"
      })
    }
    //that.Login();
  },
  input_advice: function (e) {
    console.log(e)
    var value = e.detail.value;
    var select = false;
    if (value.length > 0){
      select = true;
    }
    this.setData({
      advice:value,
      input_length: value.length+'/400',
      selected: select
    })
  },
  importPhoto: function (e) {
      var that = this;
      var paths = new Array();
      wx.chooseImage({
        count: 3,
        sizeType: ["compressed"],
        success: function (res) {
          paths = res.tempFilePaths;
          console.log(paths)
        },
        fail: function (res) {
          console.log(res);
        },
        complete: function (res) {

        }
      })
  }
})