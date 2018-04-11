
var wxCharts = require('../../../libs/wxcharts.js');
Page({
  data: {
    width:"510px"
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.lineShow();
  }, 
  lineShow: function (type) {
    const ctx = wx.createCanvasContext('stage')
    
    ctx.setFillStyle('red')
    ctx.fillRect(10, 10, 150, 75)

    ctx.beginPath()
    ctx.moveTo(10, 100)
    ctx.lineTo(150, 100)
    ctx.setStrokeStyle('red')
    ctx.stroke()

    ctx.setFontSize(20)
    ctx.setFillStyle('orange')
    ctx.fillText('canvas ', 10, 150)
    
    ctx.draw()
  },
  line(canvasId, options) {
    let windowWidth = 0
    wx.getSystemInfo({
      success(result) {
        windowWidth = result.windowWidth
      }
    })
    let a = windowWidth / (options.xAxis.length - 1)
    let data = []
    options.xAxis.map((item, i) => {
      data.push([i * a, 200 - options.yAxis[i]])
    })

    const ctx = wx.createCanvasContext(canvasId)
    ctx.beginPath()
    data.map((item, i) => {
      if (i == 0) {
        ctx.moveTo(item[0], item[1])
      }
      ctx.lineTo(item[0], item[1])
    })
    ctx.setLineWidth(1)
    ctx.setLineCap('square')
    ctx.setStrokeStyle('red')
    ctx.stroke()
    ctx.draw()
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    
  }
})
