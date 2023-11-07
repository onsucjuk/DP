$(document).ready(function () {

    let f = document.getElementById("f");

    // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
    $("#btnSend").on("click", function () {
        doSubmit(); // 공지사항 등록하기 실행
    })

    $("#btnTourName").on("click", function () {
        tourNameExists(f)

    })

    if (SS_USER_ID.length < 0) {
        alert("로그인 해주세요.");
        location.href = "/user/login";
    }
})

//글자 길이 바이트 단위로 체크하기(바이트값 전달)
function calBytes(str) {
    let tcount = 0;
    let tmpStr = String(str);
    let strCnt = tmpStr.length;
    let onechar;
    for (let i = 0; i < strCnt; i++) {
        onechar = tmpStr.charAt(i);
        if (escape(onechar).length > 4) {
            tcount += 2;
        } else {
            tcount += 1;
        }
    }
    return tcount;
}

function tourNameExists(f) {

    if (f.tourName.value === ""){
        alert("여행명을 입력하세요.");
        f.tourName.focus();
        return;
    }

    $.ajax({
            url: "/tour/getTourNameExists",
            type: "post",
            dataType: "JSON",
            data: $("#f").serialize(),
            success: function (json) {
                if(json.existsYn === "Y") {
                    alert("동일한 여행명이 존재합니다.");
                    tourNameCheck = "Y";
                    f.tourName.focus();
                } else {
                    alert("작성 가능한 여행명입니다.");
                    tourNameCheck = "N";
                }
            }
        }
    )
}


function doSubmit() {

    let f = document.getElementById("f"); // form 태그

    if (f.tourName.value === "") {
        alert("여행명을 입력하시기 바랍니다.");
        f.tourName.focus();
        return;
    }
    if (calBytes(f.tourName.value) > 240) {
        alert("최대 240Bytes까지 입력 가능합니다.");
        f.tourName.focus();
        return;
    }

    if (tourNameCheck != "N") {
        alert("여행명 중복 체크 및 중복되지 않은 여행명을 등록 바랍니다.");
        f.tourName.focus();
        return;
    }

    // Ajax 호출해서 글 등록하기
    $.ajax({
            url: "/tour/insertTourInfo",
            type: "post", // 전송방식은 Post
            // contentType: "application/json",
            dataType: "JSON", // 전송 결과는 JSON으로 받기
            data: $("#f").serialize(), // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
            success: function (json) {
                alert(json.msg); // 메시지 띄우기
                if(json.msg==="로그인 해주세요."){
                    location.href = "/user/login";
                } else {
                    location.href = "/tour/tourInfo"; // 여행 정보로 이동
                }
            }
        }
    )
}