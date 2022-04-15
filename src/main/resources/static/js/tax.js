let index = {

	init: function() {
		$("#btn-select").on("click", () => { // ()=>{} : this를 바인딩하기 위해서..
			this.select();
		});
	},

	select: function() {
		
		var array = [];
		
		for (var i = 0; i < $(".number").length; i++) {
			array[i] = $(".number")[i].innerHTML;
		}
		
		var data = {
			"b_no": array // 사업자번호 "xxxxxxx" 로 조회 시,
		};

		$.ajax({
			type: "POST",
			url: "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=sBC5Y6wUFBw5ipm9767%2F1Cem0Z22wOJwyRNuDzCcaErWb9XjI%2BRL2mSHLZgbaUOnjM9vx%2B%2F4Mua9ZJWPyxxZAw%3D%3D",  // serviceKey 값을 xxxxxx에 입력
			data: JSON.stringify(data), // json 을 string으로 변환하여 전송
			dataType: "json",
			contentType: "application/json",
			accept: "application/json",
			success: function(result) {
				let res = [];
				
				for (var j = 0; j < result.data.length; j++) {
					if (result.data[j].tax_type.indexOf('간이과세자') !== -1) {
						res.push(result.data[j]);
					}
				}
				
				console.log(res);

			},
			error: function(result) {
				console.log(result.responseJSON); //responseText의 에러메세지 확인
			}
		});

	},

}

index.init();