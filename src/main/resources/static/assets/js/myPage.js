$(document).ready(function () {
    // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
    $("#btnEdit").on("click", function () {
        location.href = "/user/myPageEdit"
    })
})