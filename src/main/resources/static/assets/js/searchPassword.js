// HTML로딩이 완료되고, 실행됨
$(document).ready(function () {

    let f = document.getElementById("f");

    // 로그인 화면 이동
    $("#btnLogin").on("click", function () { // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
        location.href = "/user/login";
    })

    $("#btnSendAuth").on("click", function () {
        $.ajax({
                url: "/user/sendEmailAuth",
                type: "post",
                dataType: "JSON",
                data: $("#f").serialize(),
                success: function (json) {
                    if (f.email.value === ""){
                        alert("이메일 정보를 입력해주세요.")
                        return;
                    }
                    else if(json.existsYn === "Y") {
                        alert("이메일로 인증번호가 발송되었습니다. \n받은 메일의 인증번호를 입력하기 바랍니다.");
                        emailAuthNumber = json.authNumber;
                    } else {
                        alert("가입된 이메일 정보가 없습니다.");
                        f.email.focus();
                    }
                }
            }
        )
    })

    $("#btnAuth").on("click", function () {

        $.ajax({
                url: "/user/getUserIdExists",
                type: "post",
                dataType: "JSON",
                data: $("#f").serialize(),
                success: function (json) {

                    if (f.authNumber.value === "") {
                        alert("인증번호를 입력해주세요.");
                        f.authNumber.focus();
                        return;
                    }
                    else if (f.authNumber.value != emailAuthNumber) {
                        alert("이메일 인증번호가 일치하지 않습니다.");
                        emailCheck = "Y"
                        f.authNumber.focus();
                        return;

                    }
                    else {
                        alert("인증번호가 확인되었습니다.");
                        emailCheck = "N";
                    }
                }
            }
        )

    })

    // 비밀번호  찾기
    $("#btnSearchPassword").on("click", function () {
        let f = document.getElementById("f"); // form 태그

        if (f.userId.value === "") {
            alert("아이디를 입력하세요.");
            f.userId.focus();
            return;
        }

        if (f.userName.value === "") {
            alert("이름을 입력하세요.");
            f.userName.focus();
            return;
        }

        if (f.email.value === "") {
            alert("이메일을 입력하세요.");
            f.email.focus();
            return;
        }

        if (emailCheck !== "N") {
            alert("이메일 인증번호 확인해주세요.");
            f.authNumber.focus();
            return;
        }

        f.method = "post"; // 비밀번호 찾기 정보 전송 방식
        f.action = "/user/searchPasswordProc" // 비밀번호 찾기 URL

        f.submit(); // 아이디 찾기 정보 전송하기
    })
})