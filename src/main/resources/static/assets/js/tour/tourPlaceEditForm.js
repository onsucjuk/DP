$(document).ready(function () {
    // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)

    let f = document.getElementById("f");

    $("#btnPlaceFind_E").on("click", function () {
        location.href = "/tour/tourPlaceFind_E";
    })

    $("#btnPlaceEdit").on("click", function () {
        doPlaceEdit(f)

    })

    $("#btnDayInfo").on("click", function () {

        location.href = "/tour/tourDayInfo?nSeq=" + nDay;

    })

    function doPlaceEdit(f) {

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
            url: "/tour/updateTourPlace",
            type: "post",
            datatype: "JSON",
            data: $("#f").serialize(),
            success: function (json) {

                if (json.result === 1) {
                    alert(json.msg);
                    location.href = "/user/login";

                } else {
                    alert(json.msg);
                    location.href = "/tour/tourDayInfo?nSeq=" + nDay;
                    // 성공 또는 실패와 관계없이 sessionStorage 제거
                    sessionStorage.removeItem("SS_PLACE_NICK");
                    sessionStorage.removeItem("SS_MEMO");
                    sessionStorage.removeItem("SS_PLACE_SEQ");
                }
            },
                error: function () {
                    alert("Ajax request failed.");

                    // 실패 시에도 sessionStorage 제거
                    sessionStorage.removeItem("SS_PLACE_NICK");
                    sessionStorage.removeItem("SS_MEMO");
                    sessionStorage.removeItem("SS_PLACE_SEQ");
                }
        })
    }

})
