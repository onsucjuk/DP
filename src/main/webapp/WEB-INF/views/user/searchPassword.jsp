<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>비밀번호 찾기</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">

        let emailAuthNumber = "";
        let emailCheck = "Y";

        // HTML로딩이 완료되고, 실행됨
        $(document).ready(function () {

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
                            if(json.existsYn === "Y") {
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

                        if (f.authNumber.value != emailAuthNumber) {
                            alert("이메일 인증번호가 일치하지 않습니다.");
                            emailCheck = "Y"
                            f.authNumber.focus();
                            return;

                        } else {
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
    </script>
</head>
<body>
<h2>비밀번호 찾기</h2>
<hr/>
<br/>
<form id="f">
    <div class="divTable minimalistBlack">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCell">아이디
                </div>
                <div class="divTableCell">
                    <input type="text" name="userId" id="userId" style="width:95%"/>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">이름
                </div>
                <div class="divTableCell">
                    <input type="text" name="userName" id="userName" style="width:95%"/>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">이메일
                </div>
                <div class="divTableCell">
                    <input type="email" name="email" id="email" style="width:78%"/>
                    <button id="btnSendAuth" type="button">인증번호 발송</button>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">인증번호확인
                </div>
                <div class="divTableCell">
                    <input type="text" name="authNumber" id="authNumber" style="width:78%"/>
                    <button id="btnAuth" type="button">인증번호확인</button>
                </div>
            </div>
        </div>
    </div>
    <div>
        <button id="btnSearchPassword" type="button">비밀번호 찾기</button>
        <button id="btnLogin" type="button">로그인</button>
    </div>
</form>
</body>
</html>

