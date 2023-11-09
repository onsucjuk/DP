$(document).ready(function () {
    // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)

    let f = document.getElementById("f");

    $("#btnPlaceFind").on("click", function () {
        location.href = "/tour/tourPlaceFind";
    })

    $("#btnPlaceReg").on("click", function () {
        doPlaceReg(f)
    })

    $("#btnDayInfo").on("click", function () {

        location.href = "/tour/tourDayInfo?nSeq=" + nDay;

    })

    function doPlaceReg(f) {

        if (f.placeNick.value === "") {
            alert("목적지를 입력하세요.");
            f.placeNick.focus();
            return;
        }

        if (f.placeName.value === "") {
            alert("목적지명을 입력하세요.");
            f.placeName.focus();
            return;
        }

        if (f.placeAddr.value === "") {
            alert("주소를 입력하세요.");
            f.placeAddr.focus();
            return;
        }

        if (f.lat.value === "") {
            alert("목적지를 찾기로 추가하세요.");
            f.placeName.focus();
            return;
        }

        if (f.lon.value === "") {
            alert("목적지를 찾기로 추가하세요.");
            f.placeName.focus();
            return;
        }

        if (f.poi.value === "") {
            alert("목적지를 찾기로 추가하세요.");
            f.placeName.focus();
            return;
        }

        // Ajax 호출해서 회원가입하기
        $.ajax({
            url: "/tour/insertTourPlace",
            type: "post",
            datatype: "JSON",
            data: $("#f").serialize(),
            success: function (json) {

                if(json.result === 1) {
                    alert(json.msg);
                    location.href = "/user/login";

                } else {
                    alert(json.msg);
                    location.href = "/tour/tourDayInfo?nSeq="+nDay;
                }
            }
        })
    }

})
