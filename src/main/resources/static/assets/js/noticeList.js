        //상세보기 이동
        function doDetail(seq) {
            location.href = "/notice/noticeInfo?nSeq=" + seq;
        }

        $(document).ready(function () {

            $("#btnListReg").on("click", function () {
                location.href = "/notice/noticeReg";
            })
            $("#btnUserReg").on("click", function () {
                location.href = "/user/userRegForm";
            })
        })