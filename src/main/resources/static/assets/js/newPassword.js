/*<![CDATA[*/
$(document).ready(function () {

    /* 비정상적인 접근 및 회원정보가 없는 경우 뒤로 가기 */
/*    let msg = [[${msg}]]
    if (msg.length > 0) {
        alert(msg);
        history.back();
    }*/

    /* 로그인 화면 이동 */
    $("#btnLogin").on("click", function () {
        location.href = "/user/login";
    });

    /* 비밀번호 찾기 */
    $("#btnSearchPassword").on("click", function () {

        var f = document.getElementById("f");

        if (f.password.value === "") {
            alert("새로운 비밀번호를 입력하세요.");
            f.password.focus();
            return;
        }

        if (f.password2.value === "") {
            alert("검증을 위한 새로운 비밀번호를 입력하세요.");
            f.password2.focus();
            return;
        }

        if (f.password.value !== f.password2.value) {
            alert("입력한 비밀번호가 일치하지 않습니다.");
            f.password.focus();
            return;
        }

        alert("비밀번호가 성공적으로 변경되었습니다.");
        f.method = "post";
        f.action = "/user/login";
        f.submit();
    });
});
/*]]>*/
