$(document).ready(function () {
    // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)

    let f = document.getElementById("f");

    $("#btnEdit").on("click", function () {
        doEdit()
    })

    $("#btnBack").on("click", function () {
        history.back();
    })

    $("#btnAddr").on("click", function () {
        kakaoPost(f)

    })
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

function doEdit() {

    let f = document.getElementById("f"); // form 태그

    if (f.userName.value === "") {
        alert("이름을 입력하시기 바랍니다.");
        f.userName.focus();
        return;
    }
    if (calBytes(f.userName.value) > 240) {
        alert("이름은 최대 24자까지 입력 가능합니다.");
        f.userName.focus();
        return;
    }

    if (f.email.value === "") {
        alert("이메일을 입력하시기 바랍니다.");
        f.email.focus();
        return;
    }
    if (calBytes(f.email.value) > 1000) {
        alert("이메일는 최대 100자까지 입력 가능합니다.");
        f.email.focus();
        return;
    }

    if (f.addr1.value === "") {
        alert("주소를 입력하시기 바랍니다.");
        f.addr1.focus();
        return;
    }
    if (calBytes(f.addr1.value) > 1000) {
        alert("주소는 최대 100자까지 입력 가능합니다.");
        f.addr1.focus();
        return;
    }

    if (f.addr2.value === "") {
        alert("상세 주소를 입력하시기 바랍니다.");
        f.addr2.focus();
        return;
    }
    if (calBytes(f.addr2.value) > 1000) {
        alert("상세 주소는 최대 100자까지 입력 가능합니다.");
        f.addr2.focus();
        return;
    }

    // Ajax 호출해서 글 등록하기
    $.ajax({
            url: "/user/updateUserInfo",
            type: "post", // 전송방식은 Post
            dataType: "JSON", // 전송 결과는 JSON으로 받기
            data: $("#f").serialize(), // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
            success: function (json) {
                alert(json.msg); // 메시지 띄우기

                if (json.msg==="로그인해주세요.") {

                    location.href = "/user/login";

                } else if ("유저 정보 변경하였습니다."){

                    location.href = "/user/myPage";

                } else {

                    location.href = "/user/myPage";

                }
            }
        }
    )

}

// 카카오 우편번호 조회 API 호출
function kakaoPost(f) {
    new daum.Postcode({
        oncomplete: function (data) {

            // Kakao에서 제공하는 data는 JSON구조로 주소 조회 결과값을 전달함
            // 주요 결과값
            // 주소 : data.address
            // 우편번호 : data.zonecode
            let address = data.address; // 주소
            let zonecode = data.zonecode; // 우편번호
            f.addr1.value = "(" + zonecode + ")" + address
        }
    }).open();
}
