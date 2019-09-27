// 将video转化为Canvas
var VideoToCanvas = (function(window, document) {
    function VideoToCanvas(videoElement) {
      if(!videoElement) {return;}

      var canvas = document.createElement('canvas');
      canvas.width = videoElement.offsetWidth;
      canvas.height = videoElement.offsetHeight;
      ctx = canvas.getContext('2d');

      var newVideo = videoElement.cloneNode(false);

      var timer = null;

      var requestAnimationFrame = window.requestAnimationFrame || window.mozRequestAnimationFrame ||
                                  window.webkitRequestAnimationFrame || window.msRequestAnimationFrame;

      var cancelAnimationFrame = window.cancelAnimationFrame || window.mozCancelAnimationFrame;

      function drawCanvas() {
        ctx.drawImage(newVideo, 0, 0, canvas.width, canvas.height);
        timer = requestAnimationFrame(drawCanvas);
      }

      function stopDrawing() {
        cancelAnimationFrame(timer);
      }

      newVideo.addEventListener('play', function() {
        drawCanvas();
      },false);
      newVideo.addEventListener('pause', stopDrawing, false);
      newVideo.addEventListener('ended', stopDrawing, false);

      videoElement.parentNode.replaceChild(canvas, video);

      this.play = function() {
        newVideo.play();
      };

      this.pause = function() {
        newVideo.pause();
      };

      this.playPause = function() {
        if(newVideo.paused) {
          this.play();
        } else {
          this.pause();
        }
      };

      this.change = function(src) {
        if(!src) {return;}
        newVideo.src = src;
      };

      this.drawFrame = drawCanvas;
    }

    return VideoToCanvas;
  })(window, document);
  
  // 使用方式
  var video = document.getElementById('myVideo');
  var canvas = new VideoToCanvas(video);
