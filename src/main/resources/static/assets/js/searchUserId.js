// HTML로딩이 완료되고, 실행됨
$(document).ready(function () {

    let f = document.getElementById("f");

    // 로그인 화면 이동
    $("#btnLogin").on("click", function () { // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
        location.href = "/user/login";
    })

    // 아이디 찾기
    $("#btnSearchUserId").on("click", function () {
        let f = document.getElementById("f"); // form 태그

        if (f.userName.value === "") {
            alert("이름을 입력하세요.");
            f.userName.focus();
            return;
        }

        if (f.email.value === "") {
            alert("이메일을 입력하세요.");
            f.email.focus();
            return;
        } else {
            $.ajax({
                    url: "/user/getEmailExists",
                    type: "post",
                    dataType: "JSON",
                    data: $("#f").serialize(),
                    success: function (json) {
                        if(json.existsYn === "N") {
                            alert("가입된 이메일 정보가 없습니다.");
                            f.email.focus();
                        } else {

                            f.method = "post"; // 아이디 찾기 정보 전송 방식
                            f.action = "/user/searchUserIdProc" // 아이디 찾기 URL
                            f.submit(); // 아이디 찾기 정보 전송하기

                        }
                    }
                }
            )
        }
    })
})
