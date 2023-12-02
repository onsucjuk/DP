function doDetail(tourSeq) {

    $.ajax({
            url: "/tour/setTourSeq",
            type: "post", // 전송방식은 Post
            dataType: "JSON", // 전송 결과는 JSON으로 받기
            data: {"tourSeq": tourSeq}, // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
            success:
                function (json) {

                    if(json.result===0) {

                        alert(json.msg);
                        location.href = "/user/login"

                    } else if(json.result===1) {

                        console.log(json.msg)
                        location.href = "/tour/viewItda?dSeq=1"

                    } else {

                        alert(json.msg);
                        location.href = "/index/index"

                    }

        }
        }
    )



}