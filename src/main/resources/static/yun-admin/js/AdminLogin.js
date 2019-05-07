loadCheck();
function  loadCheck(){
    var adminname=document.getElementById('adminname').value;
    var password=document.getElementById('password').value;
    var data = "adminname" +adminname +"password"+password;
    var params = {	"adminname":adminname, "password":password	}
    $.ajax({
         type:"post",
         url:"http://localhost:8888/adminlogin",
        data:params,
        dataType:"json",
        success:function(result) {
               alert(result )
        }
    });



}