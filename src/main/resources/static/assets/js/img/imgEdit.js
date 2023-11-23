$(document).ready(function () {

    $("#btnSend").on("click", function () {
        doEdit();
    })

    $("#checkYn").on("click", function () {

        doCheck();
    })

    $("#btnClose").on("click", function () {

        window.close();
    })

    $("#file").on("click", function () {

        CEHCK_YN = "N";
    })

    if (SS_USER_ID == null || !(SS_USER_ID.length > 0)) {
        alert("로그인 해주세요.");
        location.href = "/user/login";
    }
})

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

    if (f.title.value === "") {
        alert("제목을 입력하시기 바랍니다.");
        f.title.focus();
        return;
    }
    if (calBytes(f.title.value) > 200) {
        alert("최대 200Bytes까지 입력 가능합니다.");
        f.title.focus();
        return;
    }

    if (f.contents.value === "") {
        alert("내용을 입력하시기 바랍니다.");
        f.contents.focus();
        return;
    }
    if (calBytes(f.contents.value) > 4000) {
        alert("최대 4000Bytes까지 입력 가능합니다.");
        f.contents.focus();
        return;
    }

    let checkbox = document.getElementById('noEditImg');

    if (checkbox.checked) {

        let form = $("#f")[0];
        let formData = new FormData(form);

        console.log(formData);

        // Ajax 호출해서 글 등록하기
        $.ajax({
                url: "/img/editWithoutImg",
                enctype:'multipart/form-data',
                type: "post", // 전송방식은 Post
                // contentType: "application/json",
                dataType : "JSON",
                data : formData,
                contentType : false,
                processData : false,
                cache:false,
                success: function (json) {
                    alert(json.msg);
                    if(json.msg==="로그인 해주세요."){
                        location.href = "/user/login";
                    } else {
                        window.close();
                    }
                }
            }
        )



    } else {

        let fileCheck = $("#file").val();

        if(!fileCheck){

            alert("파일을 첨부해 주세요");
            return;

        }

        if (CEHCK_YN === "N") {
            alert("사진 유효성 검사를 수행해주세요.")
            return;
        }

        let form = $("#f")[0];
        let formData = new FormData(form);

        console.log(formData);

        // Ajax 호출해서 글 등록하기
        $.ajax({
                url: "/img/editImg",
                enctype:'multipart/form-data',
                type: "post", // 전송방식은 Post
                // contentType: "application/json",
                dataType : "JSON",
                data : formData,
                contentType : false,
                processData : false,
                cache:false,
                success: function (json) {
                    alert(json.msg);
                    if(json.msg==="로그인 해주세요."){
                        location.href = "/user/login";
                    } else {
                        window.close();
                    }
                }
            }
        )

    }
}


function doCheck() {

    let fileCheck = $("#file").val();

    if(!fileCheck){

        alert("파일을 첨부해 주세요");
        return;

    }

    let form = $("#f")[0];
    let formData = new FormData(form);

    console.log(formData.get('file'));

    // Ajax 호출해서 글 등록하기
    $.ajax({
            url: "/img/imgGPS",
            enctype:'multipart/form-data',
            type: "post", // 전송방식은 Post
            // contentType: "application/json",
            dataType : "JSON",
            data : formData,
            contentType : false,
            processData : false,
            cache:false,
            success: function (json) {
                alert(json.msg);
                if(json.msg==="로그인 해주세요."){
                    location.href = "/user/login";
                } else if(json.msg==="등록 가능한 사진입니다.") {
                    CEHCK_YN = "Y"
                } else {
                    CEHCK_YN = "N"
                }
            }
        }
    )
}