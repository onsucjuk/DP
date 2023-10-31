$(document).ready(function () {
    // 로그인 화면 이동
    $("#btnLogin").on("click", function () { // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
        location.href = "/user/login";
    })

})