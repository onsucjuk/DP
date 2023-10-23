<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import ="dp.fdis.dto.UserInfoDTO" %>
<%@ page import ="dp.fdis.util.CmmUtil" %>
<%
    String ssUserName = CmmUtil.nvl((String) session.getAttribute("SS_USER_NAME"));
    String ssUserId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
%>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>로그인 성공</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">

        // HTML로딩이 완료되고, 실행됨
        $(document).ready(function () {
            // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
            $("#btnSend").on("click", function () {
                location.href = "/html/index.html";
            })

            // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
            $("#btnLogin").on("click", function () {
                location.href = "/user/login";
            })

            // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
            $("#btnList").on("click", function () {
                location.href = "/notice/noticeList";
            })

            $("#btnMyPage").on("click", function () {
                location.href = "/user/myPage";
            })
        })
    </script>
</head>
<body>
<div class="divTable minimalistBlack">
    <div class="divTableBody">
        <div class="divTableRow">
            <div class="divTableCell">로그인된 사용자이름
            </div>
            <div class="divTableCell"> <%=ssUserName%> 님이 로그인하였습니다.</div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">로그인된 사용자아이디
            </div>
            <div class="divTableCell"> <%=ssUserId%> 입니다.</div>
        </div>

    </div>
</div>
<div></div>
<br/><br/>
<button id="btnSend" type="button">메인 화면 이동</button>
<button id="btnList" type="button">게시판 이동</button>
<button id="btnLogin" type="button">로그인 화면 이동</button>
<button id="btnMyPage" type="button">마이 페이지 이동</button>
</body>
</html>
