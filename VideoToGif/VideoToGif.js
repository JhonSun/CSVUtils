document.write("<script src="../static/gif/gif.js" th:src="@{/gif/gif.js}"></script>");

var imgs = [];

function getVideoImage() {
	var videoElement = document.getElementById("myVideo");
	var canvas = document.createElement("canvas");
	canvas.width = videoElement.videoWidth;
	canvas.height = videoElement.videoHeight;
	console.log(videoElement.videoWidth)
	canvas.getContext('2d').drawImage(videoElement, 0, 0, canvas.width, canvas.height);
	$("#myImg").attr("src", canvas.toDataURL("image/png"));
	var img = document.createElement("img");
	img.src = canvas.toDataURL("image/png");
	imgs.push(img);
}

function start() {
	var gif = new GIF({
			workers: 2, //　启用两个worker
			quality: 10, // 图像质量
			workerScript: '../gif/gif.worker.js' // 文件相对路径
		});

	// 将帧画面放到gif工具中去
	for (var i = 0; i < imgs.length; i++) {
		var imgElement = imgs[i];
		gif.addFrame(imgElement);
	}

	gif.render(); //开始启动

	gif.on('finished', function (blob) { //最后生成一个blob对象
		//	gif制作完成
		var url = URL.createObjectURL(blob);
		$("#myImg").attr("src", url);
	});

}
