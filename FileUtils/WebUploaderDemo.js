var GUID = WebUploader.Base.guid(); //一个GUID
var uploader = WebUploader.create({
		swf: '/static/webuploader-0.1.5/Uploader.swf',
		server: ctx + 'fileUpload/blockUpload/upload',// 服务器地址
		pick: '#chooseVideo', // 创建上传按钮
		chunked: true, // 开始分片上传
		chunkSize: 5242880, // 每一片的大小， 50M
		auto: true, // 自动上传
		formData: {
			guid: GUID // 自定义参数
		}
	});

// 开始上传
uploader.on("uploadStart", function (file) {
	// TODO
});

// 所有分片上传成功, 开始合并
uploader.on('uploadSuccess', function (file, response) {
	var data = {};
	data.guid = GUID;
	data.fileName = file.name;
	data.evaluationId = $("#evaluationId").val();
	$.ajax({
		url: ctx + 'fileUpload/blockUpload/merge',
		type: "post",
		data: data,
		success: function (result) {
			// TODO
		}
	})
});