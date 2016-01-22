<html>
<head>
    <title>agilor-test</title>


    <style type="text/css">

        .interval {
        
            display:block;
            margin-top:30px;
        }

    </style>
</head>
<body>

    <p  style="color:red;font-size:30px">handle result:<span id="print"></span> </p>
    



    <div class="interval">
        <label for="u">username:</label> <input type="text" name="u" id="u" value="admin" />
        <label for="p">password:</label> <input type="text" name="p" id="p" value="123456" />
        <button onclick="commit()">commit</button>
    </div>

    <div class="interval">
        <button type="button" id="cookie" onclick="getCookie()">get cookie</button>
        </div>


</body>
</html>

<script type="text/javascript">

    var u = document.getElementById("u");
    var p = document.getElementById("p");

    var print = document.getElementById("print");


    var _cookie = document.getElementById("cookie");



   


    var request = function (type,url,async,data) {
        var http = new XMLHttpRequest();


        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200)
                print.innerHTML = http.responseText;
            else if(http.readyState==4 && http.status!=200)
                print.innerHTML=http.status;
        }


        http.open(type, url+"?t=" + Math.random(), async);

        http.setRequestHeader("Access-Control-Allow-Origin", "*");

        if (type == "POST") 
            http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        

        var _data = "";
        for (var it in data) {
            _data += encodeURIComponent(it) + "=" + encodeURIComponent(data[it]) + "&";
        }

        console.log(_data);
        
        http.send(_data);
        

    }


    var commit = function () {
        request("POST", "/test/user/register", true, { u:u.value, p: p.value });
    }


    var getCookie = function () {
        print.innerHTML = "cookie:"+document.cookie;
    }


</script>